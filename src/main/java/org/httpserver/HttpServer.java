package org.httpserver;

import org.httpserver.config.Configuration;
import org.httpserver.config.ConfigurationManager;

import java.io.IOException;

/**
 * Class for Http server
*/
public class HttpServer {

    public static void main(String[] args) throws IOException {
        System.out.println("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

        System.out.println("Configuration port " + config.getPort());
        System.out.println("Configuration webroot " + config.getWebroot());
    }
}
