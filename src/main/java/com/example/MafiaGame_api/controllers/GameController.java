package com.example.MafiaGame_api.controllers;

import com.example.MafiaGame_api.dtos.GameDTO;
import com.example.MafiaGame_api.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameDTO> createGame(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(gameService.createGame());
    }

    @PostMapping("/enter")
    public ResponseEntity<GameDTO> enterGame(@RequestParam Long id, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(gameService.enterGame(id));
    }

    @PostMapping("/start")
    public void startGame(@RequestParam Long gameId, @RequestParam int killerQuantity, @RequestParam int doctorQuantity, @RequestParam int policeQuantity, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        gameService.startGame(gameId, killerQuantity, doctorQuantity, policeQuantity);
    }

}
