package org.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenterThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenterThread.class);

    private int port;
    private String webroot;
    private final ServerSocket serverSocket;

    public ServerListenterThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
    }
    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                LOGGER.info(" * Connection is accepted: " + socket.getInetAddress());

                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();
            }
        } catch (IOException error) {
            error.printStackTrace();
            LOGGER.error("Issue with setting socket", error);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException error) {}
            }
        }
    }
}
