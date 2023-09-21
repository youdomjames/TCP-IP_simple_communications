package com.server;

import java.io.Serializable;

public record Notification(String message) implements Serializable {
}
