package com.example.MafiaGame_api.exeptions;


import com.example.MafiaGame_api.exeptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserLoginException extends ApiException {
    public UserLoginException() {
        super("Invalid email or password", HttpStatus.BAD_REQUEST);
    }
}