package com.example.MafiaGame_api.services;

import com.example.MafiaGame_api.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_api.dtos.VoteResultDTO;
import com.example.MafiaGame_api.dtos.UserDTO;
import com.example.MafiaGame_api.dtos.VoteDTO;
import com.example.MafiaGame_api.models.Game;
import com.example.MafiaGame_api.models.MafiaPlayer;
import com.example.MafiaGame_api.models.User;
import com.example.MafiaGame_api.models.Vote;
import com.example.MafiaGame_api.repositories.GameRepository;
import com.example.MafiaGame_api.repositories.MafiaPlayerRepository;
import com.example.MafiaGame_api.repositories.UserRepository;
import com.example.MafiaGame_api.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final MafiaPlayerRepository mafiaPlayerRepository;
    private final GameRepository gameRepository;

    public VoteDTO vote(Long id) throws ChangeSetPersister.NotFoundException {
        UserDTO authenticatedUserDTO = userService.findAuthenticatedUser();
        User authenticatedUser = modelMapper.map(authenticatedUserDTO, User.class);
        User votedUser = userRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        Game game = gameRepository.findById(authenticatedUser.getGameId()).orElseThrow(ChangeSetPersister.NotFoundException::new);

        MafiaPlayer voterPlayer = mafiaPlayerRepository.findByGameAndUser(game, authenticatedUser).orElseThrow(ChangeSetPersister.NotFoundException::new);
        MafiaPlayer votedPlayer = mafiaPlayerRepository.findByGameAndUser(game, votedUser).orElseThrow(ChangeSetPersister.NotFoundException::new);

        Vote vote = new Vote();
        vote.setVoter(voterPlayer);
        vote.setVoted(votedPlayer);
        vote.setGame(game);

        return modelMapper.map(voteRepository.save(vote), VoteDTO.class);
    }

    public List<VoteResultDTO> getVoteResults(Long gameId) throws ChangeSetPersister.NotFoundException {
        Game game = gameRepository.findById(gameId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<MafiaPlayer> mafiaPlayers = mafiaPlayerRepository.findAllByGameIdAndRemovedFalseAndNotNarrator(gameId);
        List<VoteResultDTO>voteResults = new ArrayList<>();
        MafiaPlayerDTO mostVotedPlayer = new MafiaPlayerDTO();
        int maxVotes = Integer.MIN_VALUE;

        for (MafiaPlayer mafiaPlayer : mafiaPlayers) {
            int voteForPlayer = voteRepository.countVotesForPlayer(mafiaPlayer, game);
            MafiaPlayerDTO mafiaPlayerDTO = modelMapper.map(mafiaPlayer, MafiaPlayerDTO.class);
            if (voteForPlayer > maxVotes) {
                maxVotes = voteForPlayer;
                mostVotedPlayer = mafiaPlayerDTO;
            }
            voteResults.add(new VoteResultDTO(mafiaPlayerDTO, voteForPlayer));
        }
        mostVotedPlayer.setRemoved(true);

        return voteResults;
    }
}
