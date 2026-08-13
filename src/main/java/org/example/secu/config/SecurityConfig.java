package org.example.secu.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Configuration
public class SecurityConfig {
    // 1. password encoder
    @Bean
    public PasswordEncoder passwordEncoder(
            @Validated @NotBlank @Value("${app.security.encoding-id}") String encodingId) {
        // bcrypt
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        Map<String, PasswordEncoder> enc = Map.of(
                "bcrypt", new BCryptPasswordEncoder(),
                "scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8(),
                "argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        return new DelegatingPasswordEncoder(encodingId, enc);
    }

    // 2. filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .authorizeHttpRequests(
                        auth -> auth
//                                .requestMatchers("/").permitAll()
//                                .requestMatchers("/signup").permitAll()
                                .requestMatchers("/css/**", "/error/**").permitAll()
                                .requestMatchers("/", "/signup").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .oauth2Login(o -> o
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }
}