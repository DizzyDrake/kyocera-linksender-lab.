import java.util.*;
import org.roarincreations.linksender.LinkText;
/** Independent frozen-source regression for bare-host authority fidelity. */
public final class TestWQB324BareAuthority {
 private static int count = 0;
 private static void expect(String input, String... expected) {
  List<String> got = LinkText.extract(input);
  List<String> want = Arrays.asList(expected);
  if (!got.equals(want)) throw new AssertionError(input + " expected=" + want + " actual=" + got);
  count++;
 }
 public static void main(String[] args) {
  if (args.length > 0 && args[0].equals("observe")) {
   for (String s : new String[]{"bit.ly:123456/path", "i.cvs.com:123456/offer", "bit.ly:abc/path", "bit.ly@other.example/path", "bit.ly:999999/path", "bit.ly:1234/path", "example.com."})
    System.out.println(s + " -> " + LinkText.extract(s));
   return;
  }
  // Never propose a DIFFERENT link by silently dropping malformed authority.
  expect("bit.ly:123456/path");
  expect("i.cvs.com:123456/offer");
  expect("bit.ly:abc/path");
  expect("bit.ly@other.example/path");
  expect("bit.ly:999999/path");
  // Positive controls: legitimate links must survive unchanged.
  expect("bit.ly:1234/path", "https://bit.ly:1234/path");
  expect("bit.ly#fragment", "https://bit.ly#fragment");
  expect("https://bit.ly:123456/path");
  expect("bit.ly:0/path", "https://bit.ly:0/path");
  expect("bit.ly:12345/path", "https://bit.ly:12345/path");
  expect("i.cvs.com/offer", "https://i.cvs.com/offer");
  expect("Visit bit.ly/abc today", "https://bit.ly/abc");
  System.out.println("PASS " + count + " bare authority fidelity cases");
 }
}
