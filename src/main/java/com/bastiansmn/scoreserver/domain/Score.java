package com.bastiansmn.scoreserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long score_id;

    @Column(nullable = false, updatable = false)
    private Long score;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "link_namespace_score",
            joinColumns = @JoinColumn(name = "score_id"),
            inverseJoinColumns = @JoinColumn(name = "namespace_id")
    )
    @JsonIgnore
    private Namespace namespace;

    @OneToOne(
            fetch = FetchType.EAGER
    )
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Date.from(Instant.now());
        updatedAt = Date.from(Instant.now());
    }
}
