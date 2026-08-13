package org.example.secu.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "comment")
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Hibernate
@Getter
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccountEntity user;
}