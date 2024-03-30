package org.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenterThread.class);
    private final Socket socket;
    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            final String CRLF = "\r\n";
            String html = "<html><head><title>Simple Java HTTP server</title></head><body><h1>This page was created by my simple Java HTTP server</h1></body></html>";
            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length" + html.getBytes().length + CRLF + CRLF + html + CRLF + CRLF;

            outputStream.write(response.getBytes());

            // TODO need to implement reading
            // TODO need to implement writing

            inputStream.close();
            outputStream.close();
            socket.close();

            LOGGER.info(" * Connection processing finished.");
        } catch (IOException error) {
            error.printStackTrace();
            LOGGER.error("Issue with communication", error);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException error) {}
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException error) {}
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException error) {}
            }
        }
    }
}
