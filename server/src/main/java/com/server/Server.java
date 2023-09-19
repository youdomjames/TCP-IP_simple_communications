package com.server;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


@Service
public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private final Socket client;
    private final PrintWriter writer;
    private final BufferedReader reader;

    public Server() throws IOException {
        serverSocket = new ServerSocket(8000);
        System.out.println("Waiting for clients");
        client = serverSocket.accept();
        writer = new PrintWriter(client.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        if (client.isConnected()) {
            System.out.println("New client connected");
        }
    }

    public void sendNotifications() throws InterruptedException {
        int count = 1;
        while (client.isConnected()) {
            String notification = "Notification-" + count++;
            writer.println(notification);
            System.out.println("Sent = " + notification);
            Thread.sleep(5000);
        }
    }

    public void receiveRequests() {
        try {
            String data;
            while((data = reader.readLine()) != null){
                System.out.println("Data from Client: " + data);
                if (data.startsWith("REQUEST_")) {
                    writer.println("RESPONSE_Hello Client");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutDown() {
       try {
           writer.close();
           reader.close();
           serverSocket.close();
           System.out.println("Server Shutdown!!!");
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    @Override
    public void run() {
        try {
            sendNotifications();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
