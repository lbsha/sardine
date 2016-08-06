package sardine;

/**
 * @author bruce-sha
 *   2015/5/21
 */
public interface ResponseTransformer<T> {

    //TODO:是不是应该返回bytes？
//    String render(Object model) throws Exception;
    String render(T model) throws Exception;

    //TODO
    default String contentType() {
        return SardineBase.DEFAULT_ACCEPT_TYPE;
    }

    /**
     *
     */
    public abstract class SardineResponseTransformerRoute<T> extends Route.SardineRoute<T> {

        protected SardineResponseTransformerRoute(final String path, final String accept, final Condition condition) {
            super(path, accept, condition);
        }

        public abstract String render(T model) throws Exception;


        public static <T> SardineResponseTransformerRoute<T> create(String path,
                                                                    Condition condition,
                                                                    Route<T> route,
                                                                    ResponseTransformer<T> transformer) {
            return create(path, SardineBase.DEFAULT_ACCEPT_TYPE, condition, route, transformer);
        }

        public static <T> SardineResponseTransformerRoute<T> create(String path,
                                                                    String acceptType,
                                                                    Condition condition,
                                                                    Route<T> route,
                                                                    ResponseTransformer<T> transformer) {
            return new SardineResponseTransformerRoute<T>(path, acceptType, condition) {

                @Override
                public T apply(Request request, Response response) throws Exception {
                    return route.apply(request, response);
                }

                @Override
                public String render(T model) throws Exception {
                    return transformer.render(model);
                }
            };
        }
    }
}
