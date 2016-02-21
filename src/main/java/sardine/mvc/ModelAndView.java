package sardine.mvc;

import java.util.HashMap;
import java.util.Map;

/**
 * @auth bruce-sha
 * @date 2015/6/15
 */
public class ModelAndView {

    /**
     * Model object.
     */
    private Map<String, Object> model;
    /**
     * View name used to render output.
     */
    private String viewName;

    /**
     * Constructs an instance with the provided model and view name
     *
     * @param model    the model
     * @param viewName the view name
     */
    public ModelAndView(Map<String, Object> model, String viewName) {
        this.model = model;
        this.viewName = viewName;
    }

    /**
     * @return the model object
     */
    public Map<String, ?> getModel() {
        return model;
    }

    /**
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    public static ModelAndView view(String viewName) {
        return new ModelAndView(new HashMap<String, Object>(), viewName);
    }

    public ModelAndView model(String modelName, Object modelObject) {
        if (model == null) model = new HashMap<String, Object>();
        model.put(modelName, modelObject);
        return this;
    }

    public ModelAndView models(Map<String, Object> anModel) {
        if (model == null) model = new HashMap<String, Object>();
        model.putAll(anModel);
        return this;
    }
}
