package com.bastiansmn.scoreserver.controller;

import com.bastiansmn.scoreserver.domain.Score;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping("/create/{namespace}/{user_uuid}")
    public ResponseEntity<Score> create(@PathVariable String namespace, @PathVariable String user_uuid, @RequestParam Long score)
            throws FunctionalException {
        return ResponseEntity.ok(scoreService.create(namespace, user_uuid, score));
    }

    // TODO: Trouver comment gérer la collision de noms (autre moyen d'avoir un historique des scores ?)
    @GetMapping("/{user_uuid}/{namespace}")
    public ResponseEntity<Score> getLastOfUser(@PathVariable String user_uuid, @PathVariable String namespace) throws FunctionalException {
        return ResponseEntity.ok(scoreService.getLastOfUser(user_uuid, namespace));
    }

    @GetMapping("/all/{user_uuid}/{namespace}")
    public ResponseEntity<List<Score>> getAllOfUser(@PathVariable String user_uuid, @PathVariable String namespace) throws FunctionalException {
        return ResponseEntity.ok(scoreService.getAllOfUser(user_uuid, namespace));
    }

    @DeleteMapping("/{score_id}")
    public ResponseEntity<Void> delete(@PathVariable Long score_id) throws FunctionalException {
        scoreService.delete(score_id);
        return ResponseEntity.noContent().build();
    }

}
