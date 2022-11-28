package com.bastiansmn.scoreserver.service;

import com.bastiansmn.scoreserver.domain.Namespace;
import com.bastiansmn.scoreserver.domain.Score;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.exception.FunctionalRule;
import com.bastiansmn.scoreserver.repository.NamespaceRepository;
import com.bastiansmn.scoreserver.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NamespaceService {

    private final NamespaceRepository namespaceRepository;
    private final ScoreRepository scoreRepository;

    public Namespace create(String name) throws FunctionalException {
        if (this.namespaceRepository.existsByName(name))
            throw new FunctionalException(FunctionalRule.NS_ALREADY_EXISTS.getMessage(), HttpStatus.BAD_REQUEST
            );

        Namespace namespace = Namespace.builder()
                .name(name)
                .build();

        return namespaceRepository.save(namespace);
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
        // Get all scores of the namespace
        Namespace namespace = this.get(name);
        System.out.println(namespace.getScores());
        return scoreRepository.findAll();
    }

    public void delete(String name) throws FunctionalException {
        namespaceRepository.delete(this.get(name));
    }

}
