package sardine.monitor;

import io.netty.handler.codec.http.HttpResponseStatus;
import sardine.log.Logs;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.*;

/**
 * 监控是微服务必不可少的特性
 *
 * @auth bruce-sha
 * @date 2015/5/22
 */
public class Metrics {

    static final LocalDateTime startTime = LocalDateTime.now(); // 开始时间

    static final AtomicLong totalRequestCount = new AtomicLong();  // 总请求数，包含静态文件
    static final AtomicLong totalResponseTime = new AtomicLong();  // 总请求时间，纳秒，包含静态文件

    static final AtomicLong responseBytesWithOutAsserts = new AtomicLong(); // 返回字节数，不包含静态文件

    static final AtomicLong assertsRequestCount = new AtomicLong(); // 静态文件请求数
    static final AtomicLong assertsResponseBytes = new AtomicLong(); // 静态文件返回字节数

    static final AtomicLong response1xxCount = new AtomicLong();  // 1xx返回码统计
    static final AtomicLong response2xxCount = new AtomicLong();  // 2xx返回码统计
    static final AtomicLong response3xxCount = new AtomicLong();  // 3xx返回码统计
    static final AtomicLong response4xxCount = new AtomicLong();  // 4xx返回码统计
    static final AtomicLong response5xxCount = new AtomicLong();  // 5xx返回码统计

    public static String stats() {

        final StringBuilder sb = new StringBuilder();

        sb.append("sardine start time: ").append(startTime);

        sb.append("total request count: ").append(totalRequestCount);
        sb.append("total response time: ").append(totalResponseTime.longValue() / 100_0000L).append("ms");
        sb.append("response bytes: ").append(responseBytesWithOutAsserts).append(" (with out asserts)");
        sb.append("response 1xx count: ").append(response1xxCount);
        sb.append("response 2xx count: ").append(response2xxCount);
        sb.append("response 3xx count: ").append(response3xxCount);
        sb.append("response 4xx count: ").append(response4xxCount);
        sb.append("response 5xx count: ").append(response5xxCount);

        sb.append("asserts request count: ").append(assertsRequestCount);
        sb.append("asserts response bytes: ").append(assertsResponseBytes);

        return sb.toString();
    }

    public static String alive() {
        return LocalDateTime.now().toString();
    }

    public static long totalRequestCount() {
        long c = totalRequestCount.incrementAndGet();
        Logs.debug(() -> "sardine total request count: " + c);
        return c;
    }

    public static long totalResponseTime(long time) {
        long t = totalResponseTime.addAndGet(time);
//        Logs.debug(() -> "sardine total request time: " + t + " ms.");

        Logs.debug(() -> {

            TimeUnit unit = chooseUnit(t);
            double value = (double) t / NANOSECONDS.convert(1, unit);

            // Too bad this functionality is not exposed as a regular method call
            return String.format("%s ms (%.4g %s)", t / 1000000, value, abbreviate(unit));

//            return "sardine total request time: " + t + " ms.";
        });


        return t;
    }

    private static TimeUnit chooseUnit(final long nanos) {
        if (DAYS.convert(nanos, NANOSECONDS) > 0) return DAYS;
        if (HOURS.convert(nanos, NANOSECONDS) > 0) return HOURS;
        if (MINUTES.convert(nanos, NANOSECONDS) > 0) return MINUTES;
        if (SECONDS.convert(nanos, NANOSECONDS) > 0) return SECONDS;
        if (MILLISECONDS.convert(nanos, NANOSECONDS) > 0) return MILLISECONDS;
        if (MICROSECONDS.convert(nanos, NANOSECONDS) > 0) return MICROSECONDS;
        return NANOSECONDS;
    }

    private static String abbreviate(final TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS:
                return "ns";
            case MICROSECONDS:
                return "\u03bcs"; // μs
            case MILLISECONDS:
                return "ms";
            case SECONDS:
                return "s";
            case MINUTES:
                return "min";
            case HOURS:
                return "h";
            case DAYS:
                return "d";
            default:
                throw new AssertionError();
        }
    }

    public static long assertsRequestCount() {
        return assertsRequestCount.incrementAndGet();
    }

    // 注意：这里不包括静态文件的数据
    public static long responseBytesWithOutAsserts(long bytes) {
        long b = responseBytesWithOutAsserts.addAndGet(bytes);
        Logs.debug(() -> "sardine total response bytes: " + b + " (without asserts)");
        assertsResponseBytes(bytes);
        return b;
    }

    public static long assertsResponseBytes(long bytes) {
        long b = assertsResponseBytes.addAndGet(bytes);
        Logs.debug(() -> "sardine total response bytes: " + b + " (with asserts)");
        return b;
    }

    public static void responseXxxCount(final HttpResponseStatus status) {

        final int statusCode = status.code();

        switch (statusCode / 100) {
            case 1:
                long c = response1xxCount.incrementAndGet();
                Logs.debug(() -> "sardine 1xx status code count: " + c);
                break;
            case 2:
                c = response2xxCount.incrementAndGet();
                Logs.debug(() -> "sardine 2xx status code count: " + c);
                break;
            case 3:
                c = response3xxCount.incrementAndGet();
                Logs.debug(() -> "sardine 3xx status code count: " + c);
                break;
            case 4:
                c = response4xxCount.incrementAndGet();
                Logs.debug(() -> "sardine 4xx status code count: " + c);
                break;
            case 5:
                c = response5xxCount.incrementAndGet();
                Logs.debug(() -> "sardine 5xx status code count: " + c);
                break;
            default:
                Logs.debug(() -> "sardine unknown status code: " + statusCode);
                break;
        }
    }

//    @FunctionalInterface
//    public interface Recordable {
//
//        Object record();
//
//        default String asString() {
//            return Objects.toString(record(), "");
//        }
//
//    }
}
