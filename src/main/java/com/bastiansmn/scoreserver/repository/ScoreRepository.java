package com.bastiansmn.scoreserver.repository;

import com.bastiansmn.scoreserver.domain.Namespace;
import com.bastiansmn.scoreserver.domain.Score;
import com.bastiansmn.scoreserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Optional<Score> findByUserAndNamespace(User user, Namespace namespace);

    List<Score> findAllByNamespace(Namespace namespace);

}
