package org.example.secu.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "user_account")
@Entity // 뒤에 entity 등이 붙어서 테이블 이름이 부자연스러운 경우 table로 먼저 이름을 지정
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAccountEntity {
    @Id
    // 어차피 PostgreSQL로 쓰면 AUTO로 해도 괜찮은데... MySQL과 호환성 고려하면 IDENTITY하는 걸 권장
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 로그인용이 아니라 식별용
    @Column(unique = true, nullable = false)
    private String username; // 중복되면 곤란
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    // SocialLogin
    private String socialProvider; // kakao, google
    private String socialId;
}