package com.example.MafiaGame_api.exeptions.common;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApiException {
    public InternalServerErrorException() {
        super("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}