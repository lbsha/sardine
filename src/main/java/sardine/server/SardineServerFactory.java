package sardine.server;

import sardine.route.RouteEntryMatcher;

/**
 * @author bruce-sha
 *         2015/6/11
 * @since 1.0.0
 */
public final class SardineServerFactory {

    private SardineServerFactory() {
    }

    public static SardineServer create(final boolean hasStaticHandler) {
        MatcherProcessor matcherProcessor = new MatcherProcessor(RouteEntryMatcher.singleton(), hasStaticHandler);
        // matcherFilter.init(null);
        // JettyHandler handler = new JettyHandler(matcherFilter);
        SardineInboundHandler handler = new SardineInboundHandler(matcherProcessor);
        return new SardineServer(handler);
    }

}
