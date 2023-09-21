package com.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class RequestsHandler implements Runnable{
    private final ClientSocket clientSocket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final ObjectOutputStream objectOutputStream;
    public RequestsHandler(ClientSocket clientSocket, ObjectOutputStream objectOutputStream) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new PrintWriter(clientSocket.getSocket().getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getSocket().getInputStream()));
        this.objectOutputStream = objectOutputStream;
    }
    @Override
    public void run() {
        try {
            String data;
            ObjectMapper mapper = new ObjectMapper();
            while((data = reader.readLine()) != null){
                System.out.println(Thread.currentThread().getName() + " Data from Client: " + data);
                if (data.startsWith("REQUEST_")) {
                    objectOutputStream.writeObject(mapper.convertValue(new ServerData(1,"Hello Client"), Object.class)); //Todo: Find a better way
                    objectOutputStream.reset();
//                    writer.println(new ServerData("Hello Client"));
                }
            }
        } catch (IOException e) {
            shutdown();
            e.printStackTrace();
        }
    }

    private void shutdown() {
        try {
            writer.close();
            reader.close();
            clientSocket.close();
            System.out.println("Requests' Handler thread shutdown for client = " + clientSocket.getID());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
