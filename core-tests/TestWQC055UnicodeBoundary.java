import org.roarincreations.linksender.LinkText;
import java.util.List;
/** Offline source-frozen regression. Never silently select a substring of a Unicode hostname. */
public final class TestWQC055UnicodeBoundary {
  private static int count;
  private static int failures;
  private static void equal(String input, String... expected) {
    count++;
    List<String> actual=LinkText.extract(input);
    if (!actual.equals(List.of(expected)))
      throw new AssertionError("input="+input+" expected="+List.of(expected)+" actual="+actual);
  }
  private static void neverPartial(String input, String wrong) {
    count++;
    List<String> actual=LinkText.extract(input);
    if (actual.contains(wrong)) {
      failures++;
      System.err.println("FAIL PARTIAL HOSTNAME MISIDENTIFIED input="+input+" wrong="+wrong+" actual="+actual);
    }
  }
  public static void main(String[] args) {
    boolean regression=args.length>0 && args[0].equals("regression");
    if (!regression) {
      equal("bit.ly/abc", "https://bit.ly/abc");
      equal("i.cvs.com/offer?q=1", "https://i.cvs.com/offer?q=1");
      equal("xn--bcher-kva.de/shop", "https://xn--bcher-kva.de/shop");
      equal("me@bit.ly/abc");
      equal("/prefix/bit.ly/abc");
      equal("https://user:secret@example.org/");
      equal("Copied ordinary SMS text");
      System.out.println("PASS "+count+" positive cases; source only");
    } else {
      // A future fix may accept a complete IDN or reject it, but must never
      // silently transform it into a DIFFERENT ASCII hostname.
      neverPartial("b\u00fccher.de/shop", "https://cher.de/shop");
      neverPartial("ma\u00f1ana.com", "https://ana.com");
      neverPartial("\u00e9bit.ly/abc", "https://bit.ly/abc");
      neverPartial("\u6c49bit.ly/abc", "https://bit.ly/abc");
      neverPartial("a\u0301bit.ly/abc", "https://bit.ly/abc");
      neverPartial("\u0661bit.ly/abc", "https://bit.ly/abc");
      System.out.println("Unicode boundary: "+count+" checked, "+failures+" confirmed failures; source only");
      if (failures>0) throw new AssertionError(failures+" partial hostname misidentifications");
    }
  }
}
