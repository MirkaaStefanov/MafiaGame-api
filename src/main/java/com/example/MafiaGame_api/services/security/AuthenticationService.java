package com.example.MafiaGame_api.services.security;



import com.example.MafiaGame_api.dtos.auth.AuthenticationRequest;
import com.example.MafiaGame_api.dtos.auth.AuthenticationResponse;
import com.example.MafiaGame_api.dtos.auth.RefreshTokenBodyDTO;
import com.example.MafiaGame_api.dtos.auth.RegisterRequest;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshTokenBodyDTO refreshTokenBodyDTO) throws IOException;

}
