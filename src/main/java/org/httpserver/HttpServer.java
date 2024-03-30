package org.httpserver;

import org.httpserver.config.Configuration;
import org.httpserver.config.ConfigurationManager;
import org.httpserver.core.ServerListenterThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class for Http server
*/
public class HttpServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        LOGGER.info(" * Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info(" * Configuration port " + config.getPort());
        LOGGER.info(" * Configuration webroot " + config.getWebroot());

        try {
            ServerListenterThread serverListenterThread = new ServerListenterThread(config.getPort(), config.getWebroot());
            serverListenterThread.start();
        } catch (IOException error) {
            error.printStackTrace();
            // TODO implement handler later
        }
    }
}
