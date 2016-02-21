package sardine.route;

import sardine.HttpMethod;
import sardine.SardineBase;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @auth bruce-sha
 * @date 2015/5/21
 */
class RouteEntry<T> {

    final HttpMethod method;
    final String path;
    final String accept;
    final T target;

    // 缓存
    final private String[] pathSplitsCache;

    public RouteEntry(final HttpMethod method, final String path, final String accept, final T target) {

        this.method = Objects.requireNonNull(method);
        this.path = Objects.requireNonNull(path);
        this.accept = Objects.requireNonNull(accept);
        this.target = Objects.requireNonNull(target);

        this.pathSplitsCache = this.path.split("/");
    }


    boolean match(final HttpMethod requestMethod, final String requestPath) {

        if ((HttpMethod.BEFORE == requestMethod || HttpMethod.AFTER == requestMethod)
                && (method == requestMethod)
                && (SardineBase.ALL_PATHS.equals(path)))
            return true;

        if (method == requestMethod) return isMatch(requestPath);
        else return false;

//        if (method != requestMethod) return false;
//        else return isMatch(requestPath);
    }


    /**
     * 判断是否匹配
     * <p/>
     * 约束：两个长度必须相等，只有两个特殊情况
     * 特殊情况： /hello/* /hello/ 此时前者长1
     * 特殊情况： /hello/* /hello/bruce/sha 此时后者长n
     */
    private boolean isMatch(final String realPath) {

        final String routePath = path;

        // 完全相等
        if (routePath.equals(realPath)) return true;

        // /结尾需要谨慎 /hello/应该匹配 /hello/*或者/hello/或者 /hello/:name 而不是/hello
        // 但是/hello/与/hello split出来是完全一样的{"","hello"}
        if (realPath.endsWith("/") && !(routePath.endsWith("/") || routePath.endsWith("*")))
//                || pathSplitsCache[Math.max(pathSplitsCache.length - 1, 0)].startsWith(":")))
            return false;

        final String[] routePathArray = routePath.split("/");
        final String[] realPathArray = realPath.split("/");

        int routePathLength = routePathArray.length;
        int realPathLength = realPathArray.length;

        //如果不等最后一位必须是*
        if (routePathLength != realPathLength) {
            if (!routePath.endsWith("*")) return false;
            if (routePathLength > realPathLength + 1) return false;
        }

        return !IntStream.range(0, Math.min(routePathLength, realPathLength))
                .parallel()
                .filter(i -> !routePathArray[i].startsWith(":"))
                .filter(i -> !routePathArray[i].equals("*"))
                .anyMatch(i -> !routePathArray[i].equals(realPathArray[i]));
    }

    @Override
    public String toString() {
        return accept + " " + method + " " + path;
    }
}