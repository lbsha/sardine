package sardine.mvc;

import java.util.Map;

/**
 * @author bruce-sha
 *   2015/6/15
 */
public abstract class TemplateEngine {

    /**
     * Renders the object
     *
     *   object the object
     * @return the rendered model and view
     */
    public String render(Object object) {
        ModelAndView modelAndView = (ModelAndView) object;
        return render(modelAndView);
    }

    /**
     * Creates a new ModelAndView object with given arguments.
     *
     *   model    object.
     *   viewName to be rendered.
     * @return object with model and view set.
     */
    public ModelAndView modelAndView(Map<String, Object> model, String viewName) {
        return new ModelAndView(model, viewName);
    }

    /**
     * Method called to render the output that is sent to client.
     *
     *   modelAndView object where object (mostly a POJO) and the name of the view to render are set.
     * @return message that it is sent to client.
     */
    public abstract String render(ModelAndView modelAndView);

}
