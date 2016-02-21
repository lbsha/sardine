package sardine;

/**
 * 沙丁异常
 *
 * @auth bruce-sha
 * @date 2015/6/17
 */
public class SardineException extends RuntimeException {

    public SardineException() {
    }

    public SardineException(String message) {
        super(message);
    }

    public SardineException(Throwable cause) {
        super(cause);
    }

    public SardineException(String message, Throwable cause) {
        super(message, cause);
    }

    public SardineException(String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
