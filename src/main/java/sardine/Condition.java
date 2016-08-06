package sardine;

/**
 * 条件
 *
 * @author bruce-sha
 */
@FunctionalInterface
public interface Condition {

    boolean apply(final Request request) throws Exception;

    /**
     * 空参 条件
     */
    @FunctionalInterface
    /*public static*/ interface VCondition {
        boolean apply() throws Exception;
    }
}
