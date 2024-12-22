package com.example.MafiaGame_api.controllers;

import com.example.MafiaGame_api.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_api.services.MafiaPlayerService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mafiaPlayer")
@AllArgsConstructor
public class MafiaPlayerController {

    private final MafiaPlayerService mafiaPlayerService;

    @GetMapping("/findMyPlayer")
    public ResponseEntity<MafiaPlayerDTO> findMyPlayer(@RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(mafiaPlayerService.findMafiaPlayerByUser());
    }

}
