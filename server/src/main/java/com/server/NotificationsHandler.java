package com.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import com.fasterxml.jackson.databind.ObjectMapper;


public class NotificationsHandler implements Runnable, Serializable {
    private final ClientSocket clientSocket;
    private final PrintWriter writer;
    private final ObjectOutputStream objectOutputStream;

    public NotificationsHandler(ClientSocket clientSocket, ObjectOutputStream objectOutputStream) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new PrintWriter(clientSocket.getSocket().getOutputStream(), true);
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        int count = 1;
        ObjectMapper mapper = new ObjectMapper();
        while (clientSocket.isConnected()) {
            try {
                Object value = mapper.convertValue(new ServerData(2, "Notification "+ count++), Object.class); //TODO: Find a better way
                objectOutputStream.writeObject(value);
                objectOutputStream.reset();
                System.out.println(Thread.currentThread().getName() + " Sent = " + value);
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                shutdown();
                e.printStackTrace();
            }
        }
        shutdown();
    }

    private void shutdown() {
        writer.close();
        clientSocket.close();
        System.out.println("Notifications' Handler thread shutdown for client = " + clientSocket.getID());
    }
}
