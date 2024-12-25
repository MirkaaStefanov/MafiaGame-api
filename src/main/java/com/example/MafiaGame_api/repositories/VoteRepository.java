package com.example.MafiaGame_api.repositories;

import com.example.MafiaGame_api.dtos.VoteResultDTO;
import com.example.MafiaGame_api.models.Game;
import com.example.MafiaGame_api.models.MafiaPlayer;
import com.example.MafiaGame_api.models.Vote;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.voted = :player AND v.game = :game")
    int countVotesForPlayer(@Param("player") MafiaPlayer player, @Param("game") Game game);
}
