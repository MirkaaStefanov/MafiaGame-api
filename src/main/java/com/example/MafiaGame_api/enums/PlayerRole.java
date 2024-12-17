package com.example.MafiaGame_api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlayerRole {

    MAFIA,
    POLICE,
    DOCTOR,
    VILLAGER,
    NARRATOR;

}
