import org.roarincreations.linksender.LinkFrame;
import org.roarincreations.linksender.LinkText;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/** Synthetic, host-only selected-link fidelity regression. No Android/phone/SMS. */
public final class TestExplicitStandaloneUrl {
  private static int checks = 0;
  private static void check(boolean yes, String what) {
    checks++;
    if (!yes) throw new AssertionError(what);
  }
  public static void main(String[] args) throws Exception {
    String[] complete = {
      "https://example.org/download?sig=abc!",
      "https://example.org/a;",
      "https://example.org/a?",
      "https://example.org/a,",
      "https://example.org/a.",
      "http://example.org/path!"
    };
    for (String value : complete) {
      check(LinkText.extract(value).equals(List.of(value)), "complete explicit URL changed: " + value);
    }
    check(LinkText.extract("  https://example.org/a! \n").equals(List.of("https://example.org/a!")), "outer whitespace only");
    check(LinkText.extract("Read https://example.org/a! later").equals(List.of("https://example.org/a")), "prose still conservative");
    check(LinkText.extract("bit.ly/path.").equals(List.of("https://bit.ly/path")), "bare-domain compatibility");
    check(LinkText.extract("i.cvs.com/offer").equals(List.of("https://i.cvs.com/offer")), "nested bare domain");
    check(LinkText.extract("https://example.org/a! https://example.org/b.").equals(List.of("https://example.org/a", "https://example.org/b")), "multiple links conservative");
    check(LinkText.extract("ordinary copied text").isEmpty(), "do not invent URL");
    check(LinkText.extract("https://user:pass@example.org/a!").isEmpty(), "reject credential URL");
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    LinkFrame.send(output, complete[0]);
    check(LinkFrame.receive(new ByteArrayInputStream(output.toByteArray())).equals(complete[0]), "frame retains exact selected URL");
    check(LinkText.extract("https://example.org/a\nhttps://example.org/b").size() == 2, "line separated links");
    System.out.println("PASS " + checks + " host-only explicit standalone URL assertions");
  }
}
