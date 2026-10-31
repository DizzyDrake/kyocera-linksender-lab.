import java.util.List;
import org.roarincreations.linksender.LinkText;
/** Independent synthetic regression for WQB278 LinkText regex stack behavior. No Android APIs. */
public final class TestWQA279RegexStack {
  static int checks;
  static void eq(String input, List<String> expected, String label) {
    checks++;
    List<String> got;
    try { got = LinkText.extract(input); }
    catch (StackOverflowError error) { throw new AssertionError(label + ": StackOverflowError in LinkText.extract", error); }
    if (!expected.equals(got)) throw new AssertionError(label + ": expected=" + expected + ", got=" + got);
  }
  static void one(String input, String expected, String label) { eq(input, List.of(expected), label); }
  public static void main(String[] args) {
    String mid500 = "a'b".repeat(500), mid1000 = "a'b".repeat(1000);
    one("Read https://example.org/" + mid500 + " later", "https://example.org/" + mid500, "embedded HTTP 500");
    one("Read bit.ly/" + mid500 + " later", "https://bit.ly/" + mid500, "embedded bare 500");
    one("Read https://example.org/" + mid1000 + " later", "https://example.org/" + mid1000, "embedded HTTP 1000");
    one("Read bit.ly/" + mid1000 + " later", "https://bit.ly/" + mid1000, "embedded bare 1000");
    eq("Read https://example.org/" + "a'b".repeat(1500) + " later", List.of(), "overlong HTTP ignored");
    eq("Read bit.ly/" + "a'b".repeat(1500) + " later", List.of(), "overlong bare ignored");
    one("Read https://example.org/" + "a".repeat(2000) + " later", "https://example.org/" + "a".repeat(2000), "long ordinary HTTP token");
    one("Read bit.ly/" + "a".repeat(2000) + " later", "https://bit.ly/" + "a".repeat(2000), "long ordinary bare token");
    eq("a.".repeat(10000), List.of(), "many dotted fragments");
    one("'https://example.org/O'Reilly'", "https://example.org/O'Reilly", "quoted HTTP");
    one("'bit.ly/O'Reilly'", "https://bit.ly/O'Reilly", "quoted bare");
    eq("contact name@example.org", List.of(), "email suppressed");
    eq("plain message", List.of(), "plain text suppressed");
    one("https://example.org/ends'", "https://example.org/ends'", "exact standalone terminal apostrophe");
    System.out.println("PASS " + checks + " independent LinkText stack and punctuation assertions");
  }
}
