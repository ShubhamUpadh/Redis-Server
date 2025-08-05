package org.redis;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkLayer {
    private final int port;
    private ServerSocket serverSocket;
    private static final Logger logger = Logger.getLogger(NetworkLayer.class.getName());
    public NetworkLayer(int port = 1234){
        this.port = port;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(port);
        logger.log(Level.INFO ,"Server is up and listening on port " + port);

        while (true){
            Socket clientSocket = null;
            try{
                clientSocket = serverSocket.accept();
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

    private void handleClient(Socket clientSocket) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        StringBuilder serializedString = new StringBuilder();
        while ((bytesRead = bufferedInputStream.read(buffer)) != -1){
            String received = new String(buffer,0 , bytesRead);
            serializedString.append(received);
        }
        Object deserializedObject = RespParser.deserialize(serializedString.toString());
        System.out.println(deserializedObject);



    }


}
