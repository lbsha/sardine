package sardine;


import io.netty.handler.codec.http.HttpResponseStatus;
import sardine.Filter.SardineFilter;
import sardine.Route.SardineRoute;
import sardine.log.Logs;
import sardine.route.SimpleRouteMatcher;
import sardine.server.SardineServer;
import sardine.server.SardineServerFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * @author bruce-sha
 *   2015/5/21
 */
public abstract class SardineBase {

    /* 公共参数 */
    public static final int DEFAULT_PORT = 9527;
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final String DEFAULT_ACCEPT_TYPE = "*/*";
    public static final String DEFAULT_CONTENT_TYPE = "text/html";
    public static final String ALL_PATHS = "!(*%)!!^";
    public static final String EMPTY = "";

    /* 静态文件 */
    static protected Optional<String> staticFileFolder = Optional.empty();
    static protected Optional<String> externalStaticFileFolder = Optional.empty();

    /* 标志位 */
    static protected volatile boolean started = false;
    static protected volatile boolean isDebug = false;
    static protected volatile boolean isParallel = true;

    static protected String application = "sardine";
    static protected String host = DEFAULT_HOST;
    static protected int port = DEFAULT_PORT;

    static protected SardineServer server;
    static protected SimpleRouteMatcher router;

//    // TODO: https暂时不支持
//    static protected Optional<String> keyStoreFile = Optional.empty();
//    static protected Optional<String> keyStorePassword = Optional.empty();
//    static protected Optional<String> trustStoreFile = Optional.empty();
//    static protected Optional<String> trustStorePassword = Optional.empty();

    public static void application(final String applicationName) {
        assertBeforeServerStarted();
        SardineBase.application = Objects.requireNonNull(applicationName);
    }

    public static String application() {
        return SardineBase.application;
    }

    public static synchronized void host(final String ip) {
        assertBeforeServerStarted();
        SardineBase.host = Objects.requireNonNull(ip);
    }

    public static synchronized void port(final int port) {
        assertBeforeServerStarted();
        if (port < 0 || port > 0xFFFF) throw new IllegalArgumentException("port out of range.");
        SardineBase.port = port;
    }

    public static synchronized void notFound(final String notFoundMessage) {
//        assertBeforeServerStarted();
        SardineBase.NOT_FOUND = notFoundMessage;
    }

    public static synchronized void serverError(final String serverErrorMessage) {
        assertBeforeServerStarted();
        SardineBase.SERVER_ERROR = serverErrorMessage;
    }

    public static synchronized void secure(final String keyStoreFile, final String keyStorePassword,
                                           final String trustStoreFile, final String trustStorePassword) {
        assertBeforeServerStarted();
        if (keyStoreFile == null) throw new IllegalArgumentException("keyStore file can't be null.");
//        SardineBase.keyStoreFile = Optional.of(keyStoreFile);
//        SardineBase.keyStorePassword = Optional.ofNullable(keyStorePassword);
//        SardineBase.trustStoreFile = Optional.ofNullable(trustStoreFile);
//        SardineBase.trustStorePassword = Optional.ofNullable(trustStorePassword);
    }

    public static synchronized void staticFileLocation(final String folder) {
//        start();
        assertBeforeServerStarted();
        SardineBase.staticFileFolder = Optional.ofNullable(folder);
        Logs.info(() -> "static file location: " + folder);
    }


    public static synchronized void externalStaticFileLocation(final String externalFolder) {
        assertBeforeServerStarted();
//        start();
        externalStaticFileFolder = Optional.ofNullable(externalFolder);
        Logs.info(() -> "external static file location: " + externalFolder);
    }

    static private void assertBeforeServerStarted() {
        if (started) throw new IllegalStateException("This must be called before route mapping.");
    }

    static private boolean hasStaticFileHandlers() {
        return staticFileFolder.isPresent() || externalStaticFileFolder.isPresent();
    }

    static protected SardineRoute build(final String path, final Route route) {
        return build(path, Optional.of(DEFAULT_ACCEPT_TYPE), route);
    }

    static protected SardineRoute build(final String path, final Condition condition, final Route route) {
        return new SardineRoute(path, DEFAULT_ACCEPT_TYPE, condition) {
            @Override
            public Object apply(Request request, Response response) throws Exception {
                return route.apply(request, response);
            }
        };
    }

    static protected SardineRoute build(final String path, final Optional<String> accept, final Route route) {
        return new SardineRoute(path, accept.orElse(DEFAULT_ACCEPT_TYPE), null) {
            @Override
            public Object apply(Request request, Response response) throws Exception {
                return route.apply(request, response);
            }
        };
    }

    static protected SardineFilter build(final String path, final Filter filter) {
        return build(path, Optional.of(DEFAULT_ACCEPT_TYPE), filter);
    }

    static protected SardineFilter build(final String path, final Optional<String> accept, final Filter filter) {
        return new SardineFilter(path, accept.orElse(DEFAULT_ACCEPT_TYPE)) {
            @Override
            public void apply(Request request, Response response) throws Exception {
                filter.apply(request, response);
            }
        };
    }

    static protected <T> void route(final HttpMethod httpMethod, final SardineRoute<T> route) {
        start();
        router.add(httpMethod, route.getPath(), route.getAccept(), route);
    }

    static protected void filter(final HttpMethod httpMethod, final SardineFilter filter) {
        start();
        router.add(httpMethod, filter.getPath(), filter.getAccept(), filter);
    }

    static protected synchronized void start() {

        if (started) return;

//        Config.initialize();

        router = SimpleRouteMatcher.singleton();

        // Stream.of(new Thread()).forEach(Thread::start);

        new Thread(() -> {
            server = SardineServerFactory.create(hasStaticFileHandlers());
            server.fire(
                    host,
                    port,
                    staticFileFolder,
                    externalStaticFileFolder/*,
                    keyStoreFile,
                    keyStorePassword,
                    trustStoreFile,
                    trustStorePassword*/);
        }).start();

        started = true;
    }


    public static synchronized void stop() {

        if (server == null || router == null) throw new IllegalStateException("null server or router.");

        router.clear();
        server.stop();
        started = false;
    }

    // TODO:设置调试模式
    static protected synchronized void debug(final boolean debug) {
        assertBeforeServerStarted();
        SardineBase.isDebug = debug;
    }

    public static String NOT_FOUND = String.format("<html><body><h2>%s</h2></body></html>", HttpResponseStatus.NOT_FOUND);
    public static String SERVER_ERROR = String.format("<html><body><h2>%s</h2></body></html>", HttpResponseStatus.INTERNAL_SERVER_ERROR);
}