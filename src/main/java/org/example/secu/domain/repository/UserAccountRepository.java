package org.example.secu.domain.repository;

import org.example.secu.domain.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
    Optional<UserAccountEntity> findByUsername(String username);

    // Social
    Optional<UserAccountEntity> findBySocialIdAndSocialProvider(String socialId, String socialProvider);
}