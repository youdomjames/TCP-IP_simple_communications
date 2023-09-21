package com.client.service;

import com.client.model.Notification;
import com.client.model.ServerData;
import com.client.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class Client implements Runnable {
    private  Socket client;
    private  PrintWriter writer;
    private final ServerData response;
    private final NotificationRepository notificationRepository;

    @Autowired
    public Client(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
        response = new ServerData();
    }

    public void start() {
        try {
            client = new Socket("localhost", 8000);
            writer = new PrintWriter(client.getOutputStream(), true);
            IncomingMessageHandler messageHandler = new IncomingMessageHandler(client, notificationRepository);
            Thread thread = new Thread(messageHandler);
            thread.start();
            if (client.isConnected()) {
                System.out.println("Connected to Server...");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void receiveData() {

    }



    public String send(String data) throws InterruptedException {
        String key = "REQUEST_";
        writer.println(key.concat(data));
        synchronized (response) {
            response.wait();
            return "Server RESPONSE = " + response;
        }
    }

    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }


    @Override
    public void run() {
        receiveData();
    }
}

//Get a message handler