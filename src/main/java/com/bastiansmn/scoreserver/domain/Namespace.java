package com.bastiansmn.scoreserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

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

    @Column(nullable = false)
    @JsonIgnore
    private String accessUUID;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "namespace_users",
            joinColumns = @JoinColumn(name = "namespace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    @PrePersist
    protected void onCreate() {
        this.accessUUID = UUID.randomUUID().toString();
    }

}
