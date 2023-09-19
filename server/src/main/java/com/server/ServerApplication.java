package com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ServerApplication.class, args);
		Server server = context.getBean(Server.class);
		Thread thread = new Thread(server);
		try {
			thread.start();
			server.receiveRequests();
		} catch (Exception e) {
			e.printStackTrace();
			server.shutDown();
		}
	}

}
