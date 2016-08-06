package unitest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author bruce-sha
 *   2015/6/18
 */
public class HelloTest {
    public static void main(String[] args) throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://httpbin.org/post")
                .header("accept", "application/json")
                .queryString("apiKey", "123")
                .field("parameter", "value")
                .field("foo", "bar")
                .asJson();
    }
}
