package sardine;

/**
 * 条件
 *
 * @auth bruce-sha
 * @date 2015/5/21
 */
@FunctionalInterface
public interface Condition {

    boolean apply(Request request) throws Exception;

    /**
     * 空参 条件
     */
    @FunctionalInterface
    /*public static*/ interface VCondition {
        boolean apply() throws Exception;
    }
}
