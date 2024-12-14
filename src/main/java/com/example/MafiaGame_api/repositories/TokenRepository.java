package com.example.MafiaGame_api.repositories;


import com.example.MafiaGame_api.models.Token;
import com.example.MafiaGame_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByUser(User user);

    Optional<Token> findByToken(String token);
}

