package sardine.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sardine.SardineBase;

import java.time.LocalTime;
import java.util.Objects;

/**
 * LOG 工具
 *
 * @author bruce-sha
 *         2015/5/21
 * @since 1.0.0
 */
public abstract class Logs {

    static final private Logger LOG = LoggerFactory.getLogger(SardineBase.application());

    public static void console(final Recordable recordable) {
        System.out.println(LocalTime.now() + " console - " + recordable.asString());
    }

    public static void mustLog(final Recordable recordable) {
        if (LOG.isDebugEnabled()) LOG.debug(recordable.asString());
        else if (LOG.isInfoEnabled()) LOG.info(recordable.asString());
        else if (LOG.isWarnEnabled()) LOG.warn(recordable.asString());
        else if (LOG.isErrorEnabled()) LOG.error(recordable.asString());
        else if (LOG.isTraceEnabled()) LOG.trace(recordable.asString());
        else console(recordable);
    }

    public static void log(Recordable recordable) {
        if (LOG.isInfoEnabled()) LOG.info(recordable.asString());
    }

    public static void log(String messageTemplate, Object... arguments) {
        if (LOG.isInfoEnabled()) LOG.info(messageTemplate, arguments);
    }

    public static void info(Recordable recordable) {
        if (LOG.isInfoEnabled()) LOG.info(recordable.asString());
    }

    public static void info(String messageTemplate, Object... arguments) {
        if (LOG.isInfoEnabled()) LOG.info(messageTemplate, arguments);
    }

    public static void warn(Recordable recordable) {
        if (LOG.isWarnEnabled()) LOG.warn(recordable.asString());
    }

    public static void warn(String messageTemplate, Object... arguments) {
        if (LOG.isWarnEnabled()) LOG.warn(messageTemplate, arguments);
    }

    public static void error(Recordable recordable) {
        if (LOG.isErrorEnabled()) LOG.error(recordable.asString());
    }

    public static void error(String messageTemplate, Object... arguments) {
        if (LOG.isErrorEnabled()) LOG.error(messageTemplate, arguments);
    }

    public static void error(String message, Throwable t) {
        if (LOG.isErrorEnabled()) LOG.error(message, t);
    }

    public static void error(Throwable t) {
        if (LOG.isErrorEnabled()) LOG.error(t.getMessage(), t);
    }

    public static void debug(Recordable recordable) {
        if (LOG.isDebugEnabled()) LOG.debug(recordable.asString());
    }

    public static void debug(String messageTemplate, Object... arguments) {
        if (LOG.isDebugEnabled()) LOG.debug(messageTemplate, arguments);
    }


    /**
     * 优点：不需要预先计算字符串
     */
    @FunctionalInterface
    public interface Recordable {

        Object record();

        default String asString() {
            return Objects.toString(record(), "");
        }

//    @FunctionalInterface
//    public static interface TemplateConsole {
//        Object record(Object... args);
//
//        default String asString() {
//            return Objects.toString(record(), "");
//        }
//    }
    }

}