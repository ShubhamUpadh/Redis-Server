package org.redis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerLayer {
    private final int port;
    private ServerSocket serverSocket;
    private static final Logger logger = Logger.getLogger(ServerLayer.class.getName());
    public ServerLayer(int port = 1234){
        this.port = port;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(port);
        logger.log(Level.INFO ,"Server is up and listening on port " + port);

        while (true){
            try{
                Socket clientSocket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.log(Level.INFO, "Client connected" + clientSocket.getInetAddress());
            new Thread(() ->{
                try{
                    handleClient(clientSocket);
                }
                catch (Exception e){
                    logger.log(Level.WARNING, "Not able to create thread for " +
                            clientSocket.getInetAddress() + "\t" + e.getMessage());
                }
            }).start();
        }
    }


}
