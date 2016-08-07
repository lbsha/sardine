package sardine.route;

import sardine.HttpMethod;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author bruce_sha
 *         2015/5/21
 * @since 1.0.0
 */
public class RouteEntryMatched {

    final RouteEntry matchedRoute;

    final String requestUri;
    final String accept;

    RouteEntryMatched(final RouteEntry matchedRoute, final String requestUri, final String accept) {
        this.matchedRoute = Objects.requireNonNull(matchedRoute);
        this.requestUri = Objects.requireNonNull(requestUri);
        this.accept = Objects.requireNonNull(accept);
    }


    public HttpMethod method() {
        return matchedRoute.method;
    }

    public Object target() {
        return matchedRoute.target;
    }

    public String routeUri() {
        return matchedRoute.path;
    }

    public String requestUri() {
        return requestUri;
    }

    public String accept() {
        return accept;
    }


    //约束：两个长度必须相等，只有两个特殊情况
    //特殊情况： /hello/* /hello/ 此时前者长1
    //特殊情况： /hello/* /hello/bruce/sha 此时后者长n
    public List<RoutePathEntry> toRoutePathEntries() {

        final String realPath = this.requestUri();
        final String routePath = this.routeUri();

        String[] routePathArray = routePath.split("/");
        String[] realPathArray = realPath.split("/");

        int realPathLength = realPathArray.length;
        int routePathLength = routePathArray.length;

        //相等没的说
        if (routePathLength == realPathLength) {
            return IntStream.range(0, realPathLength)
                    .mapToObj(i -> new RoutePathEntry(realPathArray[i], routePathArray[i]))
                    .collect(Collectors.toList());
        } else {
//            if (!routePath.endsWith("*")) throw new RuntimeException();
            //特殊情况： /hello/* /hello/bruce/sha 此时后者长n
            if (routePathLength < realPathLength) {
                realPathArray[routePathLength - 1] = Stream
                        .of(realPathArray)
                        .skip(routePathLength - 1)
                        .reduce((join, e) -> join = join + '/' + e).get();
                return IntStream.range(0, routePathLength)
                        .mapToObj(i -> new RoutePathEntry(realPathArray[i], routePathArray[i]))
                        .collect(Collectors.toList());
            }
            //特殊情况： /hello/* /hello/ 此时前者长1
            else if (routePathLength == realPathLength + 1) {
                List<RoutePathEntry> re = IntStream
                        .range(0, realPathLength)
                        .mapToObj(i -> new RoutePathEntry(realPathArray[i], routePathArray[i]))
                        .collect(Collectors.toList());
                re.add(new RoutePathEntry("", "*"));
                return re;
            }
        }

        throw new RuntimeException();

//        String[] realPathArray = realPath.split("/");
//        String[] routePathArray = routePath.split("/");
//
////        if(true)
////        {
////            int length = Math.min(requestPathArray.length, routePathArray.length);
////            final List<RoutePathEntry> re = new ArrayList<>();
////            for (int i = 0; i < length; i++) {
////                re.add(new RoutePathEntry(requestPathArray[i], routePathArray[i]));
////            }
////            return re;
////        }
//
//        int length = Math.max(realPathArray.length, routePathArray.length);
//
//        if (routePathArray.length < length) routePathArray = Arrays.copyOf(routePathArray, length);
//        else if (realPathArray.length < length) realPathArray = Arrays.copyOf(realPathArray, length);
//
//        final List<RoutePathEntry> re = new ArrayList<>();
//        for (int i = 0; i < length; i++) {
//            re.add(new RoutePathEntry(realPathArray[i], routePathArray[i]));
//        }
//        return re;
    }
}
