import org.roarincreations.linksender.LinkFrame;
import org.roarincreations.linksender.LinkText;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Source-only Java protocol and parsing regression tests; no Android SDK/device. */
public class TestTextTransfer {
    private static int checks;
    private static void ok(boolean value, String label) { checks++; if (!value) throw new AssertionError(label); }
    private static void fails(byte[] bytes, String label) {
        try { LinkFrame.receiveMessage(new ByteArrayInputStream(bytes)); throw new AssertionError(label); }
        catch (IOException expected) { checks++; }
    }
    private static byte[] textFrame(String text) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); LinkFrame.sendText(out,text); return out.toByteArray();
    }
    private static byte[] urlFrame(String url) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); LinkFrame.send(out,url); return out.toByteArray();
    }
    public static void main(String[] args) throws Exception {
        String example="https://example.org/";
        byte[] old=urlFrame(example);
        ok(old[0]=='L'&&old[1]=='S'&&old[2]=='B'&&old[3]=='1',"URL uses legacy magic");
        ok(LinkFrame.receive(new ByteArrayInputStream(old)).equals(example),"legacy URL receive");
        LinkFrame.Message legacy=LinkFrame.receiveMessage(new ByteArrayInputStream(old));
        ok(!legacy.isText && legacy.value.equals(example),"new receiver accepts old URL");
        ok(LinkFrame.validText("Copied from an SMS"),"plain text valid");
        ok(LinkFrame.validText("Line 1\nLine 2\t🙂"),"newlines, tabs and emoji");
        ok(!LinkFrame.validText(null),"null text");
        ok(!LinkFrame.validText(" \n\t "),"blank text");
        ok(!LinkFrame.validText("a\u0000b"),"NUL text");
        ok(!LinkFrame.validText("a\u0007b"),"BEL text");
        ok(!LinkFrame.validText("x".repeat(8193)),"over 8192 bytes");
        ok(LinkFrame.validText("x".repeat(8192)),"exact max bytes");
        ok(!LinkFrame.validText("🙂".repeat(2049)),"emoji byte limit");
        ok(LinkFrame.validText("🙂".repeat(2048)),"emoji at byte limit");
        ok(!LinkFrame.validText("\ud800"),"unpaired high surrogate");
        ok(!LinkFrame.validText("\udc00"),"unpaired low surrogate");
        String sms="Text copied on purpose: i.cvs.com/abc?x=1\nMore: bit.ly/q8 and 🙂";
        byte[] sent=textFrame(sms);
        ok(sent[0]=='L'&&sent[1]=='S'&&sent[2]=='B'&&sent[3]=='2'&&sent[4]==2,"typed text magic and type");
        LinkFrame.Message message=LinkFrame.receiveMessage(new ByteArrayInputStream(sent));
        ok(message.isText && message.value.equals(sms),"text roundtrip exact");
        try {LinkFrame.receive(new ByteArrayInputStream(sent));throw new AssertionError("text mislabeled URL");}
        catch(IOException expected){checks++;}
        ok(LinkFrame.receiveMessage(new ByteArrayInputStream(textFrame("🙂".repeat(2048)))).value.length()==4096,"max byte text roundtrip");
        try {LinkFrame.sendText(new ByteArrayOutputStream(),"\n");throw new AssertionError("blank outbound");}
        catch(IOException expected){checks++;}
        byte[] invalidType=sent.clone();invalidType[4]=99;fails(invalidType,"invalid type");
        byte[] truncated=Arrays.copyOf(sent,sent.length-1);fails(truncated,"truncated text");
        byte[] zeroLen=sent.clone();Arrays.fill(zeroLen,5,9,(byte)0);fails(zeroLen,"zero length");
        byte[] tooLarge=sent.clone();tooLarge[5]=0;tooLarge[6]=0;tooLarge[7]=33;tooLarge[8]=0;fails(tooLarge,"oversize typed frame");
        byte[] invalidUtf8=sent.clone();invalidUtf8[9]=(byte)0xff;fails(invalidUtf8,"invalid UTF8");
        byte[] invalidCtrl=textFrame("ab");invalidCtrl[9]=0;fails(invalidCtrl,"invalid NUL inbound");
        byte[] wrongMagic=sent.clone();wrongMagic[3]=0;fails(wrongMagic,"bad magic");
        ok(LinkText.extract("Visit bit.ly/xyz").equals(List.of("https://bit.ly/xyz")),"bare bitly");
        ok(LinkText.extract("i.cvs.com/offer?q=1").equals(List.of("https://i.cvs.com/offer?q=1")),"bare cvs");
        ok(LinkText.extract("bit.ly/path.").equals(List.of("https://bit.ly/path")),"bare trailing punctuation");
        ok(LinkText.extract("https://bit.ly/path").equals(List.of("https://bit.ly/path")),"already full URL");
        ok(LinkText.extract("HTTP://example.org/hello").equals(List.of("HTTP://example.org/hello")),"http legacy");
        ok(LinkText.extract("https://a.test/x bit.ly/z").size()==2,"mixed links");
        ok(LinkText.extract("ordinary text without any links").isEmpty(),"ordinary text no false link");
        ok(LinkText.extract("me@bit.ly/path").isEmpty(),"no link from email");
        ok(LinkText.extract("/prefix/bit.ly/path").isEmpty(),"no link from path fragment");
        ok(LinkText.extract("https://user:secret@example.org/").isEmpty(),"reject credential URL");
        ok(LinkText.extract("https://bit.ly/x https://bit.ly/x").size()==1,"dedupe full URLs");
        ok(LinkText.extract("(i.cvs.com/a(b))").equals(List.of("https://i.cvs.com/a(b)")),"bare balanced parens");
        System.out.println("PASS " + checks + " v0.1.3 text/URL/protocol assertions; no Android SDK/device used");
    }
}
