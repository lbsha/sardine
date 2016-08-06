package sardine.server;

import sardine.route.SimpleRouteMatcher;

/**
 * @author bruce-sha
 *   2015/6/11
 */
public final class SardineServerFactory {

    private SardineServerFactory() {
    }

    public static SardineServer create(boolean hasStaticHandler) {
        MatcherProcessor matcherProcessor = new MatcherProcessor(SimpleRouteMatcher.singleton(), hasStaticHandler);
        // matcherFilter.init(null);
        // JettyHandler handler = new JettyHandler(matcherFilter);
        SardineInboundHandler handler = new SardineInboundHandler(matcherProcessor);
        return new SardineServer(handler);
    }

}
