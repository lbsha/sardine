package sardine.server;

import io.netty.handler.codec.http.FullHttpRequest;
import sardine.Request;
import sardine.Session;
import sardine.route.RouteMatched;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @auth bruce_sha
 * @date 2015/5/21
 */
final class RequestWrapper implements Request {

    public static RequestWrapper create(final Request request) {
        return new RequestWrapper(request);
    }


    private final Request delegate;

    private RequestWrapper(final Request request) {
        delegate = request;
    }

//    Request delegate() {
//        return delegate;
//    }

//    public void delegate(Request delegate) {
//        this.delegate = delegate;
//    }

    public void changeMatch(RouteMatched match) {
        ((SardineRequest) delegate).changeMatch(match);
    }

    @Override
    public Optional<String> paramsOptional(String param) {
        return delegate.paramsOptional(param);
    }

    @Override
    public String paramsOrElse(String param, String defaultValue) {
        return delegate.paramsOrElse(param, defaultValue);
    }

    @Override
    public boolean ajax() {
        return delegate.ajax();
    }

    @Override
    public String path() {
        return delegate.path();
    }

    @Override
    public String accept() {
        return delegate.accept();
    }

    @Override
    public Map<String, Object> parseJsonBody() {
        return delegate.parseJsonBody();
    }

    @Override
    public List<String> multiQueryParams(String queryParam) {
        return delegate.multiQueryParams(queryParam);
    }

    @Override
    public <T> T bodyExtract() {
        return delegate.bodyExtract();
    }

    public String method() {
        return delegate.method();
    }

    public String scheme() {
        return delegate.scheme();
    }

    public int port() {
        return delegate.port();
    }

//    public String pathInfo() {
//        return delegate.pathInfo();
//    }

//    public String servletPath() {
//        return delegate.servletPath();
//    }


//    public String contextPath() {
//        return delegate.contextPath();
//    }

    public String contentType() {
        return delegate.contentType();
    }

    public String body() {
        return delegate.body();
    }

    public byte[] bodyAsBytes() {
        return delegate.bodyAsBytes();
    }

    public int bodyLength() {
        return delegate.bodyLength();
    }

    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public Map<String, String> params() {
        return delegate.params();
    }

    public String params(String param) {
        return delegate.params(param);
    }

    public String[] splats() {
        return delegate.splats();
    }

    public List<String> splatsAsList() {
        return delegate.splatsAsList();
    }

    @Override
    public String splatsFirst() {
        return delegate.splatsFirst();
    }

    @Override
    public String splatsLast() {
        return delegate.splatsLast();
    }

    public String host() {
        return delegate.host();
    }

    public String ip() {
        return delegate.ip();
    }

    public String queryParams(String queryParam) {
        return delegate.queryParams(queryParam);
    }

    public String headers(CharSequence header) {
        return delegate.headers(header);
    }

    public Set<String> queryParams() {
        return delegate.queryParams();
    }

    public Set<String> headers() {
        return delegate.headers();
    }


//    public String queryString() {
//        return delegate.queryString();
//    }


    public FullHttpRequest raw() {
        return delegate.raw();
    }


    public String toString() {
        return delegate.toString();
    }


    public String userAgent() {
        return delegate.userAgent();
    }

//    public String url() {
//        return delegate.url();
//    }

    public String uri() {
        return delegate.uri();
    }


    public String protocol() {
        return delegate.protocol();
    }


//    public void attribute(String attribute, Object value) {
//        delegate.attribute(attribute, value);
//    }
//
//
//    public Object attribute(String attribute) {
//        return delegate.attribute(attribute);
//    }
//
//
//    public Set<String> attributes() {
//        return delegate.attributes();
//    }


    public Session session() {
        return delegate.session();
    }


    public Session session(boolean create) {
        return delegate.session(create);
    }

//
//    public QueryParamsMap queryMap() {
//        return delegate.queryMap();
//    }
//
//
//    public QueryParamsMap queryMap(String key) {
//        return delegate.queryMap(key);
//    }


    public Map<String, String> cookies() {
        return delegate.cookies();
    }


    public String cookie(String name) {
        return delegate.cookie(name);
    }


    public void rewrite(String uri) {
        delegate.rewrite(uri);
    }
}
