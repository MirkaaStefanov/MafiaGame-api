package com.example.MafiaGame_api.services.security;

import com.example.MafiaGame_api.dtos.auth.AdminUserDTO;
import com.example.MafiaGame_api.dtos.auth.PublicUserDTO;
import com.example.MafiaGame_api.dtos.auth.RegisterRequest;
import com.example.MafiaGame_api.enums.Role;
import com.example.MafiaGame_api.exeptions.UserCreateException;
import com.example.MafiaGame_api.exeptions.UserNotFoundException;
import com.example.MafiaGame_api.exeptions.common.AccessDeniedException;
import com.example.MafiaGame_api.models.User;
import com.example.MafiaGame_api.repositories.UserRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.MafiaGame_api.models.User.Role;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserServiceAuthentication {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User createUser(RegisterRequest request) {
        try {
            User user = User
                    .builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(com.example.MafiaGame_api.enums.Role.USER)
                    .address(request.getAddress())
                    .usernameField(request.getUsername())
                    .deleted(false)
                    .build();

            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UserCreateException(true);
        } catch (ConstraintViolationException exception) {
            throw new UserCreateException(exception.getConstraintViolations());
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email"));
    }

    @Override
    public List<AdminUserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(x -> modelMapper.map(x, AdminUserDTO.class))
                .toList();
    }

    @Override
    public AdminUserDTO updateUser(Long id, AdminUserDTO userDTO, PublicUserDTO currentUser) {
        User userToUpdate = findById(id);

        if (userToUpdate.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException();
        }

        modelMapper.map(userDTO, userToUpdate);
        userToUpdate.setId(id);

        User updatedUser = userRepository.save(userToUpdate);
        return modelMapper.map(updatedUser, AdminUserDTO.class);
    }


    @Override
    public void deleteUserById(Long id, PublicUserDTO currentUser) {
        User user = findById(id);

        if (user.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException();
        }

        user.setDeleted(true);
        userRepository.save(user);
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id"));
    }
}

