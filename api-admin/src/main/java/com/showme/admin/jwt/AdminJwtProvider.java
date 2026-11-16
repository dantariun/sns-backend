package com.showme.admin.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class AdminJwtProvider {

    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_ROLE = "role";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    private final SecretKey secretKey;
    @Getter
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;

    public AdminJwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiry}") long accessTokenExpiry,
            @Value("${jwt.refresh-token-expiry}") long refreshTokenExpiry) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    public String generateAccessToken(Long adminId, String authority) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(adminId))
                .claim(CLAIM_TYPE, TYPE_ACCESS)
                .claim(CLAIM_ROLE, authority)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenExpiry))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long adminId) {
        Date now = new Date();
        // iat 이 초 단위라 같은 초에 재발급하면 동일 토큰이 생기므로 jti 로 구분
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(String.valueOf(adminId))
                .claim(CLAIM_TYPE, TYPE_REFRESH)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenExpiry))
                .signWith(secretKey)
                .compact();
    }

    public boolean isValidAccessToken(String token) {
        try {
            return TYPE_ACCESS.equals(parseClaims(token).get(CLAIM_TYPE, String.class));
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("유효하지 않은 JWT: {}", e.getMessage());
            return false;
        }
    }

    public Long extractAdminId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    public String extractRole(String token) {
        return parseClaims(token).get(CLAIM_ROLE, String.class);
    }

    public LocalDateTime extractExpiration(String token) {
        return parseClaims(token).getExpiration().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
