package org.example.secu.service;

import lombok.RequiredArgsConstructor;
import org.example.secu.domain.dto.CustomUserDetails;
import org.example.secu.domain.dto.KakaoOAuth2DTO;
import org.example.secu.domain.entity.UserAccountEntity;
import org.example.secu.domain.repository.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // 1. 로그인
    // 2. 로그인 -> 실패 -> 계정 없음 -> 새롭게 생성

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User ou = super.loadUser(userRequest);
        // 상속 받은 원본(상위) 클래스에서 이미 구현된 loadUser로 OAuth2User
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // provider 이름
        Map<String, Object> attributes = ou.getAttributes();
        if (!"kakao".equals(registrationId)) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("unsupported_provider"),
                    "지원하지 않는 프로바이더: %s".formatted(registrationId)
            );
        }

        KakaoOAuth2DTO kakao;
        try {
            kakao = KakaoOAuth2DTO.from(attributes);
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_user_info_response"),
                    e.getMessage(),
                    e
            );
        }

        String providerId = kakao.id();
        String nickname = kakao.kakaoAccount().profile().nickname();
        UserAccountEntity userAccount = userAccountRepository
                .findBySocialIdAndSocialProvider(providerId, registrationId)
                .orElseGet(() -> createSocialUser(providerId, registrationId, nickname));

        return CustomUserDetails.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(
                        "ROLE_%s".formatted(userAccount.getRole().toUpperCase())
                )))
                .attributes(attributes)
                .build();
    }

    private UserAccountEntity createSocialUser(String providerId, String registrationId, String nickname) {
        String randomPassword = "{bcrypt}" + new BCryptPasswordEncoder()
                .encode(UUID.randomUUID().toString());
        UserAccountEntity user = UserAccountEntity.builder()
                .socialId(providerId)
                .socialProvider(registrationId)
                .username(nickname)
                .password(randomPassword)
                .role("user")
                .build();
        return userAccountRepository.save(user);
    }

    private final UserAccountRepository userAccountRepository;
}
