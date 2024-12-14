package com.example.MafiaGame_api.exeptions;


import com.example.MafiaGame_api.exeptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {
    public InvalidTokenException() {
        super("Invalid token", HttpStatus.UNAUTHORIZED);
    }
}
