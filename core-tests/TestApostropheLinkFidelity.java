import org.roarincreations.linksender.LinkText;
import java.util.List;

/** Host-only URL picker tests: apostrophes interior to URL tokens, not quote delimiters. */
public final class TestApostropheLinkFidelity {
    private static int checks = 0;
    private static void equal(String text, String... expected) {
        checks++;
        List<String> got = LinkText.extract(text);
        if (!got.equals(List.of(expected))) throw new AssertionError("input=" + text + " expected=" + List.of(expected) + " got=" + got);
    }
    public static void main(String[] args) {
        equal("Read https://example.org/O'Reilly for details.", "https://example.org/O'Reilly");
        equal("See https://example.org/it's-here?tag=can't today.", "https://example.org/it's-here?tag=can't");
        equal("'https://example.org/O'Reilly'", "https://example.org/O'Reilly");
        equal("Try bit.ly/O'Reilly tomorrow.", "https://bit.ly/O'Reilly");
        equal("See i.cvs.com/it's-here?tag=can't today.", "https://i.cvs.com/it's-here?tag=can't");
        equal("Visit https://example.org/O'Reilly or https://example.net/it's-here.", "https://example.org/O'Reilly", "https://example.net/it's-here");
        equal("https://example.org/O'Reilly", "https://example.org/O'Reilly");
        equal("https://example.org/it's-here?tag=can't", "https://example.org/it's-here?tag=can't");
        equal("'https://example.org/hello'", "https://example.org/hello");
        equal("Visit https://example.org/hello' later", "https://example.org/hello");
        equal("Visit https://example.org/hello' today", "https://example.org/hello");
        equal("Visit https://example.org/path. tomorrow", "https://example.org/path");
        equal("https://example.org/path!", "https://example.org/path!");
        equal("See https://example.org/path! today", "https://example.org/path");
        equal("https://user:pass@example.org/O'Reilly");
        equal("Send this as plain text, not a link");
        equal("mail me at test@example.org");
        equal("See <https://example.org/O'Reilly>", "https://example.org/O'Reilly");
        equal("Read \"https://example.org/O'Reilly\"", "https://example.org/O'Reilly");
        equal("Read https://example.org/a?tag=can't, then leave.", "https://example.org/a?tag=can't");
        System.out.println("PASS " + checks + " apostrophe URL fidelity assertions");
    }
}
