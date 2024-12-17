package com.example.MafiaGame_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GameDTO {

    private Long id;
    private List<Long> players;
    private boolean active;
}
