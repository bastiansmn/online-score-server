package com.bastiansmn.scoreserver.repository;

import com.bastiansmn.scoreserver.domain.Namespace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, Long> {

    Optional<Namespace> findByName(String name);

    Boolean existsByName(String name);

}
