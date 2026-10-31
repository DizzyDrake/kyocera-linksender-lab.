import java.util.List;
import java.util.Random;
import org.roarincreations.linksender.LinkText;
/** Original WQB280 host-only adversarial checks; no Android SDK or phone exercised. */
public final class TestWQB280RegexSafety {
    private static int checks;
    private static void eq(String text, List<String> expected, String label) {
        checks++;
        List<String> actual = LinkText.extract(text);
        if (!expected.equals(actual)) throw new AssertionError(label + " expected=" + expected + " got=" + actual);
    }
    private static void one(String text, String expected, String label) {
        eq(text, List.of(expected), label);
    }
    private static void validOutputs(String text, String label) {
        checks++;
        for (String url : LinkText.extract(text)) {
            if (!LinkText.valid(url)) throw new AssertionError(label + ": invalid emitted URL");
            if (url.length() > LinkText.MAX_URL_CHARS) throw new AssertionError(label + ": oversize URL emitted");
        }
    }
    public static void main(String[] args) {
        String tail = "a'b".repeat(1200);
        one("see https://example.org/" + tail + " end", "https://example.org/" + tail, "long http apostrophe path");
        one("see bit.ly/" + tail + " end", "https://bit.ly/" + tail, "long bare apostrophe path");
        one("see https://example.org/" + "a".repeat(4000) + " end", "https://example.org/" + "a".repeat(4000), "near-limit http");
        eq("see https://example.org/" + "a".repeat(4100) + " end", List.of(), "over-limit http rejected");
        one("see bit.ly/" + "a".repeat(4000) + " end", "https://bit.ly/" + "a".repeat(4000), "near-limit bare");
        eq("see bit.ly/" + "a".repeat(4100) + " end", List.of(), "over-limit bare rejected");
        one("See i.cvs.com/offer?code=ab'cd later", "https://i.cvs.com/offer?code=ab'cd", "bare query apostrophe");
        one("See https://example.org/a'b?code=c'd later", "https://example.org/a'b?code=c'd", "http query apostrophe");
        one("'https://example.org/O'Reilly'", "https://example.org/O'Reilly", "quote delimiters");
        eq("person@example.org", List.of(), "email not URL");
        eq("plain copied SMS without links", List.of(), "plain text not URL");
        eq("See https://example.org/" + "a'b".repeat(2000) + " end", List.of(), "long mixed token rejected");
        validOutputs("a.".repeat(20000), "many dotted fragments");
        validOutputs("x".repeat(20000), "one giant token");
        validOutputs("https://example.org/" + "x".repeat(20000), "one giant URL");
        Random r = new Random(280);
        String[] parts = {"text", "https://example.org/a'b", "bit.ly/a'b", "person@example.org", "x.", "'", " ", "!", "http://foo.example/q=ok", "i.cvs.com/abc", "http://user:pass@example.org/"};
        for (int i=0;i<1200;i++) {
            StringBuilder sb=new StringBuilder();
            for (int j=0;j<1+r.nextInt(15);j++) sb.append(parts[r.nextInt(parts.length)]).append(' ');
            validOutputs(sb.toString(), "deterministic mixed sample " + i);
        }
        System.out.println("PASS " + checks + " WQB280 independent bounded-regex and output-validation assertions; host Java only");
    }
}
