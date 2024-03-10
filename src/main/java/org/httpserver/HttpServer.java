package org.httpserver;

import org.httpserver.config.Configuration;
import org.httpserver.config.ConfigurationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

        try {
            ServerSocket serverSocket = new ServerSocket(config.getPort());
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            final String CRLF = "\n\r";
            String html = "<html><head><title>Simple Java HTTP server</title></head><body><h1>This page was created by my simple Java HTTP server</h1></body></html>";
            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length" + html.getBytes().length + CRLF + CRLF + html + CRLF + CRLF;

            outputStream.write(response.getBytes());

            // TODO need to implement reading
            // TODO need to implement writing

            inputStream.close();
            outputStream.close();
            socket.close();
            serverSocket.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
