package sardine;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import sardine.log.Logs;
import sardine.route.RouteMatched;
import sardine.route.Routes;
import sardine.route.Routes.RoutePathEntry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @auth bruce-sha
 * @date 2015/6/17
 */
public interface Request {

    Map<String, String> params();

    String param(String param);

    Optional<String> paramOptional(String param);

    String paramOrElse(String param, String defaultValue);

    String[] splats();

    List<String> splatsAsList();

    String firstSplat();

    String lastSplat();

    String method();

    boolean ajax();

    String scheme();

    String host();

    int port();

    String uri();

    String path();

    Set<String> headers();

    String header(CharSequence header);

    String userAgent();

    String accept();

    String contentType();

    String ip();

    String body();

    byte[] bodyAsBytes();

    int bodyLength();

    // TODO：parse body from json to map
    Map<String, Object> parseJsonBody();

    Set<String> queryParams();

    String queryParam(String queryParam);

    List<String> multiQueryParam(String queryParam);

    Map<String, String> cookies();

    String cookie(String name);

    Session session();

    Session session(boolean create);

    FullHttpRequest raw();

    String protocol();

    //TODO: 直接修改请求uri，其他值不变
    void rewrite(String location);

    <T> T bodyExtract();

    /**
     * 沙丁请求
     */
    public class SardineRequest implements Request {

        private FullHttpRequest request;

        private Set<String> headers; // http 头

        private List<String> splats; // 雪花参数
        private Map<String, String> params; // 路由参数
        private Map<String, List<String>> queryParams; // query参数

        private String body; // http body as string

        // TODO：暂不支持
        private Session session;

        SardineRequest(final FullHttpRequest request/*, final RouteMatched match*/) {
            this.request = request;
//            changeMatch(match);
        }

        public static Request create(/*RouteMatched match, */FullHttpRequest request) {
            return new SardineRequest(request/*, match*/);
        }

        public void changeMatch(final RouteMatched match) {

            List<RoutePathEntry> routePathEntries = Routes.routePathEntries(match.requestUri(), match.routeUri());

            splats = Collections.unmodifiableList(parseSplats(routePathEntries));
            Logs.debug(() -> "sardine parse splats: " + splats);

            params = Collections.unmodifiableMap(parseParams(routePathEntries));
            Logs.debug(() -> "sardine parse param: " + params);

            queryParams = Collections.unmodifiableMap(parseQueryParams());
            Logs.debug(() -> "sardine query param: " + queryParams);
        }

        private List<String> parseSplats(final Collection<Routes.RoutePathEntry> routePathEntries) {
            return routePathEntries
                    .stream()
                    .parallel()
                    .filter(e -> Routes.notNull(e.requestPart()))
                    .filter(e -> Routes.notNull(e.routePart()))
                    .filter(e -> Routes.isSplat(e.routePart()))
                    .map(e -> e.requestPart())
                    .collect(Collectors.toList());
        }

        private Map<String, String> parseParams(final Collection<Routes.RoutePathEntry> routePathEntries) {
            return routePathEntries
                    .stream()
                    .parallel()
                    .filter(e -> Routes.notNull(e.requestPart()))
                    .filter(e -> Routes.notNull(e.routePart()))
                    .filter(e -> Routes.isParam(e.routePart()))
                    .collect(Collectors.toMap(e -> e.routePart(), e -> e.requestPart()));
        }

