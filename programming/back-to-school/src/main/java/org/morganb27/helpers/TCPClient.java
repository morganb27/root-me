package org.morganb27.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient {
    private static final Logger logger = Logger.getLogger(TCPClient.class.getName());

    final private String host;
    final private int port;
    private Socket socket;

    private PrintWriter writer;

    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            logger.info("Connected to " + host + ":" + port);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to connect to server", e);
        }
    }

    public void sendMessage(String message) {
        if (writer != null) {
            float floatMessage = Float.parseFloat(message);
            writer.println(floatMessage);
            logger.info("Sent message: " + message);
        } else {
            logger.warning("Cannot send message. Writer is not initialized.");
        }
    }

    public String readMessage() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            if (bytesRead > 0) {
                return new String(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            logger.severe("Error reading data from server: " + e.getMessage());
        }
        return null;
    }



    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            logger.info("Connection closed");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to close resources", e);
        }
    }

}
