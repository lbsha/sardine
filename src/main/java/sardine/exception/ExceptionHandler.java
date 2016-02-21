package sardine.exception;

import sardine.Request;
import sardine.Response;

/**
 * 异常憨豆
 *
 * @auth bruce-sha
 * @date 2015/5/21
 */
@FunctionalInterface
public interface ExceptionHandler {

    void apply(Exception exception, Request request, Response response);

    /**
     *
     */
    public static abstract class SardineExceptionHandler implements ExceptionHandler {

        final protected Class<? extends Exception> exceptionClass;

        public SardineExceptionHandler(Class<? extends Exception> exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        public abstract void apply(Exception exception, Request request, Response response);
    }
}
