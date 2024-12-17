package com.example.MafiaGame_api.models;

import com.example.MafiaGame_api.enums.PlayerRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mafia_players")
public class MafiaPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlayerRole role;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean killed;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean removed;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean healed;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;


}
