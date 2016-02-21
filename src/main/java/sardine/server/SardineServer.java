package sardine.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.NetUtil;
import sardine.asset.AssetsHandler;
import sardine.log.Logs;

import java.util.Objects;
import java.util.Optional;

/**
 * @auth bruce-sha
 * @date 2015/5/21
 */
public class SardineServer {

    final private ChannelHandler handler;
    private EventLoopGroup workerGroup;
    private EventLoopGroup bossGroup;

    public SardineServer(ChannelHandler handler) {
        this.handler = Objects.requireNonNull(handler);
    }

    /**
     * Ignites the sardine server, listening on the specified port, running SSL secured with the specified keystore
     * and truststore.  If truststore is null, keystore is reused.
     *
     * @param host The address to listen on
     * @param port - the port
     * @param keyStoreFile - The keystore file location as string
     * @param keyStorePassword - the password for the keystore
     * @param trustStoreFile - the truststore file location as string, leave null to reuse keystore
     * @param trustStorePassword - the trust store password
     * @param staticFilesFolder - the route to static files in classPath
     * @param externalFilesFolder - the route to static files external to classPath.
     */
    public void fire(final String host,
                     final int port,
                     final Optional<String> staticFilesFolder,
                     final Optional<String> externalFilesFolder,
                     final Optional<String> keyStoreFile,
                     final Optional<String> keyStorePassword,
                     final Optional<String> trustStoreFile,
                     final Optional<String> trustStorePassword) {

        if (staticFilesFolder.isPresent()) AssetsHandler.staticFileFolder(staticFilesFolder.get());
        if (externalFilesFolder.isPresent()) AssetsHandler.externalStaticFileFolder(externalFilesFolder.get());

        try {
            workerGroup = new NioEventLoopGroup();
            bossGroup = new NioEventLoopGroup();

            final ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
                            // pipeline.addLast( new HttpContentCompressor());//不能针对 chunked
//                            pipeline.addLast(new HttpChunkContentCompressor());//全部压缩
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(handler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, NetUtil.SOMAXCONN)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            Channel channel = bootstrap.bind(host, port).sync().channel();

            Logs.log("service started on port {}", port);

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            Logs.error(e);
        } finally {
            stop();
        }
//        if (port == 0) {
//            try (ServerSocket s = new ServerSocket(0)) {
//                port = s.getLocalPort();
//            } catch (IOException e) {
//                logger.error("Could not singleton first available port (port set to 0), using default: {}", DEFAULT_PORT);
//                port = DEFAULT_PORT;
//            }
//        }
//
//        ServerConnector connector;
//
//        if (keyStoreFile == null) {
//            connector = createSocketConnector();
//        } else {
//            connector = createSecureSocketConnector(keyStoreFile,
//                                                    keyStorePassword, trustStoreFile, trustStorePassword);
//        }
//
//        // Set some timeout OPTIONS to make debugging easier.
//        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
//        connector.setSoLingerTime(-1);
//        connector.setHost(host);
//        connector.setPort(port);
//
//        server = connector.getServer();
//        server.setConnectors(new Connector[] {connector});
//
//        // Handle static file routes
//        if (staticFilesFolder == null && externalFilesFolder == null) {
//            server.setHandler(handler);
//        } else {
//            List<Handler> handlersInList = new ArrayList<Handler>();
//            handlersInList.add(handler);
//
//            // Set static file location
//            setStaticFileLocationIfPresent(staticFilesFolder, handlersInList);
//
//            // Set external static file location
//            setExternalStaticFileLocationIfPresent(externalFilesFolder, handlersInList);
//
//            HandlerList handlers = new HandlerList();
//            handlers.setHandlers(handlersInList.toArray(new Handler[handlersInList.size()]));
//            server.setHandler(handlers);
//        }
//
//        try {
//            logger.info("== {} has ignited ...", NAME);
//            logger.info(">> Listening on {}:{}", host, port);
//
//            server.start();
//            server.join();
//        } catch (Exception e) {
//            logger.error("fire failed",e);
//            System.exit(100); // NOSONAR
//        }
    }

    //http://netty.io/wiki/user-guide-for-5.x.html#wiki-h3-17
    public void stop() {

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

//        logger.info(">>> {} shutting down ...", NAME);
//        try {
//            if (server != null) {
//                server.stop();
//            }
//        } catch (Exception e) {
//            logger.error("stop failed",e);
//            System.exit(100); // NOSONAR
//        }
//        logger.info("done");
    }
//
//    /**
//     * Creates a secure jetty socket connector. Keystore required, truststore
//     * optional. If truststore not specifed keystore will be reused.
//     *
//     * @params keyStoreFile       The keystore file location as string
//     * @params keyStorePassword   the password for the keystore
//     * @params trustStoreFile     the truststore file location as string, leave null to reuse keystore
//     * @params trustStorePassword the trust store password
//     * @return a secure socket connector
//     */
//    private static ServerConnector createSecureSocketConnector(String keyStoreFile,
//                                                               String keyStorePassword, String trustStoreFile,
//                                                               String trustStorePassword) {
//
//        SslContextFactory sslContextFactory = new SslContextFactory(
//                keyStoreFile);
//
//        if (keyStorePassword != null) {
//            sslContextFactory.setKeyStorePassword(keyStorePassword);
//        }
//        if (trustStoreFile != null) {
//            sslContextFactory.setTrustStorePath(trustStoreFile);
//        }
//        if (trustStorePassword != null) {
//            sslContextFactory.setTrustStorePassword(trustStorePassword);
//        }
//        return new ServerConnector(new Server(), sslContextFactory);
//    }
//
//    /**
//     * Creates an ordinary, non-secured Jetty server connector.
//     *
//     * @return - a server connector
//     */
//    private static ServerConnector createSocketConnector() {
//        return new ServerConnector(new Server());
//    }
//
//    /**
//     * Sets static file location if present
//     */
//    private static void setStaticFileLocationIfPresent(String staticFilesRoute, List<Handler> handlersInList) {
//        if (staticFilesRoute != null) {
//            ResourceHandler resourceHandler = new ResourceHandler();
//            Resource staticResources = Resource.newClassPathResource(staticFilesRoute);
//            resourceHandler.setBaseResource(staticResources);
//            resourceHandler.setWelcomeFiles(new String[] {"index.html"});
//            handlersInList.add(resourceHandler);
//        }
//    }
//
//    /**
//     * Sets external static file location if present
//     */
//    private static void setExternalStaticFileLocationIfPresent(String externalFilesRoute,
//                                                               List<Handler> handlersInList) {
//        if (externalFilesRoute != null) {
//            try {
//                ResourceHandler externalResourceHandler = new ResourceHandler();
//                Resource externalStaticResources = Resource.newResource(new File(externalFilesRoute));
//                externalResourceHandler.setBaseResource(externalStaticResources);
//                externalResourceHandler.setWelcomeFiles(new String[] {"index.html"});
//                handlersInList.add(externalResourceHandler);
//            } catch (IOException exception) {
//                exception.printStackTrace(); // NOSONAR
//                System.err.println("Error during initialize external resource " + externalFilesRoute); // NOSONAR
//            }
//        }
//    }

}
