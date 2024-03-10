package org.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.httpserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager myConfigurationManager;
    public static Configuration myCurrentConfiguration;

    public ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (myConfigurationManager == null)
            myConfigurationManager = new ConfigurationManager();
        return myConfigurationManager;
    }

    /**
     * Used to load configuration file from the path
    */
    public void loadConfigurationFile(String filePath) throws IOException {
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException error) {
            throw new HttpConfigurationException(error);
        }

        StringBuffer sbuffer = new StringBuffer();

        int i;
        try {
            while ((i = fileReader.read()) != -1) {
                sbuffer.append((char) i);
            }
        } catch (IOException error) {
            throw new HttpConfigurationException(error);
        }

        JsonNode config = null;
        try {
            config = Json.parse(sbuffer.toString());
        } catch (IOException error) {
            throw new HttpConfigurationException("Error while parsing config file", error);
        }

        try {
            myCurrentConfiguration = Json.fromJson(config, Configuration.class);
        } catch (JsonProcessingException error) {
            throw new HttpConfigurationException("Error while parsing current configuration file", error);
        }
    }

    /**
     * Return current configuration
     *
     * @return myCurrentConfiguration
     */
    public Configuration getCurrentConfiguration() {
        if (myCurrentConfiguration == null) {
            throw new HttpConfigurationException("There is no current configuration set.");
        }

        return myCurrentConfiguration;
    }
}
