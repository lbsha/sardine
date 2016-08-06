package sardine;

import io.netty.handler.codec.http.*;
import sardine.log.Logs;

import java.util.Objects;

/**
 * @author bruce-sha
 *   2015/6/17
 */
public interface Response {

    void status(int httpStatusCode);

    /**
     * TODO：不要与netty耦合
     *   httpStatus
     */
    void status(HttpResponseStatus httpStatus);

    void contentType(String contentType);

    /**
     *   location
     * @return 返回空只是为方便调用，没什么具体意义
     */
    String redirect(String location);

    String redirect(String location, int httpStatusCode);

    String redirect(String location, HttpResponseStatus httpStatus);

    void header(CharSequence header, CharSequence value);

    void headerInt(CharSequence header, int value);

    void headerObject(CharSequence header, Object value);

    void cookie(String name, String value);

    void cookie(String name, String value, long maxAge);

    void cookie(String name, String value, long maxAge, boolean secured);

    void cookie(String path, String name, String value, long maxAge, boolean secured);

    void removeCookie(String name);

    void file(String filePath);

    void body(String body);

    String body();

    HttpResponse raw();

    /**
     * 沙丁响应
     */
    public class SardineResponse implements Response {

        private final FullHttpResponse response;
        private String body;

        private SardineResponse(final FullHttpResponse response) {
            this.response = response;
        }

        public static Response create(FullHttpResponse response) {
            return new SardineResponse(response);
        }

        @Override
        public void status(final int httpStatusCode) {
            response.setStatus(HttpResponseStatus.valueOf(httpStatusCode));
        }

        @Override
        public void status(final HttpResponseStatus httpStatus) {
            response.setStatus(httpStatus);
        }

        @Override
        public void contentType(final String contentType) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, Objects.requireNonNull(contentType));
        }

        @Override
        public String redirect(final String location) {
            //scalatra 是 302
            return redirect(location, HttpResponseStatus.FOUND);
        }

        @Override
        public String redirect(final String location, final int httpStatusCode) {
            return redirect(location, HttpResponseStatus.valueOf(httpStatusCode));
        }

        @Override
        public String redirect(final String location, final HttpResponseStatus httpStatus) {
            body(SardineBase.EMPTY);
            response.setStatus(httpStatus);
            response.headers().set(HttpHeaderNames.LOCATION, location);

            Logs.debug(() -> ("redirecting to " + location + " with " + httpStatus));

            return SardineBase.EMPTY;
        }

        @Override
        public void header(final CharSequence header, final CharSequence value) {
            response.headers().set(header, value);
        }

        @Override
        public void headerInt(final CharSequence header, final int value) {
            response.headers().setInt(header, value);
        }

        @Override
        public void headerObject(CharSequence header, Object value) {
            response.headers().setObject(header, value);
        }

        @Override
        public void cookie(final String name, final String value) {
            cookie(name, value, Long.MIN_VALUE, false);
        }

        // seconds
        @Override
        public void cookie(final String name, final String value, final long maxAge) {
            cookie(name, value, maxAge, false);
        }

        @Override
        public void cookie(final String name, final String value, final long maxAge, final boolean secured) {
            cookie(null, name, value, maxAge, secured);
        }

        @Override
        public void cookie(final String path, final String name, final String value, final long maxAge, final boolean secured) {

            final Cookie cookie = new DefaultCookie(name, value);
            cookie.setPath(path);
            cookie.setMaxAge(maxAge);
            cookie.setSecure(secured);
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode(cookie));
        }

        @Override
        public void removeCookie(final String name) {
            final Cookie cookie = new DefaultCookie(name, "");
            cookie.setMaxAge(0);
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode(cookie));
        }

        //TODO：直接转向文件
        @Override
        public void file(final String filePath) {
//        get("/home", (req, res) -> {
//            byte[] encoded = java.nio.file.Files.readAllBytes(Paths.get(path));
//            String s = new String(encoded, Charset.defaultCharset());
//            return s;
//        });
        }

        @Override
        public void body(final String body) {
//            this.body = Objects.requireNonNull(body);
            this.body = body;
        }

        @Override
        public String body() {
            return body;
        }

        @Override
        public HttpResponse raw() {
            return response;
        }

        @Override
        public String toString() {
            return response.toString() + body();
        }
    }
}
