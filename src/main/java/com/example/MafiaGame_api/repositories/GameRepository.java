package com.example.MafiaGame_api.repositories;

import com.example.MafiaGame_api.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {

    Optional<Game> findById(Long id);

    @Query("SELECT g FROM Game g JOIN g.players p WHERE p.user.id = :userId AND g.active = true")
    Optional<Game> findGameByUserId(@Param("userId") Long userId);

}
