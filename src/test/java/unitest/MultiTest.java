package unitest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author bruce-sha
 *   2015/6/18
 */
public class MultiTest {

    public static final int count = 100;
    public static final int multi = 100;

    public static void main(String[] args) throws UnirestException {

        for (int i = 0; i < multi; i++) {
            new Thread(() -> {
                int j = count;
                while (j-- > 0) {
                    try {
                        HttpResponse<String> response = Unirest.post("http://localhost:9527/hello.html")
                                .header("Accept-Encoding", "gzip")
                                .asString();
                        System.out.println(response.getBody());
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }).start();
        }
        ;
    }
}
