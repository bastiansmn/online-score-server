package com.bastiansmn.scoreserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "namespaces")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Namespace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long namespace_id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "namespace",
            orphanRemoval = true
    )
    private Set<Score> scores;

}
