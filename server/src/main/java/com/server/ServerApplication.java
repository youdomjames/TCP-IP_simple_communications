package com.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ServerApplication.class, args);

		Server server = context.getBean(Server.class);
		Thread thread = new Thread(server);
		try {
			server.start();
			thread.start();
			server.sendData();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
