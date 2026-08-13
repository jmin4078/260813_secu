package org.example.secu.domain.dto;


import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

@Getter
@Builder
public class CustomUserDetails implements UserDetails {
    private final List<SimpleGrantedAuthority> authorities;
    private final long id;
    private final String username;
    private final String password;
}