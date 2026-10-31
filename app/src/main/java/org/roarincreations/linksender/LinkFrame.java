package org.roarincreations.linksender;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

/** One bounded, deliberately selected payload per secure paired-device RFCOMM connection.
 *  LSB1 is preserved for existing URL-only receivers; LSB2 adds explicit payload type.
 *  Neither type is opened, fetched, logged, or persisted by this class. */
public final class LinkFrame {
    private static final int MAGIC_URL_V1 = 0x4c534231; // LSB1: [magic][length][UTF-8 URL]
    private static final int MAGIC_TYPED_V2 = 0x4c534232; // LSB2: [magic][type byte][length][UTF-8]
    public static final int MAX_BYTES = 8192;
    private static final int RECEIVED_ACK = 0x06;
    private static final int URL = 1;
    private static final int TEXT = 2;
    private LinkFrame() { }

    public static final class Message {
        public final boolean isText;
        public final String value;
        private Message(boolean text, String value) { this.isText = text; this.value = value; }
    }

    /** Retain legacy LSB1 encoding for HTTP(S) links and older receivers. */
    public static void send(OutputStream output, String url) throws IOException {
        if (!LinkText.valid(url)) throw new IOException("Invalid or unsupported URL");
        byte[] bytes = encode(url);
        DataOutputStream data = new DataOutputStream(output);
        data.writeInt(MAGIC_URL_V1);
        data.writeInt(bytes.length);
        data.write(bytes);
        data.flush();
    }

    /** Explicit text mode. No SMS access or automatic URL selection. Requires v0.1.3 receiver. */
    public static void sendText(OutputStream output, String text) throws IOException {
        if (!validText(text)) throw new IOException("Invalid or empty text");
        byte[] bytes = encode(text);
        DataOutputStream data = new DataOutputStream(output);
        data.writeInt(MAGIC_TYPED_V2);
        data.writeByte(TEXT);
        data.writeInt(bytes.length);
        data.write(bytes);
        data.flush();
    }

    private static byte[] encode(String value) throws IOException {
        // The default Java UTF-8 encoder silently replaces unpaired surrogates: reject instead.
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isHighSurrogate(c)) {
                if (++i >= value.length() || !Character.isLowSurrogate(value.charAt(i)))
                    throw new IOException("Invalid Unicode surrogate");
            } else if (Character.isLowSurrogate(c)) throw new IOException("Invalid Unicode surrogate");
        }
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 1 || bytes.length > MAX_BYTES) throw new IOException("Message too large");
        return bytes;
    }

    /** Text may contain normal line breaks and tabs, but not NUL/other control characters. */
    public static boolean validText(String text) {
        if (text == null || text.trim().isEmpty()) return false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isISOControl(c) && c != '\n' && c != '\r' && c != '\t') return false;
        }
        try { encode(text); return true; } catch (IOException e) { return false; }
    }

    /** Blocks until receiver validates the full frame. Cancel by closing the socket. */
    public static void requireAck(InputStream input) throws IOException {
        if (input.read() != RECEIVED_ACK) throw new IOException("Receiver did not acknowledge payload");
    }
    public static void acknowledge(OutputStream output) throws IOException {
        output.write(RECEIVED_ACK);
        output.flush();
    }

    public static Message receiveMessage(InputStream input) throws IOException {
        DataInputStream data = new DataInputStream(input);
        int magic = data.readInt();
        int type;
        if (magic == MAGIC_URL_V1) type = URL;
        else if (magic == MAGIC_TYPED_V2) {
            type = data.readUnsignedByte();
            if (type != URL && type != TEXT) throw new IOException("Unsupported message type");
        } else throw new IOException("Unrecognized message");
        int length = data.readInt();
        if (length < 1 || length > MAX_BYTES) throw new IOException("Invalid message length");
        byte[] bytes = new byte[length];
        data.readFully(bytes);
        final String value;
        try {
            value = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) { throw new IOException("Invalid UTF-8", e); }
        if (type == URL && !LinkText.valid(value)) throw new IOException("Invalid received URL");
        if (type == TEXT && !validText(value)) throw new IOException("Invalid received text");
        return new Message(type == TEXT, value);
    }

    /** Historical URL-only caller compatibility. Never mislabel received text as a URL. */
    public static String receive(InputStream input) throws IOException {
        Message message = receiveMessage(input);
        if (message.isText) throw new IOException("Received text where URL was expected");
        return message.value;
    }
}
