package com.example.MafiaGame_api.repositories;

import com.example.MafiaGame_api.models.MafiaPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MafiaPlayerRepository extends JpaRepository<MafiaPlayer,Long> {

}
