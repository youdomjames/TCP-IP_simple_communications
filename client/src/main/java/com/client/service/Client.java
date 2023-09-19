package com.client.service;

import com.client.model.Notification;
import com.client.model.ServerResponse;
import com.client.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

@Service
public class Client implements Runnable {
    private final Socket client;
    private final ServerResponse response;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final NotificationRepository notificationRepository;

    @Autowired
    public Client(NotificationRepository notificationRepository) throws IOException {
        client = new Socket("localhost", 8000);
        writer = new PrintWriter(client.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        response = new ServerResponse();
        this.notificationRepository = notificationRepository;
        if (client.isConnected()) {
            System.out.println("Connected to Server...");
        }
    }

    public void receiveData() {
        try {
            String data;
            while ((data = reader.readLine()) != null) {
                System.out.println("From server : " + data);
                if (data.startsWith("RESPONSE_")) {
                    response.set(data.split("_")[1]);
                    synchronized (response) {
                        response.notifyAll();
                    }
                } else {
                    saveAsNotification(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    private void saveAsNotification(String data) {
        Notification notification = new Notification();
        notification.setMessage(data);
        notificationRepository.save(notification);
    }

    public String send(String data) throws InterruptedException {
        String key = "REQUEST_";
        writer.println(key.concat(data));
        synchronized (response) {
            response.wait();
            return "Server RESPONSE = " + response.get();
        }
    }

    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    public void close() {
        try {
            System.out.println("Disconnected!!!");
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        receiveData();
    }
}
