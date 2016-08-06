package sardine;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author bruce-sha
 *   2015/6/17
 */
public class RoutesTest {
    public static void main(String[] args) {

        String[] s0 = Pattern.compile("/").split("/hello/");
        String[] s1 = Pattern.compile("/").split("/hello");

        String[] a0 = "".split("/");                // {""}
        String[] a1 = "/".split("/");               // {}
        System.out.println(a0.length);
        System.out.println(a1.length);
        String[] b0 = "/hello/*".split("/");        // {"","hello","*"}
        String[] b1 = "/hello/".split("/");         // {"","hello"}
        String[] b2 = "/hello".split("/");          // {"","hello"}

        String[] c0 = "/hello/*".split("/");        // {"","hello","*"}
        String[] c1 = "/hello/bruce/sha".split("/");// {"","hello","bruce","sha"}

        String[] d0 = "/hello/*.*".split("/");      // {"","hello","*.*"}

        Optional<String> n = Stream.of(c1).skip(c0.length - 1).reduce((join, e) -> join = join + '/' + e);

        System.out.println(n.get());
    }

}
