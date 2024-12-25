package com.example.MafiaGame_api.controllers;

import com.example.MafiaGame_api.dtos.VoteResultDTO;
import com.example.MafiaGame_api.dtos.VoteDTO;
import com.example.MafiaGame_api.services.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vote")
@AllArgsConstructor
public class VoteController {
    VoteService voteService;
    @PostMapping("/out")
    public ResponseEntity<VoteDTO> vote(@PathVariable(name = "id") Long id, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(voteService.vote(id));
    }
    @PostMapping("/all-votes")
    public ResponseEntity<List<VoteResultDTO>>getAllVotes(@PathVariable(name = "id") Long id, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(voteService.getVoteResults(id));
    }
}
