package com.example.MafiaGame_api.services;

import com.example.MafiaGame_api.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_api.dtos.UserDTO;
import com.example.MafiaGame_api.models.Game;
import com.example.MafiaGame_api.models.MafiaPlayer;
import com.example.MafiaGame_api.models.User;
import com.example.MafiaGame_api.repositories.GameRepository;
import com.example.MafiaGame_api.repositories.MafiaPlayerRepository;
import com.example.MafiaGame_api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MafiaPlayerService {

    private final MafiaPlayerRepository mafiaPlayerRepository;
    private final GameRepository gameRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public MafiaPlayerDTO findMafiaPlayerByUser() throws ChangeSetPersister.NotFoundException {
        UserDTO userDTO = userService.findAuthenticatedUser();
        User user = modelMapper.map(userDTO, User.class);
        Game game = gameRepository.findGameByUserId(userDTO.getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);

        MafiaPlayer mafiaPlayer = mafiaPlayerRepository.findByGameAndUser(game,user).orElseThrow(ChangeSetPersister.NotFoundException::new);

        return modelMapper.map(mafiaPlayer,MafiaPlayerDTO.class);
    }

}
