package org.example.secu.service;

import lombok.RequiredArgsConstructor;
import org.example.secu.domain.dto.CustomUserDetails;
import org.example.secu.domain.entity.UserAccountEntity;
import org.example.secu.domain.repository.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 존재하는지
        UserAccountEntity entity = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "%s 유저는 없습니다".formatted(username)));
        // 2. db(entity) -> UserDetails(security)
        return CustomUserDetails.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword()) // ? -> 저장될 때 인코딩 되서 저장됨
                // 알아서 security가 매칭 여부를 판단
                // .role("user")
                .authorities(List.of(new SimpleGrantedAuthority(
                        "ROLE_%s".formatted(entity.getRole().toUpperCase())
                        // user -> ROLE_USER
                )))
                .build();
    }
}