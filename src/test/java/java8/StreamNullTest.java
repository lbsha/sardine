package java8;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @auth bruce-sha
 * @date 2015/6/16
 */
public class StreamNullTest {
    public static void main(String[] args) {
        Stream.of(Optional.ofNullable(null)).filter(e -> e.isPresent()).forEach(e -> System.out.println(e));
    }
}
