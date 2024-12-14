package com.example.MafiaGame_api.services.security;


import com.example.MafiaGame_api.enums.TokenType;
import com.example.MafiaGame_api.models.Token;
import com.example.MafiaGame_api.models.User;
import com.example.MafiaGame_api.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Token findByToken(String jwt) {
        return tokenRepository.findByToken(jwt).orElse(null);
    }

    @Override
    public List<Token> findByUser(User user) {
        return tokenRepository.findAllByUser(user);
    }

    @Override
    public void saveToken(User user, String jwtToken, TokenType tokenType) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    @Override
    public void revokeToken(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        tokenRepository.deleteAll(tokenRepository.findAllByUser(user));
    }

    @Override
    @Transactional
    public void logoutToken(String jwt) {
        Token storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        if (storedToken == null) {
            return;
        }

        revokeAllUserTokens(storedToken.getUser());
        SecurityContextHolder.clearContext();
    }
}
