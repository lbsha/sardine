package java8;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bruce-sha
 *   2015/6/15
 */
public class MapRemoveKeyTest {
    public static void main(String[] args) {
        Map m = new HashMap<>();
        m.put("a", "1");
        m.put("b", "2");

        System.out.println(m);

        m.keySet().remove("a");

        System.out.println(m);
    }
}
