package com.example.MafiaGame_api.services;

import com.example.MafiaGame_api.dtos.GameDTO;
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
//    private final SimpMessagingTemplate messagingTemplate;

    public GameDTO createGame(){
        UserDTO authenticatedUser = userService.findAuthenticatedUser();
        User user = modelMapper.map(authenticatedUser, User.class);

        MafiaPlayer mafiaPlayer = new MafiaPlayer();
        mafiaPlayer.setUser(user);
        mafiaPlayer.setRole(PlayerRole.NARRATOR);

        Game game = new Game();
        game.getPlayers().add(mafiaPlayer);

        mafiaPlayer.setGame(game);

        mafiaPlayerRepository.save(mafiaPlayer);
        return modelMapper.map(gameRepository.save(game),GameDTO.class);

    }
    public GameDTO enterGame(Long gameId) throws ChangeSetPersister.NotFoundException {
        UserDTO authenticatedUser = userService.findAuthenticatedUser();
        User user = modelMapper.map(authenticatedUser, User.class);
        Game game = gameRepository.findById(gameId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        Optional<MafiaPlayer> optionalMafiaPlayer = mafiaPlayerRepository.findByGameAndUser(game,user);

        if(optionalMafiaPlayer.isPresent()){
            throw new ValidationException("This user is already in the game!");
        }

        MafiaPlayer mafiaPlayer = new MafiaPlayer();
        mafiaPlayer.setUser(user);
        mafiaPlayer.setGame(game);

        game.getPlayers().add(mafiaPlayer);

        mafiaPlayerRepository.save(mafiaPlayer);
        return modelMapper.map(gameRepository.save(game),GameDTO.class);
    }
    public void startGame(Long gameId, int killerQuantity, int doctorQuantity, int policeQuantity) throws ChangeSetPersister.NotFoundException {
        Game game = gameRepository.findById(gameId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<MafiaPlayer> mafiaPlayers = game.getPlayers();
        Collections.shuffle(mafiaPlayers);

        int createdKillers = 0;
        int createdDoctors = 0;
        int createdPolice = 0;

        for(MafiaPlayer mafiaPlayer : mafiaPlayers){

            if(mafiaPlayer.getRole() != null){
                continue;
            }
            if(createdKillers<killerQuantity){
                mafiaPlayer.setRole(PlayerRole.MAFIA);
                createdKillers++;
            }else if(createdDoctors<doctorQuantity){
                mafiaPlayer.setRole(PlayerRole.DOCTOR);
                createdKillers++;
            }else if(createdPolice<policeQuantity){
                mafiaPlayer.setRole(PlayerRole.POLICE);
                createdPolice++;
            }
        }
        mafiaPlayerRepository.saveAll(mafiaPlayers);
        for (MafiaPlayer mafiaPlayer : mafiaPlayers) {
//            messagingTemplate.convertAndSend("/topic/redirect/" + mafiaPlayer.getUser().getId(), "/mafiaPlayer/role");
        }
    }


}
