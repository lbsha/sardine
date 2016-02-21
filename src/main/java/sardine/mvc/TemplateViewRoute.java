package sardine.mvc;


import sardine.Request;
import sardine.Response;

/**
 * @auth bruce-sha
 * @date 2015/5/21
 */
public interface TemplateViewRoute {

    ModelAndView apply(Request request, Response response);

}
