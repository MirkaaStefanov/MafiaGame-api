package com.example.MafiaGame_api.repositories;

import com.example.MafiaGame_api.models.Game;
import com.example.MafiaGame_api.models.MafiaPlayer;
import com.example.MafiaGame_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MafiaPlayerRepository extends JpaRepository<MafiaPlayer,Long> {

    Optional<MafiaPlayer> findByGameAndUser(Game game, User user);

    @Query("SELECT mp FROM MafiaPlayer mp WHERE mp.game.id = :gameId AND mp.removed = false AND mp.role <> 'NARRATOR'")
    List<MafiaPlayer> findAllByGameIdAndRemovedFalseAndNotNarrator(Long gameId);

    @Query("SELECT m FROM MafiaPlayer WHERE m.role = 'MAFIA' AND m.game.id = :gameId AND m.removed = false")
    List<MafiaPlayer> findAllByRoleMafiaRemovedFalseAndGame_Id(Long gameId);

    List<MafiaPlayer> findAllByRoleDoctorRemovedFalseAndGame_Id(Long gameId);

    Optional<MafiaPlayer> findByKilledTrueRemovedFalseAndGame_Id(Long gameId);

    Optional<MafiaPlayer> findByHealedTrueRemovedFalseAndGame_Id(Long gameId);

}
