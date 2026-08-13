package org.example.secu.service;

import lombok.RequiredArgsConstructor;
import org.example.secu.controller.UserAccountController;
import org.example.secu.domain.entity.UserAccountEntity;
import org.example.secu.domain.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void create(UserAccountController.UserForm form) {
        // exist로 겹침을 검사를 해줘야하는데... 생략
        String encoded = passwordEncoder.encode(form.password());
        UserAccountEntity user = UserAccountEntity.builder()
                .username(form.username())
                .password(encoded) // DB에 패스워드를 그냥 저장하면 바로 법 위반
                .role("user")
                .build();
        userAccountRepository.save(user);
    }
}