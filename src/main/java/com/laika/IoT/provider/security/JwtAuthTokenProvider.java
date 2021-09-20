package com.laika.IoT.provider.security;

import com.laika.IoT.core.security.AuthTokenProvider;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class JwtAuthTokenProvider implements AuthTokenProvider<JwtAuthToken> {
    // 토큰 생성, 변환
    private final Key key;
    private static final String AUTHORIZATION_HEADER = "x-auth-token";

    public JwtAuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public JwtAuthToken createAuthToken(String id, String role, Date expiredDate) {
        return new JwtAuthToken(id, role, expiredDate, key);
    }

    @Override
    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    @Override
    public Optional<String> resolveToken(HttpServletRequest request) {
        //헤더에서 토큰 꺼내오기
        String authToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authToken)) {
            return Optional.of(authToken);
        } else {
            return Optional.empty();
        }
    }
}