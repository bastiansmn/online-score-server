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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final NamespaceRepository namespaceRepository;

    public Score create(String namespace, String user_uuid, Long score) throws FunctionalException {
        Optional<Namespace> namespaceOptional = namespaceRepository.findByName(namespace);

        if (namespaceOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        Score scoreEntity = Score.builder()
                .userUUID(user_uuid)
                .score(score)
                .namespace(namespaceOptional.get())
                .build();

        return scoreRepository.save(scoreEntity);
    }

    public Score getLastOfUser(String user_uuid, String namespace) throws FunctionalException {
        if (!this.scoreRepository.existsByUserUUID(user_uuid))
            throw new FunctionalException(
                    FunctionalRule.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND
            );

        Optional<Namespace> namespaceOptional = namespaceRepository.findByName(namespace);

        if (namespaceOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        return this.scoreRepository.findFirstByUserUUIDAndNamespaceOrderByCreatedAtDesc(user_uuid,namespaceOptional.get());
    }

    public List<Score> getAllOfUser(String user_uuid, String namespace) throws FunctionalException {
        if (!this.scoreRepository.existsByUserUUID(user_uuid))
            throw new FunctionalException(
                    FunctionalRule.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND
            );

        Optional<Namespace> namespaceOptional = namespaceRepository.findByName(namespace);

        if (namespaceOptional.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        return this.scoreRepository.findAllByUserUUIDAndNamespaceOrderByCreatedAtAsc(user_uuid, namespaceOptional.get());
    }

    public void delete(Long score_id) throws FunctionalException {
        if (!this.scoreRepository.existsById(score_id))
            throw new FunctionalException(
                    FunctionalRule.SCORE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND
            );
        this.scoreRepository.deleteById(score_id);
    }

}
