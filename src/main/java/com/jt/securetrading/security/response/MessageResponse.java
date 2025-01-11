package com.jt.securetrading.security.response;

public class MessageResponse {
    private String message;

    // Constructor
    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter
    public String getMessage() {
        return message;
    }

    // Setter
    public void setMessage(String message) {
        this.message = message;
    }
}