package sardine.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import sardine.*;
import sardine.Filter.SardineFilter;
import sardine.HttpMethod;
import sardine.Request.SardineRequest;
import sardine.Response.SardineResponse;
import sardine.Route.SardineRoute;
import sardine.exception.ExceptionHandler;
import sardine.exception.ExceptionMapper;
import sardine.log.Logs;
import sardine.monitor.Metrics;
import sardine.route.RouteEntryMatched;
import sardine.route.RouteEntryMatcher;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.netty.handler.codec.http.HttpHeaderUtil.isKeepAlive;

/**
 * @author bruce_sha
 *         2015/5/31
 * @since 1.0.0
 */
public class MatcherProcessor {

    // http重写
    static private final String HTTP_METHOD_OVERRIDE_HEADER = "X-HTTP-Method-Override";
    static private final String HTTP_METHOD_OVERRIDE_FORM = "_method";

    private final RouteEntryMatcher routeMatcher;
    private final boolean hasStaticFilesHandlers;

    public MatcherProcessor(RouteEntryMatcher routeMatcher, boolean hasStaticFilesHandlers) {
        this.routeMatcher = routeMatcher;
        this.hasStaticFilesHandlers = hasStaticFilesHandlers;
    }

    private static <T> T unException(Route<T> t, Request request, Response response) {
        try {
            return t.apply(request, response);
        } catch (Exception e) {
            throw new SardineException(e);
        }
    }

    private static void uncheckedException(Filter f, Request request, Response response) {
        try {
            f.apply(request, response);
        } catch (Exception e) {
            throw new SardineException(e);
        }
    }

    private void before() {

    }

    private void handle() {

    }

    private void after() {

    }

    private String path(FullHttpRequest request) {
        return new QueryStringDecoder(request.uri()).path();
    }

    /**
     * Header中的 X-HTTP-Method-Override
     * 或 QueryString 中的 _method 拥有更高权重
     */
    private HttpMethod method(FullHttpRequest request) {

        final String method_override = request.headers().getAndConvert(HTTP_METHOD_OVERRIDE_HEADER);
        Optional<String> method = Optional.ofNullable(method_override);

        if (!method.isPresent()) {
            final Map<String, List<String>> query = new QueryStringDecoder(request.uri()).parameters();
            method = query.getOrDefault(HTTP_METHOD_OVERRIDE_FORM, Collections.emptyList()).stream().findFirst();
        }

        final String httpMethod = method.orElse(request.method().toString()).toUpperCase();

        return HttpMethod.valueOf(httpMethod);
    }

    private String accept(FullHttpRequest request) {
        String accept = request.headers().getAndConvert(HttpHeaderNames.ACCEPT);
        return accept == null ? SardineBase.DEFAULT_ACCEPT_TYPE : accept;
    }

