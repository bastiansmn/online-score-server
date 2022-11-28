package com.bastiansmn.scoreserver.controller;

import com.bastiansmn.scoreserver.domain.Namespace;
import com.bastiansmn.scoreserver.domain.Score;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.service.NamespaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{name}")
public class NamespaceController {

    private final NamespaceService namespaceService;

    @GetMapping
    public ResponseEntity<Namespace> fetchNamespace(@PathVariable String name) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.get(name));
    }

    @GetMapping("/scores")
    public ResponseEntity<Collection<Score>> fetchScores(@PathVariable String name) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.getAllScores(name));
    }

    // TODO: fetch last scores of namespace (all user)

    @PostMapping
    public ResponseEntity<Namespace> createNamespace(@PathVariable String name) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.create(name));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNamespace(@PathVariable String name) throws FunctionalException {
        namespaceService.delete(name);
        return ResponseEntity.noContent().build();
    }
}
