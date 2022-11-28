package com.bastiansmn.scoreserver.service;

import com.bastiansmn.scoreserver.domain.Score;
import com.bastiansmn.scoreserver.exception.FunctionalException;
import com.bastiansmn.scoreserver.exception.FunctionalRule;
import com.bastiansmn.scoreserver.repository.NamespaceRepository;
import com.bastiansmn.scoreserver.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final NamespaceRepository namespaceRepository;

    public Score create(String namespace, String user_uuid, Long score) throws FunctionalException {
        if (!this.namespaceRepository.existsByName(namespace))
            throw new FunctionalException(
                    FunctionalRule.NS_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND
            );

        Score scoreEntity = Score.builder()
                .userUUID(user_uuid)
                .score(score)
                .build();

        return scoreRepository.save(scoreEntity);
    }

    public Score getLastOfUser(String user_uuid) throws FunctionalException {
        if (!this.scoreRepository.existsByUserUUID(user_uuid))
            throw new FunctionalException(
                    FunctionalRule.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND
            );

        return this.scoreRepository.findFirstByUserUUIDOrderByCreatedAtDesc(user_uuid);
    }

    public List<Score> getAllOfUser(String user_uuid) throws FunctionalException {
        if (!this.scoreRepository.existsByUserUUID(user_uuid))
            throw new FunctionalException(
                    FunctionalRule.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND
            );

        return this.scoreRepository.findAllByUserUUIDOrderByCreatedAtAsc(user_uuid);
    }

    public void delete(Long score_id) {
        this.scoreRepository.deleteById(score_id);
    }

}
