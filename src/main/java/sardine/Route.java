package sardine;

import java.util.Objects;
import java.util.Optional;

/**
 * 路由器
 *
 * @auth bruce-sha
 * @date 2015/5/21
 */
@FunctionalInterface
public interface Route<T> {

    T apply(Request request, Response response) throws Exception;

    /**
     * 无参路由
     */
    @FunctionalInterface
    /*public static */ interface VRoute<T> {
        T apply() throws Exception;
    }

    /**
     * 简单路由
     */
    /*public*/ abstract class SardineRoute<T> implements Route<T> {

        final private String path;
        final private String accept;
        final Optional<Condition> condition;

        protected SardineRoute(final String path, final String accept, final Condition condition) {
            this.path = Objects.requireNonNull(path);
            this.accept = Objects.requireNonNull(accept);
            this.condition = Optional.ofNullable(condition);
        }

        public String getAccept() {
            return accept;
        }

        public String getPath() {
            return path;
        }

        public boolean condition(Request request) throws Exception {
            return !condition.isPresent() || condition.get().apply(request);
        }

        public abstract T apply(Request request, Response response) throws Exception;

        public String render(T element) throws Exception {
            return Objects.toString(element, SardineBase.EMPTY);
        }
    }
}
