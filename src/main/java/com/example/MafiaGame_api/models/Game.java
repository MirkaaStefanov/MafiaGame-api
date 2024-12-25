package com.example.MafiaGame_api.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MafiaPlayer> players;
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean playing;
}
