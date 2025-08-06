package org.redis;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkLayer {
    private final int port;
    private static final Logger logger = Logger.getLogger(NetworkLayer.class.getName());

    // ✅ Shared dispatcher across all clients
    private static final DispatcherLayer dispatcher = new DispatcherLayer();

    public NetworkLayer(int port) {
        this.port = port;
    }

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        logger.log(Level.INFO, "Server is up and listening on port " + port);

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "Client connected: " + clientSocket.getInetAddress());

                new Thread(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error handling client: "
                                + clientSocket.getInetAddress() + " - " + e.getMessage());
                    }
                }).start();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Server socket accept failed", e);
                throw new RuntimeException(e);
            }
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {
        try (
                InputStream inputStream = clientSocket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                OutputStream outputStream = clientSocket.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            StringBuilder serializedString = new StringBuilder();

            // Read from client
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                String received = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                serializedString.append(received);

                // Optional: break on specific protocol ending (e.g., \r\n or bulk size)
                // For now, assuming single request per connection
                break;
            }

            Object deserializedObject = RespParser.deserialize(serializedString.toString());
            logger.info("Received: " + deserializedObject);

            assert deserializedObject instanceof List;
            Optional<Request> requestOptional = Request.fromInput((List<String>) deserializedObject);

            Response response;
            if (requestOptional.isEmpty()) {
                response = new Response("Invalid request", true);
            } else {
                response = dispatcher.handleMessage(requestOptional.get());
            }

            // Send response back
            handleResponse(outputStream, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in handleClient", e);
        } finally {
            clientSocket.close(); // Ensure socket is closed
        }
    }

    private void handleResponse(OutputStream outputStream, Response response) throws IOException {
        String reply = response.isFailed() ? "Error: " + response.getResponse() : response.getResponse();
        String serializedReply = RespParser.serialize(reply);

        // Send serialized string back to client
        outputStream.write(serializedReply.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
}
