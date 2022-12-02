package com.bastiansmn.scoreserver.controller;

import com.bastiansmn.scoreserver.domain.Score;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PutMapping("/{namespace}/{username}")
    public ResponseEntity<Score> updateScore(@PathVariable String namespace, @PathVariable String username, @RequestParam Long score, HttpServletRequest request)
            throws FunctionalException {
        return ResponseEntity.ok(scoreService.updateScore(namespace, username, score, request));
    }

    @GetMapping("/{namespace}/{username}")
    public ResponseEntity<Score> getLastOfUser(@PathVariable String username, @PathVariable String namespace) throws FunctionalException {
        return ResponseEntity.ok(scoreService.getLastOfUser(username, namespace));
    }

    @GetMapping("/all/{namespace}/{username}")
    public ResponseEntity<List<Score>> getAllOfUser(@PathVariable String username, @PathVariable String namespace) throws FunctionalException {
        return ResponseEntity.ok(scoreService.getAllOfUser(username, namespace));
    }

    @DeleteMapping("/{namespace}/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username, @PathVariable String namespace, HttpServletRequest request) throws FunctionalException {
        scoreService.delete(username, namespace, request);
        return ResponseEntity.noContent().build();
    }

}
