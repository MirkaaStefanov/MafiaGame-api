package com.example.MafiaGame_api.services;

import com.example.MafiaGame_api.dtos.GameDTO;
import com.example.MafiaGame_api.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_api.dtos.ResultDTO;
import com.example.MafiaGame_api.dtos.UserDTO;
import com.example.MafiaGame_api.enums.PlayerRole;
import com.example.MafiaGame_api.models.Game;
import com.example.MafiaGame_api.models.MafiaPlayer;
import com.example.MafiaGame_api.models.User;
import com.example.MafiaGame_api.repositories.GameRepository;
import com.example.MafiaGame_api.repositories.MafiaPlayerRepository;
import com.example.MafiaGame_api.repositories.UserRepository;
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
    private final UserRepository userRepository;

    public GameDTO createGame() {
        UserDTO authenticatedUser = userService.findAuthenticatedUser();
        User user = modelMapper.map(authenticatedUser, User.class);

        if (user.getGameId() != null) {
            throw new ValidationException("This user is already in a game!");
        }


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

        user.setGameId(game.getId());
        userRepository.save(user);

        mafiaPlayer.setGame(game);

        mafiaPlayerRepository.save(mafiaPlayer);

        List<MafiaPlayerDTO> mafiaPlayerDTOS = game.getPlayers()
                .stream()
                .map(mafiaPlayer1 -> modelMapper.map(mafiaPlayer1, MafiaPlayerDTO.class))
                .toList();

        GameDTO gameDTO = new GameDTO(game.getId(), mafiaPlayerDTOS, game.isActive(), game.isPlaying());
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
        if (user.getGameId() != null) {
            throw new ValidationException("This user is already in a game!");
        }
        if (!game.isActive()) {
            throw new ValidationException("Game is not active!");
        }
        if (game.isPlaying()) {
            throw new ValidationException("Game have started!");
        }

        MafiaPlayer mafiaPlayer = new MafiaPlayer();
        mafiaPlayer.setUser(user);
        mafiaPlayer.setGame(game);
        mafiaPlayerRepository.save(mafiaPlayer);


        game.getPlayers().add(mafiaPlayer);
        gameRepository.save(game);

        user.setGameId(game.getId());
        userRepository.save(user);

        List<MafiaPlayerDTO> mafiaPlayerDTOS = game.getPlayers()
                .stream()
                .map(mafiaPlayer1 -> modelMapper.map(mafiaPlayer1, MafiaPlayerDTO.class)) // Map each CartItem to CartItemDTO
                .toList();

        GameDTO gameDTO = new GameDTO(game.getId(), mafiaPlayerDTOS, game.isActive(), game.isPlaying());
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

    public void exitGame() throws ChangeSetPersister.NotFoundException {
        UserDTO authenticatedUser = userService.findAuthenticatedUser();
        User user = modelMapper.map(authenticatedUser, User.class);
        Game game = gameRepository.findById(user.getGameId()).orElseThrow(ChangeSetPersister.NotFoundException::new);

        MafiaPlayer mafiaPlayer = mafiaPlayerRepository.findByGameAndUser(game, user).orElseThrow(ChangeSetPersister.NotFoundException::new);

        if (game.isPlaying()) {
            throw new ValidationException("Game have started!");
        }

        if (mafiaPlayer.getRole() == PlayerRole.NARRATOR) {
            if (game.getPlayers().size() == 1) {
                game.setActive(false);
            } else {
                game.getPlayers().remove(mafiaPlayer);
                game.getPlayers().get(0).setRole(PlayerRole.NARRATOR);
            }
        } else {
            game.getPlayers().remove(mafiaPlayer);

        }
        user.setGameId(null);
        mafiaPlayer.setGame(null);

        gameRepository.save(game);
        userRepository.save(user);
        mafiaPlayerRepository.save(mafiaPlayer);
    }


    public void kill(Long playerId) throws ChangeSetPersister.NotFoundException {
        MafiaPlayer mafiaPlayer = mafiaPlayerRepository.findById(playerId).orElseThrow(ChangeSetPersister.NotFoundException::new);

        mafiaPlayer.setKilled(true);
        mafiaPlayerRepository.save(mafiaPlayer);
    }

    public void heal(Long playerId) throws ChangeSetPersister.NotFoundException {
        MafiaPlayer mafiaPlayer = mafiaPlayerRepository.findById(playerId).orElseThrow(ChangeSetPersister.NotFoundException::new);

        mafiaPlayer.setHealed(true);
        mafiaPlayerRepository.save(mafiaPlayer);
    }

    public ResultDTO resultInTheMorning() throws ChangeSetPersister.NotFoundException {
        UserDTO authenticatedUser = userService.findAuthenticatedUser();
        Game game = gameRepository.findById(authenticatedUser.getGameId()).orElseThrow(ChangeSetPersister.NotFoundException::new);

        MafiaPlayer killed = mafiaPlayerRepository.findByKilledTrueAndRemovedFalseAndGame_Id(game.getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);
        MafiaPlayer healed = mafiaPlayerRepository.findByHealedTrueAndRemovedFalseAndGame_Id(game.getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);

        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setKilled(null);
        resultDTO.setGameEnd(false);

        if (killed.equals(healed)) {
            killed.setKilled(false);
            healed.setHealed(false);
        }
        if (!killed.isHealed()) {
            killed.setRemoved(true);
            MafiaPlayerDTO mafiaPlayerDTO = modelMapper.map(killed, MafiaPlayerDTO.class);
            resultDTO.setKilled(mafiaPlayerDTO);
            healed.setHealed(false);
        }
        mafiaPlayerRepository.save(killed);
        mafiaPlayerRepository.save(healed);


        List<MafiaPlayer> mafiaPlayers = mafiaPlayerRepository.findAllByGameIdAndRemovedFalseAndNotNarrator(game.getId());
        List<MafiaPlayer> killers = mafiaPlayerRepository.findAllByRoleMafiaAndRemovedFalseAndGame_Id(game.getId());

        mafiaPlayers.remove(killers);

        if (mafiaPlayers.size() == killers.size()) {
            resultDTO.setGameEnd(true);
            resultDTO.setWinners(false);
        }
        if (killers.isEmpty()) {
            resultDTO.setGameEnd(true);
            resultDTO.setWinners(true);
        }

        return resultDTO;
    }

}
