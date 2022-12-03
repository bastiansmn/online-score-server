package com.bastiansmn.scoreserver.controller;

import com.bastiansmn.scoreserver.domain.*;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.service.NamespaceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(
            value = "Connect to a namespace, knowing its name and accessUUID.",
            notes = "This will set a cookie with the accessUUID"
    )
    @PostMapping("/connect")
    public void connect(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace, @RequestBody NamespaceConnectionRequest nsInfo,
            HttpServletResponse response
    ) throws IOException, FunctionalException {
        this.namespaceService.connect(namespace, nsInfo, response);
    }

    @ApiOperation(
            value = "Fetching a namespace. Everyone can access this endpoint."
    )
    @GetMapping
    public ResponseEntity<Namespace> fetchNamespace(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace
    ) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.get(namespace));
    }

    @ApiOperation(
            value = "Gets all scores of the namespace (not ordered). Everyone can access this endpoint."
    )
    @GetMapping("/scores")
    public ResponseEntity<Collection<Score>> fetchScores(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace
    ) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.getAllScores(namespace));
    }

    @ApiOperation(
            value = "Adds a new user in the namespace. Everyone can access this endpoint.",
            notes = "This DOESN'T set his score (not even to 0). You need to update his score after this."
    )
    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace,
            @RequestBody UserCreationRequest userCreationRequest
    ) throws FunctionalException {
        return ResponseEntity.ok(namespaceService.registerUser(namespace, userCreationRequest));
    }

    @ApiOperation(
            value = "Creates a namespace. Everyone can access this endpoint.",
            notes = "This set a cookie with the accessUUID."
    )
    @PostMapping
    public void createNamespace(
            @PathVariable String namespace,
            @ApiParam(value = "Namespace (probably your application name)", example = "my-app")
            HttpServletResponse response
    )
            throws FunctionalException, IOException {
        namespaceService.create(namespace, response);
    }

    @ApiOperation(
            value = "Remove a namespace. You can access this namespace only if you have access to the cookie with accessUUID."
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteNamespace(
            @PathVariable @ApiParam(value = "Namespace (probably your application name)", example = "my-app") String namespace,
            HttpServletRequest request
    ) throws FunctionalException {
        namespaceService.delete(namespace, request);
        return ResponseEntity.noContent().build();
    }
}
