import org.roarincreations.linksender.LinkText;
import java.util.List;

/** Bounded hostile-text parsing regression; pure Java, no Android APIs. */
public class TestRegexBounded {
    static int assertions = 0;
    static void check(boolean condition, String label) {
        assertions++;
        if (!condition) throw new AssertionError(label);
    }
    public static void main(String[] args) {
        for (int count : new int[] {100, 1000, 3000, 5000, 10000}) {
            String input = "a.".repeat(count);
            List<String> urls;
            try { urls = LinkText.extract(input); }
            catch (StackOverflowError err) { throw new AssertionError("Regex stack overflow at " + count + " dotted labels", err); }
            check(urls.isEmpty(), "dotted fragments must not invent a link: " + count);
        }
        check(LinkText.extract("bit.ly/abc").equals(List.of("https://bit.ly/abc")), "bare short link");
        check(LinkText.extract("i.cvs.com/offer").equals(List.of("https://i.cvs.com/offer")), "nested host");
        check(LinkText.extract("example.com.").equals(List.of("https://example.com")), "trailing period");
        check(LinkText.extract("a.b.example.org/path?").equals(List.of("https://a.b.example.org/path")), "nested host and trailing punctuation");
        check(LinkText.extract("hello@example.org").isEmpty(), "do not parse email address");
        check(LinkText.extract("ordinary message 🙂").isEmpty(), "ordinary text");
        check(LinkText.extract("https://example.org/ x.y.example.org/a").size()==2, "mixed full and bare URLs");
        System.out.println("PASS " + assertions + " regex bounded-host assertions (host Java only)");
    }
}
