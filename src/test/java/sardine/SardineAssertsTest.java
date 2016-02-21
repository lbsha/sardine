package sardine;

import java.time.LocalDateTime;

import static sardine.Sardine.get;
import static sardine.Sardine.staticFileLocation;

/**
 * Created by bruce on 15/11/13.
 */
public class SardineAssertsTest {

    public static void main(String[] args) {

        staticFileLocation("");

        get("/", () -> LocalDateTime.now());

    }

}
