package sardine.mvc;

import sardine.Condition;
import sardine.Request;
import sardine.Response;
import sardine.Route.SardineRoute;
import sardine.SardineBase;

import java.util.Map;

/**
 * @auth bruce-sha
 * @date 2015/5/21
 */
public abstract class SardineTemplateViewRoute extends SardineRoute {

    protected SardineTemplateViewRoute(final String path, final String accept, final Condition condition) {
        super(path, accept, condition);
    }

    @Override
    public String render(final Object object) {
        ModelAndView modelAndView = (ModelAndView) object;
        return render(modelAndView);
    }

    public ModelAndView modelAndView(final Map<String, Object> model, final String viewName) {
        return new ModelAndView(model, viewName);
    }

    public abstract String render(final ModelAndView modelAndView);


    public static SardineTemplateViewRoute create(final String path,
                                                  final Condition condition,
                                                  final TemplateViewRoute route,
                                                  final TemplateEngine engine) {
        return create(path, SardineBase.DEFAULT_ACCEPT_TYPE, condition, route, engine);
    }

    public static SardineTemplateViewRoute create(final String path,
                                                  final String acceptType,
                                                  final Condition condition,
                                                  final TemplateViewRoute route,
                                                  final TemplateEngine engine) {
        return new SardineTemplateViewRoute(path, acceptType, condition) {

            @Override
            public Object apply(Request request, Response response) throws Exception {
                return route.apply(request, response);
            }

            @Override
            public String render(ModelAndView modelAndView) {
                return engine.render(modelAndView);
            }
        };
    }


}