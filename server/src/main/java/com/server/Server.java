package com.server;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

@Service
public class Server implements Runnable{

    private static ServerSocket serverSocket;
    private Socket server;

    public void start() throws IOException {
            serverSocket = new ServerSocket(8000);
            System.out.println("Waiting for clients");
            server = serverSocket.accept();
            System.out.println("New client connected");
    }

    public void sendData() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(server.getOutputStream());
        int count = 1;
        while (!serverSocket.isClosed()){
            writer.println("Notification " + count++);
            writer.flush();
            Thread.sleep(30000);
        }
    }

    public void receiveData() {
        while (!server.isClosed() && server.getChannel().isConnected()){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
                String data = reader.readLine();
                System.out.println("Data from Client: "+data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        receiveData();
    }
}
