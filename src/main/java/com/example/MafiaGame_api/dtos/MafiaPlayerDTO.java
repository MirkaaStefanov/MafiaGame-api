package com.example.MafiaGame_api.dtos;

import com.example.MafiaGame_api.enums.PlayerRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MafiaPlayerDTO {

    private Long id;
    private UserDTO user;
    private PlayerRole role;
    private boolean killed;
    private boolean removed;
    private boolean healed;

}
