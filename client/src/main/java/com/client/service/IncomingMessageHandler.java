package com.client.service;

import com.client.model.Notification;
import com.client.model.ServerData;
import com.client.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.catalina.Server;
import org.aspectj.weaver.ast.Not;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class IncomingMessageHandler implements Runnable, Serializable {

    private final Socket client;
    private final ServerData response;
    private final ObjectInputStream reader;
    private final NotificationRepository notificationRepository;

    public IncomingMessageHandler(Socket client, NotificationRepository notificationRepository) throws IOException {
        this.client = client;
        response = new ServerData();
        this.notificationRepository = notificationRepository;
        this.reader = new ObjectInputStream(client.getInputStream());
    }


    @Override
    public void run() {
        try {
            Object data;
            ObjectMapper mapper = new JsonMapper();
            while ((data = reader.readObject()) != null) {
                System.out.println("FROM SERVER =" + data);
                ServerData serverData = mapper.convertValue(data, ServerData.class);
                if (serverData.getType() == 1){
                    System.out.println("RESPONSE SCOPE On");
                    response.setType(serverData.getType());
                    response.setValue(serverData.getValue());
                    synchronized (response) {
                        response.notifyAll();
                    }
                }
                if (serverData.getType() == 2) {
                    saveAsNotification(new Notification(serverData.getValue()));
                }
            }
        } catch (IOException e) {
            close();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAsNotification(Notification notification) {
//        Notification notification = new Notification();
//        notification.setMessage(data);
        notificationRepository.save(notification);
    }

    public void close() {
        try {
            client.close();
            System.out.println("Disconnected!!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
