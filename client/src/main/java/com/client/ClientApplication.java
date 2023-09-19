package com.client;

import com.client.service.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ClientApplication.class, args);
		Client client = context.getBean(Client.class);
		Thread thread = new Thread(client);
		thread.start();
	}

}
