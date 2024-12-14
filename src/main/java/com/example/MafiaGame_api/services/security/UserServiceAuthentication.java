package com.example.MafiaGame_api.services.security;


import com.example.MafiaGame_api.dtos.auth.AdminUserDTO;
import com.example.MafiaGame_api.dtos.auth.PublicUserDTO;
import com.example.MafiaGame_api.dtos.auth.RegisterRequest;
import com.example.MafiaGame_api.models.User;

import java.util.List;

public interface UserServiceAuthentication {
    User createUser(RegisterRequest request);

    User findByEmail(String email);

    List<AdminUserDTO> getAllUsers();

    AdminUserDTO updateUser(Long id, AdminUserDTO userDTO, PublicUserDTO currentUser);

    void deleteUserById(Long id, PublicUserDTO currentUser);
}
