package sardine;

/**
 * @author bruce-sha
 *         2015/5/21
 * @see <a href="http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">Hypertext Transfer Protocol</a>
 * @since 1.0.0
 */
public enum HttpMethod {

    GET, POST, HEAD, // HTTP 1.0

    OPTIONS, PUT, DELETE, TRACE, CONNECT, // HTTP 1.1

    PATCH,  // RFC...

    BEFORE, AFTER;// SINATRA
}