package com.example.MafiaGame_api.exeptions;

public class CustomEmailException extends RuntimeException {
    public CustomEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
