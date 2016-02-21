package sardine;

import io.netty.handler.codec.http.HttpResponseStatus;
import sardine.Filter.VFilter;
import sardine.ResponseTransformer.SardineResponseTransformerRoute;
import sardine.Route.VRoute;
import sardine.exception.ExceptionHandler;
import sardine.exception.ExceptionMapper;
import sardine.mvc.ModelAndView;
import sardine.mvc.SardineTemplateViewRoute;
import sardine.mvc.TemplateEngine;
import sardine.mvc.TemplateViewRoute;

import java.util.Map;
import java.util.Optional;

/**
 * @auth bruce-sha
 * @date 2015/5/21
 */
public final class Sardine extends SardineBase {

    private Sardine() {
    }

    /* *********************************** 空参路由 ********************************** */

    public static void get(final String path, final VRoute route) {
        get(path, (request, response) -> route.apply());
    }

    public static void post(final String path, final VRoute route) {
        post(path, (request, response) -> route.apply());
    }

    public static void put(final String path, final VRoute route) {
        put(path, (request, response) -> route.apply());
    }

    public static void patch(final String path, final VRoute route) {
        patch(path, (request, response) -> route.apply());
    }

    public static void delete(final String path, final VRoute route) {
        delete(path, (request, response) -> route.apply());
    }

    public static void head(final String path, final VRoute route) {
        head(path, (request, response) -> route.apply());
    }

    public static void trace(final String path, final VRoute route) {
        trace(path, (request, response) -> route.apply());
    }

    public static void connect(final String path, final VRoute route) {
        connect(path, (request, response) -> route.apply());
    }

    public static void options(final String path, final VRoute route) {
        options(path, (request, response) -> route.apply());
    }

    /* *********************************** 全参路由 ********************************** */

    public static void get(final String path, final Route route) {
        route(HttpMethod.GET, build(path, route));
    }

    public static void post(final String path, final Route route) {
        route(HttpMethod.POST, build(path, route));
    }

    public static void put(final String path, final Route route) {
        route(HttpMethod.PUT, build(path, route));
    }

    public static void patch(final String path, final Route route) {
        route(HttpMethod.PATCH, build(path, route));
    }

    public static void delete(final String path, final Route route) {
        route(HttpMethod.DELETE, build(path, route));
    }

    public static void head(final String path, final Route route) {
        route(HttpMethod.HEAD, build(path, route));
    }

    public static void trace(final String path, final Route route) {
        route(HttpMethod.TRACE, build(path, route));
    }

    public static void connect(final String path, final Route route) {
        route(HttpMethod.CONNECT, build(path, route));
    }

    public static void options(final String path, final Route route) {
        route(HttpMethod.OPTIONS, build(path, route));
    }

      /* *********************************** 全参路由带accept头 ******************************** */

    public static void get(final String path, final String accept, final Route route) {
        route(HttpMethod.GET, build(path, Optional.ofNullable(accept), route));
    }

    public static void post(final String path, final String accept, final Route route) {
        route(HttpMethod.POST, build(path, Optional.ofNullable(accept), route));
    }

    public static void put(final String path, final String accept, final Route route) {
        route(HttpMethod.PUT, build(path, Optional.ofNullable(accept), route));
    }

    public static void patch(final String path, final String accept, final Route route) {
        route(HttpMethod.PATCH, build(path, Optional.ofNullable(accept), route));
    }

    public static void delete(final String path, final String accept, final Route route) {
        route(HttpMethod.DELETE, build(path, Optional.ofNullable(accept), route));
    }

    public static void head(final String path, final String accept, final Route route) {
        route(HttpMethod.HEAD, build(path, Optional.ofNullable(accept), route));
    }

    public static void trace(final String path, final String accept, final Route route) {
        route(HttpMethod.TRACE, build(path, Optional.ofNullable(accept), route));
    }

    public static void connect(final String path, final String accept, final Route route) {
        route(HttpMethod.CONNECT, build(path, Optional.ofNullable(accept), route));
    }

    public static void options(final String path, final String accept, final Route route) {
        route(HttpMethod.OPTIONS, build(path, Optional.ofNullable(accept), route));
    }


     /* ***********************************   带条件全参路由 ********************************** */

    //TODO 条件参数

    public static void get(final String path, final Condition condition, final Route route) {
//        get(path, route);
        route(HttpMethod.GET, build(path, route));
    }

    public static void post(final String path, final Condition condition, final Route route) {
        post(path, route);
    }


    /* ************************************** 过滤器 ************************************* */

    public static void before(final VFilter filter) {
        before((request, response) -> filter.apply());
    }

    public static void after(final VFilter filter) {
        after((request, response) -> filter.apply());
    }

    public static void before(final Filter filter) {
        before(SardineBase.ALL_PATHS, filter);
    }

    public static void after(final Filter filter) {
        after(SardineBase.ALL_PATHS, filter);
    }

    public static void before(final String path, final VFilter filter) {
        before(path, (request, response) -> filter.apply());
    }

    public static void after(final String path, final VFilter filter) {
        after(path, (request, response) -> filter.apply());
    }

    public static void before(final String path, final Filter filter) {
        before(path, DEFAULT_ACCEPT_TYPE, filter);
    }

    public static void after(final String path, final Filter filter) {
        after(path, DEFAULT_ACCEPT_TYPE, filter);
    }

