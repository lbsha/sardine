package sardine.config;

import sardine.Sardine;

/**
 * @author bruce-sha
 *   2015/6/29
 */
public class Config {

    public static void initialize() {

        String host = System.getProperty("sardine.host");
        if (host != null) Sardine.host(host);

        String port = System.getProperty("sardine.port");
        if (port != null) Sardine.port(Integer.parseInt(port));

    }

}
