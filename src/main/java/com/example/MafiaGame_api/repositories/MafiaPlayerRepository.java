package com.example.MafiaGame_api.repositories;

import com.example.MafiaGame_api.models.Game;
import com.example.MafiaGame_api.models.MafiaPlayer;
import com.example.MafiaGame_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MafiaPlayerRepository extends JpaRepository<MafiaPlayer,Long> {

    Optional<MafiaPlayer> findByGameAndUser(Game game, User user);

}
