package com.example.MafiaGame_api.dtos;

import com.example.MafiaGame_api.models.MafiaPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GameDTO {

    private Long id;
    private List<MafiaPlayerDTO> players;
    private boolean active;
}
