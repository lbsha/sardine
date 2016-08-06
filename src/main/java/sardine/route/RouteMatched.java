package sardine.route;

import sardine.HttpMethod;

import java.util.Objects;

/**
 * @author bruce_sha
 *   2015/5/21
 */
public class RouteMatched {

    final RouteEntry matchedRoute;

    final String requestURI;
    final String accept;

    RouteMatched(final RouteEntry matchedRoute, final String requestUri, final String accept) {
        this.matchedRoute = Objects.requireNonNull(matchedRoute);
        this.requestURI = Objects.requireNonNull(requestUri);
        this.accept = Objects.requireNonNull(accept);
    }

    public HttpMethod method() {
        return matchedRoute.method;
    }

    public Object target() {
        return matchedRoute.target;
    }

    public String routeUri() {
        return matchedRoute.path;
    }

    public String requestUri() {
        return requestURI;
    }

    public String accept() {
        return accept;
    }
}
