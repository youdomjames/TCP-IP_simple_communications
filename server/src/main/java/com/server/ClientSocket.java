package com.server;

import java.io.IOException;
import java.net.Socket;

public class ClientSocket {
    private final String ID;
    private final Socket socket;
    private boolean isConnected;

    public ClientSocket(String ID, Socket socket) {
        this.ID = ID;
        this.socket = socket;
        this.isConnected = socket.isConnected();
    }

    public String getID() {
        return ID;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void close(){
        try {
            socket.close();
            setConnected(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
