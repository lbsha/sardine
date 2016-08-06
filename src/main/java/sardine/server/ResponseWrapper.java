package sardine.server;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import sardine.Response;

/**
 * @author bruce-sha
 *   2015/6/17
 */
class ResponseWrapper implements Response {

    public static ResponseWrapper create(final Response response) {
        return new ResponseWrapper(response);
    }

    private volatile boolean redirected = false;
    private volatile boolean consumed = false;

    private final Response delegate;

    private ResponseWrapper(final Response response) {
        this.delegate = response;
    }

    @Override
    public void status(HttpResponseStatus httpStatus) {
        delegate.status(httpStatus);
    }

    @Override
    public void file(String filePath) {
        delegate.file(filePath);
    }

    @Override
    public void status(int statusCode) {
        delegate.status(statusCode);
    }

    @Override
    public void body(String body) {
        consumed = true;
        delegate.body(body);
    }

    @Override
    public String body() {
        return delegate.body();
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public HttpResponse raw() {
        return delegate.raw();
    }

    @Override
    public String redirect(String location) {
        redirected = true;
        return delegate.redirect(location);
    }

    @Override
    public String redirect(String location, int httpStatusCode) {
        redirected = true;
        return delegate.redirect(location, httpStatusCode);
    }

    @Override
    public String redirect(String location, HttpResponseStatus httpStatus) {
        redirected = true;
        return delegate.redirect(location, httpStatus);
    }

    boolean isRedirected() {
        return redirected;
    }

    boolean isConsumed() {
        return consumed;
    }

    @Override
    public void header(CharSequence header, CharSequence value) {
        delegate.header(header, value);
    }

    @Override
    public void headerInt(CharSequence header, int value) {
        delegate.headerInt(header, value);
    }

    @Override
    public void headerObject(CharSequence header, Object value) {
        delegate.headerObject(header, value);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public void contentType(String contentType) {
        delegate.contentType(contentType);
    }

    @Override
    public void cookie(String name, String value) {
        delegate.cookie(name, value);
    }

    @Override
    public void cookie(String name, String value, long maxAge) {
        delegate.cookie(name, value, maxAge);
    }

    @Override
    public void cookie(String name, String value, long maxAge, boolean secured) {
        delegate.cookie(name, value, maxAge, secured);
    }

    @Override
    public void cookie(String path, String name, String value, long maxAge, boolean secured) {
        delegate.cookie(path, name, value, maxAge, secured);
    }

    @Override
    public void removeCookie(String name) {
        delegate.removeCookie(name);
    }

}