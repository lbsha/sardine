package sardine;

/**
 * http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol
 *
 * @auth bruce-sha
 * @date 2015/5/21
 */
public enum HttpMethod {

    GET, POST, HEAD, // http 1.0

    OPTIONS, PUT, DELETE, TRACE, CONNECT, // http 1.1

    PATCH,  // rfc...

    BEFORE, AFTER;// sinatra

}