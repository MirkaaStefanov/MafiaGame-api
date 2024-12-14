package com.example.MafiaGame_api.handler;

import com.example.MafiaGame_api.exeptions.InvalidTokenException;
import com.example.MafiaGame_api.services.security.TokenService;
import com.example.MafiaGame_api.utils.ObjectMapperHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            try {
                ObjectMapperHelper.writeExceptionToObjectMapper(objectMapper, new InvalidTokenException(), response);
                return;
            } catch (IOException exception) {
                return;
            }
        }

        final String jwt = authHeader.substring(7);
        tokenService.logoutToken(jwt);
    }
}