package com.server;

/*
    type 1 = Response to request
    type 2 = Notification
**/
public record ServerData(int type, String value) {
}
