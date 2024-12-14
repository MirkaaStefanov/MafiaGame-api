package com.example.MafiaGame_api.utils;



import com.example.MafiaGame_api.dtos.ExceptionResponse;
import com.example.MafiaGame_api.exeptions.common.ApiException;

import java.time.LocalDateTime;

public class ApiExceptionParser {
    public static ExceptionResponse parseException(ApiException exception) {
        return ExceptionResponse
                .builder()
                .dateTime(LocalDateTime.now())
                .message(exception.getMessage())
                .status(exception.getStatus())
                .statusCode(exception.getStatusCode())
                .build();
    }
}
