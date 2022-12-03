package com.bastiansmn.scoreserver.controller;

import com.bastiansmn.scoreserver.domain.Score;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.service.ScoreService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(
            value = "Update the score of a user in a namespace. You need to be connected to the namespace to do this."
    )
    @PutMapping("/{namespace}/{username}")
    public ResponseEntity<Score> updateScore(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace,
            @PathVariable @ApiParam(value = "The username (unique) of the user you want to update score.", example = "player1") String username,
            @RequestParam @ApiParam(value = "The new score of the user (can be lower). Prefer storing your decimals as an integer (1.65 = 165)", example = "189") Long score,
            HttpServletRequest request
    ) throws FunctionalException {
        return ResponseEntity.ok(scoreService.updateScore(namespace, username, score, request));
    }

    @ApiOperation(
            value = "Get the last score of a user in a namespace. Everyone can access this endpoint."
    )
    @GetMapping("/{namespace}/{username}")
    public ResponseEntity<Score> getLastOfUser(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace,
            @PathVariable @ApiParam(value = "The username of the user", example = "player1") String username
    ) throws FunctionalException {
        return ResponseEntity.ok(scoreService.getLastOfUser(username, namespace));
    }

    @ApiOperation(
            value = "WARNING: Not implemented yet. Everyone can access this endpoint."
    )
    @GetMapping("/all/{namespace}/{username}")
    public ResponseEntity<List<Score>> getAllOfUser(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace,
            @PathVariable @ApiParam(value = "The username of the user", example = "player1") String username
    ) throws FunctionalException {
        return ResponseEntity.ok(scoreService.getAllOfUser(username, namespace));
    }

    @ApiOperation(
            value = "Deletes a user from a namespace. You need to be connected to the namespace to do this."
    )
    @DeleteMapping("/{namespace}/{username}")
    public ResponseEntity<Void> delete(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace,
            @PathVariable @ApiParam(value = "The user of the user", example = "player1") String username,
            HttpServletRequest request
    ) throws FunctionalException {
        scoreService.delete(username, namespace, request);
        return ResponseEntity.noContent().build();
    }

}
