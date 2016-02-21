package sardine.utils;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * @auth bruce-sha
 * @date 2015/6/18
 */
public class Utils {

    public static String path(final HttpRequest request) {
        final String uri = request.uri();
        return new QueryStringDecoder(uri).path();
    }


}
