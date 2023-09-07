package com.example.zoo.exceptions;

public class OperationException extends RuntimeException {
    public OperationException(ApiErrors message) {
        super(message.getMessage());
    }
}
