package org.example.secu.service;


import lombok.RequiredArgsConstructor;
import org.example.secu.domain.entity.CommentEntity;
import org.example.secu.domain.entity.UserAccountEntity;
import org.example.secu.domain.repository.CommentRepository;
import org.example.secu.domain.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserAccountRepository userAccountRepository;
    // user보다는 owner, writer

    @Transactional
    public void create(long userId, String content) {
        UserAccountEntity user = userAccountRepository
                .findById(userId).orElseThrow();
        CommentEntity comment = CommentEntity.builder()
                .user(user)
                .content(content)
                .build();
        commentRepository.save(comment);
    }
    public List<CommentEntity> findAll() {
//        return commentRepository.findAll();
        return commentRepository.findAllWithUser();
    }
}