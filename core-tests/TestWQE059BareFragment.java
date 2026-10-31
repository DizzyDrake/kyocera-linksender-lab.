import org.roarincreations.linksender.LinkText;
import org.roarincreations.linksender.LinkFrame;
import java.io.*;
import java.util.*;
/** Independent host-only source-derived URL-fragment fidelity regression. */
public final class TestWQE059BareFragment {
    static int count;
    static void links(String source, String... expected) {
        count++;
        List<String> actual = LinkText.extract(source);
        if (!actual.equals(List.of(expected)))
            throw new AssertionError("case="+count+" source="+source+" expected="+List.of(expected)+" actual="+actual);
    }
    public static void main(String[] args) throws Exception {
        // Existing positive controls (must not regress).
        links("bit.ly/path", "https://bit.ly/path");
        links("i.cvs.com/offer?q=1", "https://i.cvs.com/offer?q=1");
        links("https://bit.ly#part", "https://bit.ly#part");
        links("bit.ly/#part", "https://bit.ly/#part");
        links("bit.ly?x=1#part", "https://bit.ly?x=1#part");
        links("me@bit.ly#part");
        links("ébit.ly#part");
        // Owner-relevant: a bare domain fragment must not be silently discarded.
        links("bit.ly#part", "https://bit.ly#part");
        links("i.cvs.com#coupon", "https://i.cvs.com#coupon");
        links("bit.ly:443#part", "https://bit.ly:443#part");
        links("See bit.ly#part.", "https://bit.ly#part");
        links("See (bit.ly#(part))", "https://bit.ly#(part)");
        links("bit.ly#part bit.ly#other", "https://bit.ly#part", "https://bit.ly#other");
        // Transport still retains a selected normalized URL without changing type or bytes.
        String chosen = LinkText.extract("bit.ly#part").get(0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkFrame.send(out, chosen);
        String decoded = LinkFrame.receive(new ByteArrayInputStream(out.toByteArray()));
        if (!chosen.equals(decoded)) throw new AssertionError("LinkFrame URL fidelity");
        count++;
        System.out.println("PASS "+count+" WQE059 URL-fragment fidelity assertions; host Java only");
    }
}
