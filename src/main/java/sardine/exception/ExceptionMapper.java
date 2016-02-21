package sardine.exception;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auth bruce-sha
 * @date 2015/5/21
 */
public class ExceptionMapper {

    static final Map<Class<? extends Exception>, ExceptionHandler> mapper = new ConcurrentHashMap<>();

    public static void map(final Class<? extends Exception> exceptionClass, final ExceptionHandler handler) {
        mapper.put(Objects.requireNonNull(exceptionClass), Objects.requireNonNull(handler));
    }

    public static Optional<ExceptionHandler> handler(final Exception exception) {
        return handler(Objects.requireNonNull(exception).getClass());
    }

    public static Optional<ExceptionHandler> handler(final Class<? extends Exception> exceptionClass) {

        if (!mapper.containsKey(exceptionClass)) {

            Class<?> superclass = exceptionClass.getSuperclass();
            do {
                if (mapper.containsKey(superclass)) {
                    ExceptionHandler handler = mapper.get(superclass);
                    mapper.put(exceptionClass, handler);

                    return Optional.ofNullable(handler);
                }

                superclass = superclass.getSuperclass();
            } while (superclass != null);

            mapper.put(exceptionClass, null);
            return Optional.empty();
        }

        return Optional.ofNullable(mapper.get(exceptionClass));
    }
}
