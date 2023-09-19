package com.client.controller;

import com.client.model.Notification;
import com.client.service.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController {
    private final Client client;

    @Autowired
    public ClientController(Client client) {
        this.client = client;
    }

    @PostMapping("/send")
    public String sendData(@RequestBody String data) throws InterruptedException {
        return client.send(data);
    }

    @GetMapping("/notifications")
    public List<Notification> getNotifications() {
        return client.getNotifications();
    }
}
