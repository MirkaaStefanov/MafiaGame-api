package com.example.MafiaGame_api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public enum Role {

    USER,
    ADMIN;

}
