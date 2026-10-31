import org.roarincreations.linksender.LinkText;
import org.roarincreations.linksender.LinkFrame;
import java.io.*;
import java.util.*;
/** Fictional two-mode owner-trial checks: no Android SDK, Bluetooth, SMS or network. */
public final class TestWQC059OwnerTrialContract {
  private static int checks;
  static void yes(boolean b, String label) { checks++; if (!b) throw new AssertionError(label); }
  static byte[] url(String s) throws Exception { ByteArrayOutputStream out=new ByteArrayOutputStream(); LinkFrame.send(out,s); return out.toByteArray(); }
  static byte[] text(String s) throws Exception { ByteArrayOutputStream out=new ByteArrayOutputStream(); LinkFrame.sendText(out,s); return out.toByteArray(); }
  public static void main(String[] args) throws Exception {
    String chosen=LinkText.extract("Coupon: i.cvs.com#offer and bit.ly/path?x=1#section").get(0);
    yes(chosen.equals("https://i.cvs.com#offer"),"bare fragment preserved");
    yes(LinkText.extract("Coupon: i.cvs.com#offer and bit.ly/path?x=1#section").size()==2,"two links remain choices");
    byte[] frame=url(chosen);
    yes(frame[0]=='L' && frame[3]=='1',"selected link retains legacy LSB1 frame");
    LinkFrame.Message m=LinkFrame.receiveMessage(new ByteArrayInputStream(frame));
    yes(!m.isText && chosen.equals(m.value),"new receiver handles legacy link without rewriting");
    String pasted="A copied text message, with bit.ly#part and 🙂\nDo not open automatically.";
    byte[] textFrame=text(pasted);
    yes(textFrame[0]=='L' && textFrame[3]=='2' && textFrame[4]==2,"explicit text uses LSB2 type 2");
    LinkFrame.Message t=LinkFrame.receiveMessage(new ByteArrayInputStream(textFrame));
    yes(t.isText && pasted.equals(t.value),"text roundtrips exact including fragment and Unicode");
    try {LinkFrame.receive(new ByteArrayInputStream(textFrame));throw new AssertionError("text misreported as URL");}
    catch(IOException expected) { checks++; }
    yes(LinkText.extract("bücher.de/shop").isEmpty(),"never extract wrong ASCII tail from IDN");
    yes(LinkText.extract("ordinary copied text").isEmpty(),"ordinary text not auto-converted into URL");
    yes(LinkText.extract("https://user:secret@example.org").isEmpty(),"credential URL not selected");
    System.out.println("PASS "+checks+" WQC059 source-only sender/receiver trial assertions");
  }
}
