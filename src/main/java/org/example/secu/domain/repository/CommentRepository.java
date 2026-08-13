package org.example.secu.domain.repository;

import org.example.secu.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("""
            SELECT c
            FROM CommentEntity c
            JOIN FETCH c.user
            ORDER BY c.id DESC
            """)
    List<CommentEntity> findAllWithUser();
//    @EntityGraph(attributePaths = "user")
//    List<CommentEntity> findAll();
}