    public static void before(final String path, final String acceptType, final Filter filter) {
        filter(HttpMethod.BEFORE, build(path, Optional.ofNullable(acceptType), filter));
    }

    public static void after(final String path, final String acceptType, final Filter filter) {
        filter(HttpMethod.AFTER, build(path, Optional.ofNullable(acceptType), filter));
    }

    /* ************************************** 模板引擎 ************************************* */

    public static void get(final String path,
                           final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.GET, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void get(final String path, final String accept,
                           final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.GET, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }

    public static void post(final String path,
                            final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.POST, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void post(final String path, final String accept,
                            final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.POST, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }

    public static void put(final String path,
                           final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.PUT, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void put(final String path, final String accept,
                           final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.PUT, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }

    public static void delete(final String path,
                              final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.DELETE, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void delete(final String path, final String accept,
                              final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.DELETE, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }

    public static void patch(final String path,
                             final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.PATCH, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void patch(final String path, final String accept,
                             final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.PATCH, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }

    public static void head(final String path,
                            final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.HEAD, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void head(final String path, final String accept,
                            final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.HEAD, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }

    public static void trace(final String path,
                             final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.TRACE, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void trace(final String path, final String accept,
                             final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.TRACE, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }

    public static void connect(final String path,
                               final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.CONNECT, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void connect(final String path, final String accept,
                               final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.CONNECT, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }

    public static void options(final String path,
                               final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.OPTIONS, SardineTemplateViewRoute.create(path, null, route, engine));
    }

    public static void options(final String path, final String accept,
                               final TemplateViewRoute route, final TemplateEngine engine) {
        route(HttpMethod.OPTIONS, SardineTemplateViewRoute.create(path, accept, null, route, engine));
    }


    /* ************************************** ResponseTransformer ************************************* */

    public static <T> void get(final String path, final Route<T> route,
                               final ResponseTransformer<T> transformer) {
        route(HttpMethod.GET, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void get(final String path, final String accept, final Route route,
                           final ResponseTransformer transformer) {
        route(HttpMethod.GET, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }

    public static void post(final String path, final Route route,
                            final ResponseTransformer transformer) {
        route(HttpMethod.POST, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void post(final String path, final String accept, final Route route,
                            final ResponseTransformer transformer) {
        route(HttpMethod.POST, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }

    public static void put(final String path, final Route route,
                           final ResponseTransformer transformer) {
        route(HttpMethod.PUT, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void put(final String path, final String accept, final Route route,
                           final ResponseTransformer transformer) {
        route(HttpMethod.PUT, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }

    public static void delete(final String path, final Route route,
                              final ResponseTransformer transformer) {
        route(HttpMethod.DELETE, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void delete(final String path, final String accept, final Route route,
                              final ResponseTransformer transformer) {
        route(HttpMethod.DELETE, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }

    public static void head(final String path, final Route route,
                            final ResponseTransformer transformer) {
        route(HttpMethod.HEAD, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void head(final String path, final String accept, final Route route,
                            final ResponseTransformer transformer) {
        route(HttpMethod.HEAD, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }

    public static void connect(final String path, final Route route,
                               final ResponseTransformer transformer) {
        route(HttpMethod.CONNECT, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void connect(final String path, final String accept, final Route route,
                               final ResponseTransformer transformer) {
        route(HttpMethod.CONNECT, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }

    public static void trace(final String path, final Route route,
                             final ResponseTransformer transformer) {
        route(HttpMethod.TRACE, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void trace(final String path, final String accept, final Route route,
                             final ResponseTransformer transformer) {
        route(HttpMethod.TRACE, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }

    public static void options(final String path, final Route route,
                               final ResponseTransformer transformer) {
        route(HttpMethod.OPTIONS, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void options(final String path, final String accept, final Route route,
                               final ResponseTransformer transformer) {
        route(HttpMethod.OPTIONS, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }

    public static void patch(final String path, final Route route,
                             final ResponseTransformer transformer) {
        route(HttpMethod.PATCH, SardineResponseTransformerRoute.create(path, null, route, transformer));
    }

    public static void patch(final String path, final String accept, final Route route,
                             final ResponseTransformer transformer) {
        route(HttpMethod.PATCH, SardineResponseTransformerRoute.create(path, accept, null, route, transformer));
    }


    /* ************************************** 异常处理 ************************************* */

    public static void exception(final Class<? extends Exception> exceptionClass, final ExceptionHandler handler) {
        ExceptionMapper.map(exceptionClass, (exception, request, response) -> handler.apply(exception, request, response));
    }

    /* *************************************** 停止 ************************************** */

    public static void halt() {
        throw new HaltException();
    }

    public static void halt(int status) {
        throw new HaltException(status);
    }

    public static void halt(String body) {
        throw new HaltException(body);
    }

    public static void halt(int status, String body) {
        throw new HaltException(status, body);
    }

    public static void halt(HttpResponseStatus status) {
        throw new HaltException(status);
    }

    /* *************************************** 继续下一个 ************************************** */

    //TODO continue
    public static void pass() {

    }


    /* *************************************** MVC ************************************** */

    public static ModelAndView modelAndView(Map<String, Object> model, String viewName) {
        return new ModelAndView(model, viewName);
    }
}
