package com.example.MafiaGame_api.controllers;

import com.example.MafiaGame_api.dtos.GameDTO;
import com.example.MafiaGame_api.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_api.services.GameService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/players")
    public ResponseEntity<List<MafiaPlayerDTO>> allMafiaPlayersInGame(@RequestParam(name = "gameId") Long gameId, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(gameService.allMafiaPlayersInGame(gameId));
    }

    @PostMapping("/exit")
    public void exitGame(@RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        gameService.exitGame();
    }
}
