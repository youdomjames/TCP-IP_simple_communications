package com.client.controller;

import com.client.model.Notification;
import com.client.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/send")
    public String sendData(@RequestBody String data) {
        String response;
        try {
             response = clientService.send(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        return response;
    }

    @GetMapping("/notifications")
    public List<Notification> getNotifications() {
        return clientService.getNotifications();
    }
}
