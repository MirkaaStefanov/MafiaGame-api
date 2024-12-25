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

import java.util.List;

@Service
@AllArgsConstructor
public class MafiaPlayerService {

    private final MafiaPlayerRepository mafiaPlayerRepository;
    private final GameRepository gameRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public MafiaPlayerDTO findMafiaPlayerByUser(Long gameId) throws ChangeSetPersister.NotFoundException {
        UserDTO userDTO = userService.findAuthenticatedUser();
        User user = modelMapper.map(userDTO, User.class);
        Game game = gameRepository.findById(gameId).orElseThrow(ChangeSetPersister.NotFoundException::new);

        MafiaPlayer mafiaPlayer = mafiaPlayerRepository.findByGameAndUser(game, user).orElseThrow(ChangeSetPersister.NotFoundException::new);

        return modelMapper.map(mafiaPlayer, MafiaPlayerDTO.class);
    }

    public List<MafiaPlayerDTO> allMafiaPlayersThatPlay() {
        UserDTO userDTO = userService.findAuthenticatedUser();

        List<MafiaPlayer> mafiaPlayers = mafiaPlayerRepository.findAllByGameIdAndRemovedFalseAndNotNarrator(userDTO.getGameId());


        List<MafiaPlayerDTO> mafiaPlayerDTOS = mafiaPlayers
                .stream()
                .map(mafiaPlayer1 -> modelMapper.map(mafiaPlayer1, MafiaPlayerDTO.class))
                .toList();
        return mafiaPlayerDTOS;
    }

    public List<MafiaPlayerDTO> allKillers() {
        UserDTO userDTO = userService.findAuthenticatedUser();

        List<MafiaPlayer> mafiaPlayers = mafiaPlayerRepository.findAllByRoleMafiaAndGame_Id(userDTO.getGameId());


        List<MafiaPlayerDTO> mafiaPlayerDTOS = mafiaPlayers
                .stream()
                .map(mafiaPlayer1 -> modelMapper.map(mafiaPlayer1, MafiaPlayerDTO.class))
                .toList();
        return mafiaPlayerDTOS;
    }
    public List<MafiaPlayerDTO> allDoctors() {
        UserDTO userDTO = userService.findAuthenticatedUser();

        List<MafiaPlayer> mafiaPlayers = mafiaPlayerRepository.findAllByRoleDoctorRemovedFalseAndGame_Id(userDTO.getGameId());


        List<MafiaPlayerDTO> mafiaPlayerDTOS = mafiaPlayers
                .stream()
                .map(mafiaPlayer1 -> modelMapper.map(mafiaPlayer1, MafiaPlayerDTO.class))
                .toList();
        return mafiaPlayerDTOS;
    }

}
