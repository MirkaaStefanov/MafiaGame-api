package com.example.MafiaGame_api.services.security;




import com.example.MafiaGame_api.enums.TokenType;
import com.example.MafiaGame_api.models.Token;
import com.example.MafiaGame_api.models.User;

import java.util.List;

public interface TokenService {
    Token findByToken(String jwt);

    List<Token> findByUser(User user);

    void saveToken(User user, String jwtToken, TokenType tokenType);

    void revokeToken(Token token);

    void revokeAllUserTokens(User user);

    void logoutToken(String jwt);
}
