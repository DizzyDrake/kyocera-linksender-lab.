import org.roarincreations.linksender.LinkText;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
/** Independent synthetic equivalence and stress test; no Android or device. */
public final class TestWQA281TrimEquivalence {
  private static void add(MessageDigest digest, String value) {
    byte[] b=value.getBytes(StandardCharsets.UTF_8);
    digest.update((byte)(b.length>>>24));digest.update((byte)(b.length>>>16));
    digest.update((byte)(b.length>>>8));digest.update((byte)b.length);digest.update(b);
  }
  public static void main(String[] args) throws Exception {
    MessageDigest digest=MessageDigest.getInstance("SHA-256");
    Random r=new Random(281);
    String chars="aZ01()[]!?.,;/-'";
    int checks=0;
    for(int i=0;i<10000;i++) {
      StringBuilder s=new StringBuilder("Copied: https://example.org/");
      int n=r.nextInt(60);
      for(int j=0;j<n;j++) s.append(chars.charAt(r.nextInt(chars.length())));
      s.append(" END");
      List<String> found=LinkText.extract(s.toString());
      add(digest,found.toString()); checks++;
    }
    String[] representative={
      "Read https://example.org/a)))) now",
      "Read https://example.org/a[]]]]] now",
      "Read https://example.org/a((()))))) now",
      "Read https://example.org/a]!.?,; now",
      "Read bit.ly/a)))) now",
      "Read https://example.org/a'b)) later",
      "https://example.org/a))",
      "bit.ly/a(b)",
      "i.cvs.com/a[1]"
    };
    for(String s:representative){add(digest,LinkText.extract(s).toString());checks++;}
    String hex=HexFormat.of().formatHex(digest.digest());
    System.out.println("equivalence_digest="+hex+" cases="+checks);
    for(int n: new int[]{4096,8192,16384,32768,65536}) {
      String sample="Copied: https://example.org/a"+")".repeat(n)+" END";
      long st=System.nanoTime();List<String> got=LinkText.extract(sample);
      long ms=(System.nanoTime()-st)/1_000_000;
      if(!got.equals(List.of("https://example.org/a"))) throw new AssertionError("trim mismatch at "+n+" got "+got);
      System.out.println("unmatched_closing_parens="+n+" elapsed_ms="+ms+" expected_url_retained=true");
    }
  }
}
