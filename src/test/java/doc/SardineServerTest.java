package doc;

import static sardine.Sardine.get;
import static sardine.Sardine.port;

/**
 * @auth bruce-sha
 * @date 2015/6/17
 */
public class SardineServerTest {

    public static void main(String[] args) {

        port(9527);

        get("/", () -> "hello sardine.");

        get("/hello", request -> request.cookie("token") != null, (request, response) -> {
            return "hello sardine.";
        });
    }

}
