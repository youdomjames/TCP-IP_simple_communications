package com.client.model;

/*
 type 1 = Response to request
 type 2 = Notification
**/
public class ServerData {
    private int type;
    private String value;

    public ServerData() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
