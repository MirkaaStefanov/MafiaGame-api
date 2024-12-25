package com.example.MafiaGame_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO {

    private MafiaPlayerDTO killed;
    private boolean gameEnd;
    private boolean winners;
}
