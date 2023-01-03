package com.bastiansmn.scoreserver.service;

import com.bastiansmn.scoreserver.domain.*;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.exception.FunctionalRule;
import com.bastiansmn.scoreserver.repository.NamespaceRepository;
import com.bastiansmn.scoreserver.repository.ScoreRepository;
import com.bastiansmn.scoreserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NamespaceService {

    private final NamespaceRepository namespaceRepository;
    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Namespace> connect(String name, NamespaceConnectionRequest nsInfo)
            throws FunctionalException {
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

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(namespace);
    }

    public ResponseEntity<Namespace> create(String name) throws FunctionalException {
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

        URI uri = URI
                .create("/" + namespace.getName());

        return ResponseEntity
                .created(uri)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(namespace);
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
