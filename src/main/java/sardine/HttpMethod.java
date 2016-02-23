package sardine;

/**
 * http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol
 *
 * @auth bruce-sha
 * @date 2015/5/21
 */
public enum HttpMethod {

    GET, POST, HEAD, // HTTP 1.0

    OPTIONS, PUT, DELETE, TRACE, CONNECT, // HTTP 1.1

    PATCH,  // RFC...

    BEFORE, AFTER;// SINATRA
}