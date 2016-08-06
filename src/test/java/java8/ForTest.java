package java8;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bruce-sha
 *   2015/8/12
 */
public class ForTest {

    public static void main(String[] args) {
        List<String> dd = new ArrayList<String>();
        dd.add("1");
        dd.add("2");
        dd.add("3");

        dd.stream().parallel();

        for (String d : dd) {
            System.out.println(d);
            if (d.equals("2")) dd.remove(d);
        }

        for (int i = 0; i < dd.size(); i++) {
            System.out.println(dd.get(i));
            dd.remove(i);
        }
    }

}