    public void process(final FullHttpRequest httpRequest,
                        final FullHttpResponse httpResponse,
                        final ChannelHandlerContext ctx) {

        final HttpMethod method = method(httpRequest);
        final String accept = accept(httpRequest);
        String path = path(httpRequest);

        Logs.debug(() -> "sardine method: " + method + ", uri: " + httpRequest.uri() + ", accept: " + accept);

        final RequestWrapper requestWrapper = RequestWrapper.create(SardineRequest.create(httpRequest));
        final ResponseWrapper responseWrapper = ResponseWrapper.create(SardineResponse.create(httpResponse));

//        Request request = Request.SardineRequest.create(httpRequest);
//        requestWrapper.delegate(request);
//
//        Response response = Response.SardineResponse.create(httpResponse);
//        responseWrapper.delegate(response);


        //TODO 记录是否已消费，不能根据body为空判断
        try {

            /************************** 前置过滤器 ***************************/

            final List<RouteEntryMatched> beforeMatches = routeMatcher.matches(HttpMethod.BEFORE, path, accept);
            beforeMatches.stream()
                    .filter(e -> e.target() instanceof SardineFilter)
                    .forEach(e -> {

//                        if (requestWrapper.delegate() == null) {
//                            Request request = Request.SardineRequest.create(e, httpRequest);
//                            requestWrapper.delegate(request);
//                        } else {
                        requestWrapper.changeMatch(e);
//                        }

                        // 执行自定义的方法
                        uncheckedException((SardineFilter) e.target(), requestWrapper, responseWrapper);
                    });

            // Before Filter 中可通过 request.rewrite() 重写 URI
            path = path(httpRequest);


            /************************** 请求响应 ***************************/

            final Optional<RouteEntryMatched> match = routeMatcher.match(method, path, accept);

            if (match.isPresent()) {

                RouteEntryMatched routeMatched = match.get();
                Object target = routeMatched.target();

                if (target instanceof SardineRoute) {

                    SardineRoute route = (SardineRoute) target;

//                    if (requestWrapper.delegate() == null) {
//                        Request request = Request.SardineRequest.create(routeMatched, httpRequest);
//                        requestWrapper.delegate(request);
//                    } else {
                    requestWrapper.changeMatch(routeMatched);
//                    }

//                    responseWrapper.delegate(response);

                    // TODO 条件
                    Object result;
                    if (route.condition(requestWrapper)) {
                        result = route.apply(requestWrapper, responseWrapper);
                    } else {
                        responseWrapper.status(HttpResponseStatus.PRECONDITION_FAILED);
                        result = HttpResponseStatus.PRECONDITION_FAILED.reasonPhrase();
                    }

                    String resultAsString = route.render(result);
                    responseWrapper.body(resultAsString);
                }
            }

            /************************** 后置过滤器 ***************************/

            final List<RouteEntryMatched> afterMatches = routeMatcher.matches(HttpMethod.AFTER, path, accept);

            afterMatches.stream()
                    .filter(e -> e.target() instanceof SardineFilter)
                    .forEach(e -> {

//                        if (requestWrapper.delegate() == null) {
//                            Request request = Request.SardineRequest.create(e, httpRequest);
//                            requestWrapper.delegate(request);
//                        } else {
                        requestWrapper.changeMatch(e);
//                        }

                        // 执行自定义的方法
                        uncheckedException((SardineFilter) e.target(), requestWrapper, responseWrapper);
                    });

        } catch (HaltException he) {
            Logs.debug(() -> "sardine halt: " + he);

            responseWrapper.status(he.statusCode());
            responseWrapper.body(he.content());

        } catch (Exception e) {

            Logs.error("sardine error: ", e);

            Optional<ExceptionHandler> handler = ExceptionMapper.handler(e);
            if (handler.isPresent()) {
                handler.get().apply(e, requestWrapper, responseWrapper);
                Logs.debug(() -> "sardine exception handled.");
            } else {
                responseWrapper.status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
                responseWrapper.body(SardineBase.SERVER_ERROR);
                Logs.debug(() -> "sardine exception not handled.");
            }
        }

        boolean consumed = responseWrapper.isConsumed();
//        boolean consumed = response.body() != null;

        // If redirected and content is null set to empty string to not throw NotConsumedException
        if (!consumed && responseWrapper.isRedirected()) {
            responseWrapper.body(SardineBase.EMPTY);
            consumed = true;

            Logs.debug(() -> "sardine request redirected.");
        }

        /************************** 静态文件处理 ***************************/
        if (!consumed && hasStaticFilesHandlers) throw new NotConsumedException();

        if (!consumed) {
            responseWrapper.status(HttpResponseStatus.NOT_FOUND);
            responseWrapper.body(SardineBase.NOT_FOUND);
            consumed = true;

            Logs.debug(() -> "sardine request not found.");
        }

        if (consumed) {
            if (!httpResponse.headers().contains(HttpHeaderNames.CONTENT_TYPE))
                httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,
                        String.format("%s; charset=%s", SardineBase.DEFAULT_CONTENT_TYPE, StandardCharsets.UTF_8));

            httpResponse.content().writeBytes(responseWrapper.body().getBytes(StandardCharsets.UTF_8));
            int readableBytes = httpResponse.content().readableBytes();
            httpResponse.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, readableBytes);

            Metrics.responseBytesWithOutAsserts(readableBytes);

            final boolean keepAlive = isKeepAlive(httpRequest);

            if (keepAlive) httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            ChannelFuture lastContentFuture = ctx.writeAndFlush(httpResponse);

//            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}