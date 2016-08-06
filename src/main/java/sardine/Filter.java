package sardine;

import java.util.Objects;

/**
 * before / after filter
 *
 * @author bruce-sha
 */
@FunctionalInterface
public interface Filter {

    void apply(Request request, Response response) throws Exception;

    /**
     *
     */
    @FunctionalInterface
    /*public static */ interface VFilter {
        void apply() throws Exception;
    }

    /**
     *
     */
    /*public*/ abstract class SardineFilter implements Filter {

        final private String path;
        final private String accept;

        SardineFilter(final String path, final String accept) {
            this.path = Objects.requireNonNull(path);
            this.accept = Objects.requireNonNull(accept);
        }

        public String getAccept() {
            return accept;
        }

        public String getPath() {
            return path;
        }

        public abstract void apply(Request request, Response response) throws Exception;
    }
}
