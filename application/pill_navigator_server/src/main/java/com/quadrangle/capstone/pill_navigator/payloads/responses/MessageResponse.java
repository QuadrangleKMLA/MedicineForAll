package com.quadrangle.capstone.pill_navigator.payloads.responses;

import org.springframework.security.core.parameters.P;

public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) {
        this.message = message;
    }
}
