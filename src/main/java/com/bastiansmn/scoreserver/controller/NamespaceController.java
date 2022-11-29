package com.bastiansmn.scoreserver.controller;

import com.bastiansmn.scoreserver.domain.*;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.service.NamespaceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{name}")
public class NamespaceController {

    private final NamespaceService namespaceService;

    @PostMapping("/connect")
    public void connect(@PathVariable String name, @RequestBody NamespaceConnectionRequest nsInfo, HttpServletResponse response)
            throws IOException, FunctionalException {
        this.namespaceService.connect(name, nsInfo, response);
    }

    @GetMapping
    public ResponseEntity<Namespace> fetchNamespace(@PathVariable String name) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.get(name));
    }

    @GetMapping("/scores")
    public ResponseEntity<Collection<Score>> fetchScores(@PathVariable String name) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.getAllScores(name));
    }

    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@PathVariable String name, @RequestBody UserCreationRequest userCreationRequest) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.registerUser(name, userCreationRequest));
    }

    @PostMapping
    public void createNamespace(@PathVariable String name, HttpServletResponse response)
            throws FunctionalException, IOException {
        namespaceService.create(name, response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNamespace(@PathVariable String name, HttpServletRequest request) throws FunctionalException {
        namespaceService.delete(name, request);
        return ResponseEntity.noContent().build();
    }
}
