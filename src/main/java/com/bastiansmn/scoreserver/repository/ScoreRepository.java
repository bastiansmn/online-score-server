package com.bastiansmn.scoreserver.repository;

import com.bastiansmn.scoreserver.domain.Namespace;
import com.bastiansmn.scoreserver.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Boolean existsByUserUUID(String user_uuid);

    Score findFirstByUserUUIDAndNamespaceOrderByCreatedAtDesc(String user_uuid, Namespace namespace);

    List<Score> findAllByUserUUIDAndNamespaceOrderByCreatedAtAsc(String user_uuid, Namespace namespace);

}
