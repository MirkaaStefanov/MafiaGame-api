package com.example.MafiaGame_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoteDTO {
    private Long id;
    private Long voterId;
    private Long votedId;
}
