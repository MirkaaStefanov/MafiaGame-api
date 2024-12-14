package com.example.MafiaGame_api.controllers;


import com.example.MafiaGame_api.dtos.auth.AuthenticationRequest;
import com.example.MafiaGame_api.dtos.auth.AuthenticationResponse;
import com.example.MafiaGame_api.dtos.auth.RefreshTokenBodyDTO;
import com.example.MafiaGame_api.dtos.auth.RegisterRequest;
import com.example.MafiaGame_api.services.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LogoutHandler logoutHandler;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenBodyDTO refreshTokenBody) throws IOException {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenBody));
    }

}
