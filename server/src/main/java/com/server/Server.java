package com.server;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Service
public class Server {
    private final ServerSocket serverSocket;
    private final Executor executor;

    public Server() throws IOException {
        serverSocket = new ServerSocket(8000);

        executor = Executors.newCachedThreadPool();
        System.out.println("Waiting for clients...");
    }

    public void start(){
        while (!serverSocket.isClosed()){
            try {
                Socket client = serverSocket.accept();
                if (client.isConnected()) {
                    ClientSocket clientSocket = new ClientSocket(generateClientID(), client);
                    System.out.println("Client No: " + clientSocket.getID() + " IS CONNECTED!!!");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getSocket().getOutputStream());
                    executor.execute(new NotificationsHandler(clientSocket, objectOutputStream));
                    executor.execute(new RequestsHandler(clientSocket, objectOutputStream));
                }
            } catch (IOException e) {
                shutdown();
                e.printStackTrace();
            }
        }
    }

    private void shutdown() {
        try {
            serverSocket.close();
            System.out.println("Server SHUTDOWN!!!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateClientID () {
        return "CLIENT_".concat(UUID.randomUUID().toString());
    }
}
