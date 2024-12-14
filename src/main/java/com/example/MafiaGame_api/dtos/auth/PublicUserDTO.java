package com.example.MafiaGame_api.dtos.auth;


import com.example.MafiaGame_api.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserDTO {
    private Long id;
    private String firstname;

    @JsonProperty("username")
    private String usernameField;
    private String email;
    private Role role;
}
