package com.example.MafiaGame_api.dtos;

import com.example.MafiaGame_api.models.MafiaPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VoteResultDTO {
    MafiaPlayerDTO votedPlayer;
    int voteCount;
}
