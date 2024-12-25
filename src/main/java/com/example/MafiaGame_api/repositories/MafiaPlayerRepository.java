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

    @Query("SELECT m FROM MafiaPlayer m WHERE m.role = 'MAFIA' AND m.game.id = :gameId AND m.removed = false")
    List<MafiaPlayer> findAllByRoleMafiaAndRemovedFalseAndGame_Id(Long gameId);

    @Query("SELECT m FROM MafiaPlayer m WHERE m.role = 'DOCTOR' AND m.removed = false AND m.game.id = :gameId")
    List<MafiaPlayer> findAllByRoleDoctorAndRemovedFalseAndGame_Id(Long gameId);


    Optional<MafiaPlayer> findByKilledTrueAndRemovedFalseAndGame_Id(Long gameId);

    Optional<MafiaPlayer> findByHealedTrueAndRemovedFalseAndGame_Id(Long gameId);

}
