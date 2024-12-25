package com.example.MafiaGame_api.controllers;

import com.example.MafiaGame_api.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_api.services.MafiaPlayerService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mafiaPlayer")
@AllArgsConstructor
public class MafiaPlayerController {

    private final MafiaPlayerService mafiaPlayerService;

    @GetMapping("/findMyPlayer")
    public ResponseEntity<MafiaPlayerDTO> findMyPlayer(@RequestParam Long id, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(mafiaPlayerService.findMafiaPlayerByUser(id));
    }
    @GetMapping("/findAllPlayers")
    public ResponseEntity<List<MafiaPlayerDTO>> allMafiaPlayersThatPlay(@RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(mafiaPlayerService.allMafiaPlayersThatPlay());
    }

    @GetMapping("/findAllKillers")
    public ResponseEntity<List<MafiaPlayerDTO>> allKillers(@RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(mafiaPlayerService.allKillers());
    }

    @PostMapping("/findAllDoctors")
    public ResponseEntity<List<MafiaPlayerDTO>> allDoctors(@RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(mafiaPlayerService.allDoctors());
    }

}
