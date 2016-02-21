package java8;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultCookie;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @auth bruce-sha
 * @date 2015/6/15
 */
public class CookieTest {
    public static void main(String[] args) {
        Set<Cookie> cookies = new HashSet<>();
        cookies.add(new DefaultCookie("id", "123"));

        Optional<Cookie> cookie = cookies.stream().filter(c -> c.name().equalsIgnoreCase("id")).findFirst();

        if (cookie.isPresent())
            System.out.println(cookie.get());
        else
            System.out.println("none");
    }
}
