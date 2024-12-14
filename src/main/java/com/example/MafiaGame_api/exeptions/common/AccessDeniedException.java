package com.example.MafiaGame_api.exeptions.common;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApiException {
    public AccessDeniedException() {
        super("Access Denied", HttpStatus.FORBIDDEN);
    }
}
