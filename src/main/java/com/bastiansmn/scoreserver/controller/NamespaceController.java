package com.bastiansmn.scoreserver.controller;

import com.bastiansmn.scoreserver.domain.*;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.service.NamespaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{namespace}")
public class NamespaceController {

    private final NamespaceService namespaceService;

    @PostMapping("/connect")
    public void connect(@PathVariable String namespace, @RequestBody NamespaceConnectionRequest nsInfo, HttpServletResponse response)
            throws IOException, FunctionalException {
        this.namespaceService.connect(namespace, nsInfo, response);
    }

    @GetMapping
    public ResponseEntity<Namespace> fetchNamespace(@PathVariable String namespace) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.get(namespace));
    }

    @GetMapping("/scores")
    public ResponseEntity<Collection<Score>> fetchScores(@PathVariable String namespace) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.getAllScores(namespace));
    }

    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@PathVariable String namespace, @RequestBody UserCreationRequest userCreationRequest) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.registerUser(namespace, userCreationRequest));
    }

    @PostMapping
    public void createNamespace(@PathVariable String namespace, HttpServletResponse response)
            throws FunctionalException, IOException {
        namespaceService.create(namespace, response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNamespace(@PathVariable String namespace, HttpServletRequest request) throws FunctionalException {
        namespaceService.delete(namespace, request);
        return ResponseEntity.noContent().build();
    }
}
