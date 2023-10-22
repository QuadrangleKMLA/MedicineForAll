package com.quadrangle.capstone.pill_navigator.exceptions.exceptionClass;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
