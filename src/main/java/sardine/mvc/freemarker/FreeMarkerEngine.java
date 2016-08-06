package sardine.mvc.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sardine.mvc.ModelAndView;
import sardine.mvc.TemplateEngine;

import java.io.IOException;
import java.io.StringWriter;
//import sardine.ModelAndView;
//import sardine.TemplateEngine;

/**
 * Renders HTML from Route output using FreeMarker.
 * FreeMarker configuration can be set with the {@link FreeMarkerEngine#setConfiguration(Configuration)}
 * method. If no configuration is set the default configuration will be used where
 * ftl files need to be PUT in directory sardine/template/sardine.template.freemarker under the resources directory.
 */
public class FreeMarkerEngine extends TemplateEngine {

    /**
     * The FreeMarker configuration
     */
    private Configuration configuration;

    /**
     * Creates a FreeMarkerEngine
     */
    public FreeMarkerEngine() {
        this.configuration = createDefaultConfiguration();
    }

    /**
     * Creates a FreeMarkerEngine with a configuration
     *
     *   configuration The Freemarker configuration
     */
    public FreeMarkerEngine(Configuration configuration) {
        this.configuration = configuration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String render(ModelAndView modelAndView) {
        try {
            StringWriter stringWriter = new StringWriter();

            Template template = configuration.getTemplate(modelAndView.getViewName());
            template.process(modelAndView.getModel(), stringWriter);

            return stringWriter.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sets FreeMarker configuration.
     * Note: If configuration is not set the default configuration
     * will be used.
     *
     *   configuration the configuration to set
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private Configuration createDefaultConfiguration() {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setClassForTemplateLoading(FreeMarkerEngine.class, "");// FreeMarkerEngine所在目录
//        configuration.setClassForTemplateLoading(FreeMarkerEngine.class, "/");
        return configuration;
    }

}
