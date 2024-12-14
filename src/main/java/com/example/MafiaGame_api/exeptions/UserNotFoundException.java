package com.example.MafiaGame_api.exeptions;



import com.example.MafiaGame_api.exeptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException() {
        super("User not found", HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String field) {
        super("User not found by " + field, HttpStatus.NOT_FOUND);
    }
}
