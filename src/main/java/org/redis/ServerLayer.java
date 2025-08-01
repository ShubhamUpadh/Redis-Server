package org.redis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerLayer {
    private int port;
    private static final Logger logger = Logger.getLogger(ServerLayer.class.getName());
    public ServerLayer(int port = 1234){
        this.port = port;
    }

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        logger.log(Level.INFO ,"Server is up and listening on port " + port);
        Socket clientSocket = serverSocket.accept();
        logger.log(Level.INFO, "Client connected");
    }
}
