import org.roarincreations.linksender.LinkFrame;
import org.roarincreations.linksender.LinkText;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestLinkCore {
    private static int checks;
    private static void ok(boolean b, String name) { checks++; if (!b) throw new AssertionError(name); }
    private static void rejects(byte[] bytes, String name) {
        try { LinkFrame.receive(new ByteArrayInputStream(bytes)); throw new AssertionError(name); }
        catch (IOException expected) { checks++; }
    }
    private static void rejectsAck(byte[] bytes, String name) {
        try { LinkFrame.requireAck(new ByteArrayInputStream(bytes)); throw new AssertionError(name); }
        catch (IOException expected) { checks++; }
    }
    private static byte[] frame(String value) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); LinkFrame.send(out, value); return out.toByteArray();
    }
    public static void main(String[] args) throws Exception {
        ok(LinkText.extract("Hello").isEmpty(), "no URL");
        ok(LinkText.extract(null).isEmpty(), "null input");
        ok(LinkText.extract("Here: https://example.org/a.").equals(List.of("https://example.org/a")), "trailing punctuation");
        ok(LinkText.extract("(https://example.org/a(b))").equals(List.of("https://example.org/a(b)")), "balanced parens");
        ok(LinkText.extract("https://a.test/x and https://b.test/y").size() == 2, "two URLs");
        ok(LinkText.extract("https://a.test/x https://a.test/x").size() == 1, "dedupe");
        ok(LinkText.extract("HTTPS://example.org/X").size() == 1, "uppercase scheme");
        ok(LinkText.valid("https://example.org/mañana?x=%E2%9C%93"), "unicode path and percent encoding");
        ok(!LinkText.valid("file:///etc/passwd"), "file forbidden");
        ok(!LinkText.valid("javascript:alert(1)"), "js forbidden");
        ok(!LinkText.valid("https://user:pass@example.org/"), "credentials forbidden");
        ok(!LinkText.valid("https://example.org/\nsecret"), "newline forbidden");
        ok(!LinkText.valid("https://"), "no host");
        ok(!LinkText.valid("https://example.org:65536/x"), "bad port");
        ok(!LinkText.valid("https://example.org/" + "x".repeat(4100)), "too long");
        String longUrl = "https://example.org/" + "x".repeat(2500) + "?a=%E2%9C%93";
        ok(LinkFrame.receive(new ByteArrayInputStream(frame(longUrl))).equals(longUrl), "long URL frame");
        String unicode = "https://example.org/mañana?q=%E2%9C%93";
        ok(LinkFrame.receive(new ByteArrayInputStream(frame(unicode))).equals(unicode), "unicode frame");
        byte[] badMagic = frame("https://example.org/"); badMagic[0] = 0; rejects(badMagic, "magic");
        byte[] badLen = frame("https://example.org/"); badLen[7] = 0; rejects(badLen, "zero length");
        byte[] tooLarge = frame("https://example.org/"); tooLarge[4]=0;tooLarge[5]=0;tooLarge[6]=33;tooLarge[7]=0; rejects(tooLarge, "too large");
        byte[] truncated = frame("https://example.org/"); rejects(java.util.Arrays.copyOf(truncated,truncated.length-1), "truncated");
        byte[] invalid = frame("https://example.org/"); invalid[8]=(byte)0xff; rejects(invalid, "malformed utf8");
        try { LinkFrame.send(new ByteArrayOutputStream(), "not a url"); throw new AssertionError("bad outbound"); }
        catch (IOException expected) { checks++; }
        ok(!LinkText.valid("https://ex ample.org"), "whitespace forbidden");
        ok(!LinkText.valid("https://example.org/#a\rb"), "CR forbidden");
        ByteArrayOutputStream ack = new ByteArrayOutputStream();
        LinkFrame.acknowledge(ack);
        LinkFrame.requireAck(new ByteArrayInputStream(ack.toByteArray())); checks++;
        rejectsAck(new byte[]{0x00}, "negative ack");
        rejectsAck(new byte[0], "missing ack");
        System.out.println("PASS " + checks + " synthetic assertions; no Android SDK/device used");
    }
}