        private Map<String, List<String>> parseQueryParams() {
            // query string
            final QueryStringDecoder query = new QueryStringDecoder(uri());
            final Map<String, List<String>> queryParams = new HashMap<>(query.parameters());

            //TODO multipart/form-data
            if (!"application/x-www-form-urlencoded".equalsIgnoreCase(contentType())) return queryParams;

            // http body
            final HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            final List<InterfaceHttpData> bodyHttpDatas = decoder.getBodyHttpDatas();
            bodyHttpDatas.stream()
                    .parallel()
                    .filter(e -> e.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
                    .map(e -> (Attribute) e)
                    .map(e -> {
                        try {
                            return new AbstractMap.SimpleImmutableEntry<String, String>(e.getName(), e.getValue());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .forEach(e -> {
                        String key = e.getKey();
                        if (!queryParams.containsKey(key)) queryParams.putIfAbsent(key, new ArrayList<>(1));
                        queryParams.get(key).add(e.getValue());
                    });
            return queryParams;
        }

        @Override
        public Map<String, String> params() {
            return params;
        }

        @Override
        public String param(final String param) {
            if (param == null) return null;
            if (param.startsWith(":"))
                return params.get(param);
            else
                return params.get(":" + param);
        }

        @Override
        public Optional<String> paramOptional(final String param) {
            return Optional.ofNullable(param(param));
        }

        @Override
        public String paramOrElse(final String param, final String defaultValue) {
            return Optional.ofNullable(param(param)).orElse(defaultValue);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String[] splats() {
            return splats.toArray(new String[splats.size()]);
        }

        @Override
        public List<String> splatsAsList() {
            return splats;
        }

        @Override
        public String firstSplat() {
            if (splats.isEmpty()) return null;
            return splats.stream().findFirst().get();
        }

        @Override
        public String lastSplat() {
            if (splats.isEmpty()) return null;
            return splats.stream().reduce((previous, current) -> current).get();
        }

        @Override
        public String method() {
            return request.method().toString();
        }

        @Override
        public boolean ajax() {
            return header("X-Requested-With") != null;
        }

        @Override
        public String scheme() {
            //TODO：暂不支持 https
            throw new UnsupportedOperationException();
        }

        @Override
        public String host() {
            return request.headers().getAndConvert(HttpHeaderNames.HOST);
        }

        @Override
        public int port() {
            return Sardine.port;
        }

        @Override
        public String uri() {
            return request.uri();
        }

        @Override
        public String path() {
            return new QueryStringDecoder(uri()).path();
        }

        @Override
        public Set<String> headers() {
            if (headers == null) {
                List<CharSequence> names = request.headers().namesList();
                headers = names.stream().map(e -> e.toString()).collect(Collectors.toSet());
            }
            return headers;
        }

        @Override
        public String header(final CharSequence header) {
            return request.headers().getAndConvert(header);
        }

        @Override
        public String userAgent() {
            return request.headers().getAndConvert(HttpHeaderNames.USER_AGENT);
        }

        @Override
        public String accept() {
            return request.headers().getAndConvert(HttpHeaderNames.ACCEPT);
        }

        @Override
        public String contentType() {
            return request.headers().getAndConvert(HttpHeaderNames.CONTENT_TYPE);
        }

        @Override
        public String ip() {
            return request.headers().getAndConvert("X-SARDINE-IP");
        }

        @Override
        public String body() {
            if (body == null)
                body = request.content().toString(StandardCharsets.UTF_8);
            return body;
        }

        @Override
        public byte[] bodyAsBytes() {
            return request.content().array();
        }

        @Override
        public int bodyLength() {
            return request.content().readableBytes();
        }

        // TODO：parse body from json to map
        @Override
        public Map<String, Object> parseJsonBody() {
            return null;
        }

        @Override
        public Set<String> queryParams() {
            return queryParams.keySet();
        }

        @Override
        public String queryParam(final String queryParam) {
            return multiQueryParam(queryParam).stream().findFirst().orElse(null);
        }

        @Override
        public List<String> multiQueryParam(final String queryParam) {
            return queryParams.getOrDefault(queryParam, Collections.emptyList());
        }

        @Override
        public Map<String, String> cookies() {

            final String cookie = request.headers().getAndConvert(HttpHeaderNames.COOKIE);
            if (cookie == null) return Collections.emptyMap();

            final Set<Cookie> cookies = ServerCookieDecoder.decode(cookie);
            return cookies.stream().collect(Collectors.toMap(Cookie::name, c -> c.value()));
        }

        @Override
        public String cookie(final String name) {

            final String cookie = request.headers().getAndConvert(HttpHeaderNames.COOKIE);
            if (cookie == null) return null;

            final Set<Cookie> cookies = ServerCookieDecoder.decode(cookie);
            final Optional<Cookie> ok = cookies.stream().filter(c -> c.name().equals(name)).findFirst();

            if (!ok.isPresent()) return null;
            else return ok.get().value();
        }

        /**
         * Returns the current session associated with this request,
         * or if the request does not have a session, creates one.
         *
         * @return the session associated with this request
         */
        @Override
        public Session session() {
            //TODO:buru
            throw new RuntimeException();
//        if (session == null) {
//            session = new Session(request.getSession());
//        }
//        return session;
        }

        /**
         * Returns the current session associated with this request, or if there is
         * no current session and <code>create</code> is true, returns  a new session.
         *
         * @param create <code>true</code> to create a new session for this request if necessary;
         * <code>false</code> to return null if there's no current session
         * @return the session associated with this request or <code>null</code> if
         * <code>create</code> is <code>false</code> and the request has no valid session
         */
        @Override
        public Session session(final boolean create) {
//        if (session == null) {
//            HttpSession httpSession = request.getSession(create);
//            if (httpSession != null) {
//                session = new Session(httpSession);
//            }
//        }
//        return session;
            //TODO:buru
            throw new RuntimeException();
        }

        @Override
        public FullHttpRequest raw() {
            return request;
        }

        @Override
        public String protocol() {
            return request.protocolVersion().text().toString();
        }

        //TODO: 直接修改请求uri，其他值不变
        @Override
        public void rewrite(final String location) {
            request.setUri(Objects.requireNonNull(location));
        }

        @Override
        public <T> T bodyExtract() {
            return null;
        }

        @Override
        public String toString() {
            return request.toString();
        }
    }
}
