package com.client.service;

import com.client.model.Notification;
import com.client.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.util.List;

@Service
public class ClientService implements Runnable {
    @Autowired
    NotificationRepository notificationRepository;
    private static Socket client;

    public void connect() {
        try {
            client = new Socket("localhost" ,8000);
            System.out.println("Connected to Server...");
//            while (client.isConnected()){
//                try {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                    String data = reader.readLine();
//                    System.out.println("Data from server: "+data);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        while (client != null && client.isConnected()){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String data = reader.readLine();
                Notification notification = new Notification();
                notification.setMessage(data);
                notificationRepository.save(notification);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String send(String data) throws IOException {
        PrintWriter printWriter = new PrintWriter(client.getOutputStream());
        printWriter.println(data);
        printWriter.flush();
        return "Data sent to Server: " + data;
    }
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        connect();
        receiveData();
    }
}
