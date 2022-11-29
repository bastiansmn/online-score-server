package com.bastiansmn.scoreserver.service;

import com.bastiansmn.scoreserver.domain.*;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.exception.FunctionalRule;
import com.bastiansmn.scoreserver.repository.NamespaceRepository;
import com.bastiansmn.scoreserver.repository.ScoreRepository;
import com.bastiansmn.scoreserver.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NamespaceService {

    private final NamespaceRepository namespaceRepository;
    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;

    public void connect(String name, NamespaceConnectionRequest nsInfo, HttpServletResponse response)
            throws FunctionalException, IOException {
        Optional<Namespace> namespaceOptional = namespaceRepository.findByName(name);
        if (namespaceOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        Namespace namespace = namespaceOptional.get();

        if (!namespace.getAccessUUID().equals(nsInfo.getAccessUUID()))
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        ResponseCookie cookie = ResponseCookie.from("access", namespace.getAccessUUID())
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.setHeader("Content-Type", "application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), namespace);
    }

    public void create(String name, HttpServletResponse response) throws FunctionalException, IOException {
        if (this.namespaceRepository.existsByName(name))
            throw new FunctionalException(FunctionalRule.NS_ALREADY_EXISTS.getMessage(), HttpStatus.BAD_REQUEST
            );

        Namespace namespace = Namespace.builder()
                .name(name)
                .scores(Set.of())
                .users(Set.of())
                .build();
        namespace = this.namespaceRepository.save(namespace);

        ResponseCookie cookie = ResponseCookie.from("access", namespace.getAccessUUID())
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.setHeader("Content-Type", "application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), namespace);
    }

    public Namespace get(String name) throws FunctionalException {
        Optional<Namespace> namespaceOptional = namespaceRepository.findByName(name);

        if (namespaceOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        return namespaceOptional.get();
    }

    public Collection<Score> getAllScores(String name) throws FunctionalException {
        return scoreRepository.findAllByNamespace(this.get(name));
    }

    public void delete(String name, HttpServletRequest request) throws FunctionalException {
        // No cookie found in request
        Cookie authorizationHeaderCookie = WebUtils.getCookie(request, "access");
        if (authorizationHeaderCookie == null)
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        String accessUUID = authorizationHeaderCookie.getValue();
        if (accessUUID == null)
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        Namespace namespace = this.get(name);

        if (!namespace.getAccessUUID().equals(accessUUID))
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        namespaceRepository.delete(namespace);
    }

    public User registerUser(String name, UserCreationRequest userCreationRequest) throws FunctionalException {
        Namespace namepsace = this.get(name);

        if (namepsace.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()).contains(userCreationRequest.getUsername()))
            throw new FunctionalException(
                    FunctionalRule.USER_ALREADY_EXISTS.getMessage(),
                    HttpStatus.BAD_REQUEST
            );

        // If user already exists, just add the namespace to the user
        Optional<User> userOptional = userRepository.findByUsername(userCreationRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getNamespaces().add(namepsace);
            return userRepository.save(user);
        }

        User user = User.builder()
                .username(userCreationRequest.getUsername())
                .namespaces(Set.of(namepsace))
                .build();

        return userRepository.save(user);
    }
}
