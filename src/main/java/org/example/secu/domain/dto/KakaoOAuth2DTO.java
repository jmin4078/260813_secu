package org.example.secu.domain.dto;

import java.util.Map;

public record KakaoOAuth2DTO(String id, KakaoAccount kakaoAccount) {
    public static KakaoOAuth2DTO from(Map<String, Object> attributes) {
        Object id = attributes.get("id");
        Map<String, Object> account = asMap(attributes.get("kakao_account"), "kakao_account");
        Map<String, Object> profile = asMap(account.get("profile"), "kakao_account.profile");
        Object nickname = profile.get("nickname");

        if (id == null || nickname == null || nickname.toString().isBlank()) {
            throw new IllegalArgumentException("카카오 사용자 정보에 id 또는 nickname이 없습니다.");
        }

        return new KakaoOAuth2DTO(
                id.toString(),
                new KakaoAccount(new Profile(nickname.toString()))
        );
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value, String fieldName) {
        if (!(value instanceof Map<?, ?>)) {
            throw new IllegalArgumentException("카카오 사용자 정보에 %s가 없습니다.".formatted(fieldName));
        }
        return (Map<String, Object>) value;
    }

    public record KakaoAccount(Profile profile) {
    }

    public record Profile(String nickname) {
    }
}
