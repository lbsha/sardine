package sardine.route;

import sardine.HttpMethod;
import sardine.log.Logs;
import sardine.utils.MimeParse;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @auth bruce-sha
 * @date 2015/5/21
 */
public class SimpleRouteMatcher {

    // 定义的顺序即是filter执行的顺序，指明order？
    final private List<RouteEntry> routerRoutes = new CopyOnWriteArrayList<>();
//    final private List<RouteEntry<Route.SardineRoute<?>>> routerRoutes = new CopyOnWriteArrayList<>();
//    final private List<RouteEntry<Filter.SardineFilter>> filterRoutes = new CopyOnWriteArrayList<>();

    SimpleRouteMatcher() {
    }

    public void add(final HttpMethod httpMethod, final String uri, final String acceptType, final Object target) {
        addRoute(httpMethod, uri, acceptType, target);
    }

    /**
     * finds target for a requestPart route
     *
     * @param httpMethod the http method
     * @param path       the path
     * @param accept     the accept type
     * @return the target
     */
    public Optional<RouteMatched> match(HttpMethod httpMethod, String path, String accept) {
        //会找出一大批，因为filter也是用这个找
        List<RouteEntry> routeEntries = this.findTargetsForRequestedRoute(httpMethod, path);
        //TODO 找出一个
        RouteEntry entry = findTargetWithGivenAcceptType(routeEntries, accept);

        return entry != null ? Optional.of(new RouteMatched(entry, path, accept)) : Optional.empty();
    }

    /**
     * Finds multiple targets for a requestPart route.
     *
     * @param httpMethod the http method
     * @param path       the route path
     * @param acceptType the accept type
     * @return the targets
     */
    public List<RouteMatched> matches(HttpMethod httpMethod, String path, String acceptType) {
        List<RouteMatched> matchSet = new ArrayList<>();
        List<RouteEntry> routeEntries = findTargetsForRequestedRoute(httpMethod, path);

        for (RouteEntry routeEntry : routeEntries) {
            if (acceptType != null) {
                String bestMatch = MimeParse.bestMatch(Arrays.asList(routeEntry.accept), acceptType);

                if (routeWithGivenAcceptType(bestMatch)) {
                    matchSet.add(new RouteMatched(routeEntry, path, acceptType));
                }
            } else {
                matchSet.add(new RouteMatched(routeEntry, path, acceptType));
            }
        }

        return matchSet;
    }

    public void clear() {
        routerRoutes.clear();
    }

    private void addRoute(HttpMethod method, String url, String acceptedType, Object target) {
        final RouteEntry entry = new RouteEntry(method, url, acceptedType, target);
        Logs.debug(() -> "add route: " + entry);
        routerRoutes.add(entry);
    }

    //can be cached? I don't think so.
    private Map<String, RouteEntry> getAcceptedMimeTypes(List<RouteEntry> routes) {
        Map<String, RouteEntry> acceptedTypes = new HashMap<>();

        for (RouteEntry routeEntry : routes) {
            if (!acceptedTypes.containsKey(routeEntry.accept)) {
                acceptedTypes.put(routeEntry.accept, routeEntry);
            }
        }

        return acceptedTypes;
    }

    private boolean routeWithGivenAcceptType(String bestMatch) {
        return !MimeParse.NO_MIME_TYPE.equals(bestMatch);
    }

    private List<RouteEntry> findTargetsForRequestedRoute(HttpMethod httpMethod, String path) {
        return routerRoutes
                .stream()
                .parallel()
                .filter(route -> route.match(httpMethod, path))
                .sorted((route, nextRoute) -> route.path.compareTo(nextRoute.path))
                .collect(Collectors.toList());

//        List<RouteEntry> matchSet = new ArrayList<RouteEntry>();
//        for (RouteEntry entry : routerRoutes) {
//            if (entry.match(method, path)) {
//                matchSet.add(entry);
//            }
//        }
//        return matchSet;
    }

    // TODO: I believe this feature has impacted performance. Optimization?
    private RouteEntry findTargetWithGivenAcceptType(List<RouteEntry> routeMatches, String accept) {

        if (accept != null && routeMatches.size() > 0) {
            Map<String, RouteEntry> acceptedMimeTypes = getAcceptedMimeTypes(routeMatches);
            String bestMatch = MimeParse.bestMatch(acceptedMimeTypes.keySet(), accept);

            if (routeWithGivenAcceptType(bestMatch)) {
                return acceptedMimeTypes.get(bestMatch);
            } else {
                return null;
            }
        } else {
            if (routeMatches.size() > 0) {
                return routeMatches.get(0);
            }
        }

        return null;
    }

    static private class RouteMatcherFactoryHolder {
        static final SimpleRouteMatcher instance = new SimpleRouteMatcher();
    }

    public static synchronized SimpleRouteMatcher singleton() {
        return RouteMatcherFactoryHolder.instance;
    }

}