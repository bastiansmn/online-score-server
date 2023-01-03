package com.bastiansmn.scoreserver.service;

import com.bastiansmn.scoreserver.domain.Namespace;
import com.bastiansmn.scoreserver.domain.Score;
import com.bastiansmn.scoreserver.domain.User;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.exception.FunctionalRule;
import com.bastiansmn.scoreserver.repository.NamespaceRepository;
import com.bastiansmn.scoreserver.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScoreService {
    private final NamespaceService namespaceService;

    private final ScoreRepository scoreRepository;
    private final NamespaceRepository namespaceRepository;

    private static final String COOKIE_NAME = "access";

    public Score updateScore(String namespace, String username, Long score, HttpServletRequest request) throws FunctionalException {
        Cookie authorizationHeaderCookie = WebUtils.getCookie(request, COOKIE_NAME);
        if (authorizationHeaderCookie == null)
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        String accessUUID = authorizationHeaderCookie.getValue();
        if (accessUUID == null)
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        Namespace ns = this.namespaceService.get(namespace);

        if (!ns.getAccessUUID().equals(accessUUID))
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        Optional<Namespace> namespaceOptional = namespaceRepository.findByName(namespace);

        if (namespaceOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        if (!namespaceOptional.get()
                .getUsers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toSet())
                .contains(username))
            throw new FunctionalException(
                    FunctionalRule.USER_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        User user = namespaceOptional.get()
                .getUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.USER_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND
                ));

        Optional<Score> scoreOptional = scoreRepository.findByUserAndNamespace(user, namespaceOptional.get());

        if (scoreOptional.isEmpty()) {
            Score newScore = Score.builder()
                    .score(score)
                    .user(user)
                    .namespace(namespaceOptional.get())
                    .build();

            return scoreRepository.save(newScore);
        }

        scoreOptional.get().setScore(score);
        scoreOptional.get().setUpdatedAt(Date.from(Instant.now()));

        return scoreRepository.save(scoreOptional.get());
    }

    public Score getLastOfUser(String username, String namespace) throws FunctionalException {
        Optional<Namespace> namespaceOptional = namespaceRepository.findByName(namespace);

        if (namespaceOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        if (!namespaceOptional.get()
                .getUsers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toSet())
                .contains(username))
            throw new FunctionalException(
                    FunctionalRule.USER_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        User user = namespaceOptional.get()
                .getUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.USER_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND
                ));

        Optional<Score> scoreOptional = scoreRepository.findByUserAndNamespace(user, namespaceOptional.get());

        if (scoreOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.SCORE_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        return scoreOptional.get();
    }

    public List<Score> getAllOfUser(String username, String namespace) throws FunctionalException {
        Namespace ns = this.namespaceService.get(namespace);

        return ns.getScores().stream().filter(score -> score.getUser().getUsername().equals(username)).toList();
    }

    public void delete(String username, String namespace, HttpServletRequest request) throws FunctionalException {
        Cookie authorizationHeaderCookie = WebUtils.getCookie(request, COOKIE_NAME);
        if (authorizationHeaderCookie == null)
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        String accessUUID = authorizationHeaderCookie.getValue();
        if (accessUUID == null)
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        Namespace ns = this.namespaceService.get(namespace);

        if (!ns.getAccessUUID().equals(accessUUID))
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        if (!ns.getUsers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toSet())
                .contains(username))
            throw new FunctionalException(
                    FunctionalRule.USER_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        User user = ns.getUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.USER_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND
                ));

        Optional<Score> scoreOptional = scoreRepository.findByUserAndNamespace(user, ns);

        if (scoreOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.SCORE_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        scoreRepository.delete(scoreOptional.get());
    }

    public Score incrementScore(String namespace, String username, Long increment, HttpServletRequest request)
            throws FunctionalException {
        Cookie authorizationHeaderCookie = WebUtils.getCookie(request, COOKIE_NAME);
        if (authorizationHeaderCookie == null)
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        String accessUUID = authorizationHeaderCookie.getValue();
        if (accessUUID == null)
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        Namespace ns = this.namespaceService.get(namespace);

        if (!ns.getAccessUUID().equals(accessUUID))
            throw new FunctionalException(
                    FunctionalRule.NS_UNAUTHORIZED.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );

        Optional<Namespace> namespaceOptional = namespaceRepository.findByName(namespace);

        if (namespaceOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        if (!namespaceOptional.get()
                .getUsers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toSet())
                .contains(username))
            throw new FunctionalException(
                    FunctionalRule.USER_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        User user = namespaceOptional.get()
                .getUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.USER_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND
                ));

        Optional<Score> scoreOptional = scoreRepository.findByUserAndNamespace(user, namespaceOptional.get());

        if (scoreOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.SCORE_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        scoreOptional.get().setScore(scoreOptional.get().getScore() + increment);
        scoreOptional.get().setUpdatedAt(Date.from(Instant.now()));
        return scoreRepository.save(scoreOptional.get());
    }
}
