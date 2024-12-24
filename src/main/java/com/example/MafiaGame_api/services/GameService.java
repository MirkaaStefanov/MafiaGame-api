package com.example.MafiaGame_api.services;

import com.example.MafiaGame_api.dtos.GameDTO;
import com.example.MafiaGame_api.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_api.dtos.UserDTO;
import com.example.MafiaGame_api.enums.PlayerRole;
import com.example.MafiaGame_api.models.Game;
import com.example.MafiaGame_api.models.MafiaPlayer;
import com.example.MafiaGame_api.models.User;
import com.example.MafiaGame_api.repositories.GameRepository;
import com.example.MafiaGame_api.repositories.MafiaPlayerRepository;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MafiaPlayerRepository mafiaPlayerRepository;
    private final GameRepository gameRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public GameDTO createGame() {
        UserDTO authenticatedUser = userService.findAuthenticatedUser();
        User user = modelMapper.map(authenticatedUser, User.class);

        MafiaPlayer mafiaPlayer = new MafiaPlayer();
        mafiaPlayer.setUser(user);
        mafiaPlayer.setRole(PlayerRole.NARRATOR);
        mafiaPlayerRepository.save(mafiaPlayer);

        List<MafiaPlayer> players = new ArrayList<>();
        players.add(mafiaPlayer);


        Game game = new Game();
        game.setPlayers(players);
        game.setActive(true);
        gameRepository.save(game);

        mafiaPlayer.setGame(game);

        mafiaPlayerRepository.save(mafiaPlayer);

        List<MafiaPlayerDTO> mafiaPlayerDTOS = game.getPlayers()
                .stream()
                .map(mafiaPlayer1 -> modelMapper.map(mafiaPlayer1, MafiaPlayerDTO.class)) // Map each CartItem to CartItemDTO
                .toList();

        GameDTO gameDTO = new GameDTO(game.getId(), mafiaPlayerDTOS, game.isActive());
        return gameDTO;

    }

    public GameDTO enterGame(Long gameId) throws ChangeSetPersister.NotFoundException {
        UserDTO authenticatedUser = userService.findAuthenticatedUser();
        User user = modelMapper.map(authenticatedUser, User.class);
        Game game = gameRepository.findById(gameId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        Optional<MafiaPlayer> optionalMafiaPlayer = mafiaPlayerRepository.findByGameAndUser(game, user);

        if (optionalMafiaPlayer.isPresent()) {
            throw new ValidationException("This user is already in the game!");
        }

        MafiaPlayer mafiaPlayer = new MafiaPlayer();
        mafiaPlayer.setUser(user);
        mafiaPlayer.setGame(game);
        mafiaPlayerRepository.save(mafiaPlayer);

        game.getPlayers().add(mafiaPlayer);
        gameRepository.save(game);



        List<MafiaPlayerDTO> mafiaPlayerDTOS = game.getPlayers()
                .stream()
                .map(mafiaPlayer1 -> modelMapper.map(mafiaPlayer1, MafiaPlayerDTO.class)) // Map each CartItem to CartItemDTO
                .toList();

        GameDTO gameDTO = new GameDTO(game.getId(), mafiaPlayerDTOS, game.isActive());
        return gameDTO;
    }

    public void startGame(Long gameId, int killerQuantity, int doctorQuantity, int policeQuantity) throws ChangeSetPersister.NotFoundException {
        Game game = gameRepository.findById(gameId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<MafiaPlayer> mafiaPlayers = game.getPlayers();
        Collections.shuffle(mafiaPlayers);

        int createdKillers = 0;
        int createdDoctors = 0;
        int createdPolice = 0;

        for (MafiaPlayer mafiaPlayer : mafiaPlayers) {

            if (mafiaPlayer.getRole() != null) {
                continue;
            }
            if (createdKillers < killerQuantity) {
                mafiaPlayer.setRole(PlayerRole.MAFIA);
                createdKillers++;
            } else if (createdDoctors < doctorQuantity) {
                mafiaPlayer.setRole(PlayerRole.DOCTOR);
                createdKillers++;
            } else if (createdPolice < policeQuantity) {
                mafiaPlayer.setRole(PlayerRole.POLICE);
                createdPolice++;
            } else {
                mafiaPlayer.setRole(PlayerRole.VILLAGER);
            }
        }
        mafiaPlayerRepository.saveAll(mafiaPlayers);
        for (MafiaPlayer mafiaPlayer : mafiaPlayers) {
            if (mafiaPlayer.getRole() != PlayerRole.NARRATOR) {
                messagingTemplate.convertAndSend("/topic/redirect/" + mafiaPlayer.getUser().getId(), "/mafiaPlayer/role?gameId=" + game.getId());
            }
        }
    }

    public List<MafiaPlayerDTO> allMafiaPlayersInGame(Long id) throws ChangeSetPersister.NotFoundException {
        Game game = gameRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

        List<MafiaPlayerDTO> mafiaPlayerDTOS = game.getPlayers()
                .stream()
                .map(mafiaPlayer1 -> modelMapper.map(mafiaPlayer1, MafiaPlayerDTO.class)) // Map each CartItem to CartItemDTO
                .toList();

        return mafiaPlayerDTOS;
    }

}
