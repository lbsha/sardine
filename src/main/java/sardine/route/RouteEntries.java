package sardine.route;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author bruce-sha
 *         2015/5/21
 * @since 1.0.0
 */
public final class RouteEntries {

    private RouteEntries() {
    }

//    //约束：两个长度必须相等，只有两个特殊情况
//    //特殊情况： /hello/* /hello/ 此时前者长1
//    //特殊情况： /hello/* /hello/bruce/sha 此时后者长n
//    public static boolean isOK(final String realPath, final String routePath) {
//
//        // 完全相等
//        if (routePath.equals(realPath)) return true;
//
//        // /结尾需要谨慎 /hello/应该匹配 /hello/*或者/hello/ 而不是/hello  但是后两者/hello/与/hello split出来是完全一样的{"","hello"}
//        if (realPath.endsWith("/") && !((routePath.endsWith("/") || routePath.endsWith("*"))))
//            return false;
//
//        final String[] routePathArray = routePath.split("/");
//        final String[] realPathArray = realPath.split("/");
//
//        final int routePathLength = routePathArray.length;
//        final int realPathLength = realPathArray.length;
//
//        //如果不等最后一位必须是*
//        if (routePathLength != realPathLength) {
//            if (!routePath.endsWith("*")) return false;
//            if (routePathLength > realPathLength + 1) return false;
//        }
//
//        return !IntStream.range(0, Math.min(routePathLength, realPathLength))
//                .parallel()
//                .filter(i -> !routePathArray[i].startsWith(":"))
//                .filter(i -> !routePathArray[i].equals("*"))
//                .anyMatch(i -> !routePathArray[i].equals(realPathArray[i]));
//    }

//    //约束：两个长度必须相等，只有两个特殊情况
//    //特殊情况： /hello/* /hello/ 此时前者长1
//    //特殊情况： /hello/* /hello/bruce/sha 此时后者长n
//    public static List<RoutePathEntry> routePathEntries(final String realPath, final String routePath) {
//        String[] routePathArray = routePath.split("/");
//        String[] realPathArray = realPath.split("/");
//
//        int realPathLength = realPathArray.length;
//        int routePathLength = routePathArray.length;
//
//        //相等没的说
//        if (routePathLength == realPathLength) {
//            return IntStream.range(0, realPathLength)
//                    .mapToObj(i -> new RoutePathEntry(realPathArray[i], routePathArray[i]))
//                    .collect(Collectors.toList());
//        } else {
////            if (!routePath.endsWith("*")) throw new RuntimeException();
//            //特殊情况： /hello/* /hello/bruce/sha 此时后者长n
//            if (routePathLength < realPathLength) {
//                realPathArray[routePathLength - 1] = Stream
//                        .of(realPathArray)
//                        .skip(routePathLength - 1)
//                        .reduce((join, e) -> join = join + '/' + e).get();
//                return IntStream.range(0, routePathLength)
//                        .mapToObj(i -> new RoutePathEntry(realPathArray[i], routePathArray[i]))
//                        .collect(Collectors.toList());
//            }
//            //特殊情况： /hello/* /hello/ 此时前者长1
//            else if (routePathLength == realPathLength + 1) {
//                List<RoutePathEntry> re = IntStream
//                        .range(0, realPathLength)
//                        .mapToObj(i -> new RoutePathEntry(realPathArray[i], routePathArray[i]))
//                        .collect(Collectors.toList());
//                re.add(new RoutePathEntry("", "*"));
//                return re;
//            }
//        }
//
//        throw new RuntimeException();
//
////        String[] realPathArray = realPath.split("/");
////        String[] routePathArray = routePath.split("/");
////
//////        if(true)
//////        {
//////            int length = Math.min(requestPathArray.length, routePathArray.length);
//////            final List<RoutePathEntry> re = new ArrayList<>();
//////            for (int i = 0; i < length; i++) {
//////                re.add(new RoutePathEntry(requestPathArray[i], routePathArray[i]));
//////            }
//////            return re;
//////        }
////
////        int length = Math.max(realPathArray.length, routePathArray.length);
////
////        if (routePathArray.length < length) routePathArray = Arrays.copyOf(routePathArray, length);
////        else if (realPathArray.length < length) realPathArray = Arrays.copyOf(realPathArray, length);
////
////        final List<RoutePathEntry> re = new ArrayList<>();
////        for (int i = 0; i < length; i++) {
////            re.add(new RoutePathEntry(realPathArray[i], routePathArray[i]));
////        }
////        return re;
//    }


//    public static List<String> convertRouteToList(String route) {
//        String[] pathArray = route.split("/");
//        List<String> path = new ArrayList<>(pathArray.length);
//        for (String p : pathArray) {
//            if (p.length() > 0) {
//                path.add(p);
//            }
//        }
//        return path;
//    }

//    public static boolean isParam(String routePart) {
//        return routePart.startsWith(":");
//    }

//    public static boolean notNull(String routePart) {
//        return routePart != null;
//    }

//    public static boolean isSplat(String routePart) {
//        return routePart.equals("*");
//    }
}
