package com.client;

import com.client.service.ClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ClientApplication.class, args);
		ClientService clientService = context.getBean(ClientService.class);
		Thread thread = new Thread(clientService);
		thread.start();
	}

}
