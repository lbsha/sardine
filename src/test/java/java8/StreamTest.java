package java8;

import java.util.stream.IntStream;

/**
 * @author bruce-sha
 *   2015/5/22
 */
public class StreamTest {

    public static void main(String[] args) {

        IntStream.range(0, 10).forEach(i -> System.out.println(i));
        System.out.println("   ");
        IntStream.range(0, 10).parallel().forEach(i -> System.out.println(i));
    }
}
