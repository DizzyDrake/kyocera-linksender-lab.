import org.roarincreations.linksender.LinkFrame;
import org.roarincreations.linksender.LinkText;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/** Offline source-only regressions. Never uses Android, Bluetooth, SMS or network. */
public final class TestWQA321LinkBoundaryAndFrame {
  static int n=0;
  static void yes(boolean x,String label) {n++;if(!x)throw new AssertionError(label);}
  static void rejects(byte[] b,String label) {
    try {LinkFrame.receiveMessage(new ByteArrayInputStream(b));throw new AssertionError(label);}
    catch(IOException expected){n++;}
  }
  static byte[] url(String v)throws Exception{ByteArrayOutputStream o=new ByteArrayOutputStream();LinkFrame.send(o,v);return o.toByteArray();}
  static byte[] text(String v)throws Exception{ByteArrayOutputStream o=new ByteArrayOutputStream();LinkFrame.sendText(o,v);return o.toByteArray();}
  public static void main(String[] a)throws Exception {
    // Never present a truncated destination as a selected link when a bare authority is malformed.
    for(String s: new String[]{"bit.ly%2Fsecret","i.cvs.com%2Fcoupon","bit.ly\\coupon",
        "bit.ly&evil","bit.ly=other","bit.ly+suffix","bit.ly$foo","bit.ly~user",
        "bit.ly:123456/path","bit.ly:abc/path","bit.ly:80abc","bit.ly_foo","bit.ly..other.com"}) {
      yes(LinkText.extract(s).isEmpty(),"malformed bare link was truncated: "+s+" => "+LinkText.extract(s));
    }
    for(String[] pair: new String[][] {
        {"bit.ly/path", "https://bit.ly/path"},
        {"i.cvs.com/offer?q=1#frag", "https://i.cvs.com/offer?q=1#frag"},
        {"bit.ly#part", "https://bit.ly#part"},
        {"bit.ly/path%2Fextra", "https://bit.ly/path%2Fextra"},
        {"bit.ly?x=1", "https://bit.ly?x=1"},
        {"Visit bit.ly/path.", "https://bit.ly/path"},
        {"bit.ly/path...", "https://bit.ly/path"},
        {"bit.ly...", "https://bit.ly"},
        {"https://bit.ly/path", "https://bit.ly/path"},
        {"https://bit.ly/path%2Fextra", "https://bit.ly/path%2Fextra"}
    }) yes(LinkText.extract(pair[0]).equals(List.of(pair[1])),"ordinary link changed: "+pair[0]);
    yes(LinkText.extract("me@bit.ly/path").isEmpty(),"email address not a link");
    yes(LinkText.extract("https://user:secret@example.org/").isEmpty(),"credential link rejected");
    yes(LinkFrame.validText("Please read bit.ly%2Fsecret"),"malformed link still valid explicit plain text");
    String chosen="https://i.cvs.com/offer?q=1#frag";
    byte[] legacy=url(chosen);
    yes(legacy[0]=='L'&&legacy[3]=='1',"URL stays LSB1 for older receiver");
    LinkFrame.Message lm=LinkFrame.receiveMessage(new ByteArrayInputStream(legacy));
    yes(!lm.isText && chosen.equals(lm.value),"legacy link exact roundtrip");
    String copied="Copied on purpose: bit.ly%2Fsecret\nEmoji 🙂";
    byte[] typed=text(copied);
    yes(typed[0]=='L'&&typed[3]=='2'&&typed[4]==2,"text LSB2 type 2");
    LinkFrame.Message tm=LinkFrame.receiveMessage(new ByteArrayInputStream(typed));
    yes(tm.isText && copied.equals(tm.value),"text exact roundtrip");
    try {LinkFrame.receive(new ByteArrayInputStream(typed));throw new AssertionError("text misrepresented as URL");}
    catch(IOException expected){n++;}
    byte[] wrongType=typed.clone();wrongType[4]=99;rejects(wrongType,"unsupported type");
    byte[] trunc=Arrays.copyOf(typed,typed.length-1);rejects(trunc,"truncated text");
    byte[] over=typed.clone();over[5]=0;over[6]=0;over[7]=32;over[8]=1;rejects(over,"overlong text");
    byte[] badUtf8=typed.clone();badUtf8[9]=(byte)0xFF;rejects(badUtf8,"invalid UTF8");
    byte[] badMagic=typed.clone();badMagic[3]=0;rejects(badMagic,"invalid magic");
    ByteArrayOutputStream noWrite=new ByteArrayOutputStream();
    try {LinkFrame.sendText(noWrite,"bad\ud800");throw new AssertionError("invalid surrogate accepted");}
    catch(IOException expected){n++;}
    yes(noWrite.size()==0,"invalid outbound text emits zero bytes");
    try {LinkFrame.requireAck(new ByteArrayInputStream(new byte[]{0x00}));throw new AssertionError("bad ack accepted");}
    catch(IOException expected){n++;}
    System.out.println("PASS "+n+" WQA321 independent boundary/frame assertions (host-only)");
  }
}
