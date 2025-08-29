package com.hanjum.newshanjumapi.global.jwt.provider;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.chungnamthon.zeroroad.domain.member.entity.Role;
import org.chungnamthon.zeroroad.global.jwt.common.TokenExpiration;
import org.chungnamthon.zeroroad.global.jwt.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

import static org.chungnamthon.zeroroad.global.jwt.common.TokenType.ACCESS;
import static org.chungnamthon.zeroroad.global.jwt.common.TokenType.REFRESH;

@Slf4j
@Component
public class JwtProvider {

    private static final String CATEGORY_KEY = "category";
    private static final String AUTHORIZATION_KEY = "Authorization";

    private final SecretKey secretKey;

    public JwtProvider(@Value("${spring.jwt.secretKey}") String key) {
        this.secretKey = createSecretKey(key);
    }

    public TokenResponse createTokens(Long memberId, Role role) {
        return TokenResponse.builder()
                .accessToken(createAccessToken(memberId, role))
                .refreshToken(createRefreshToken())
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token);
        Role authority = getAuthority(token);
        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(authority.getValue()))
        );
    }

    public boolean isValidateToken(String token) {
        try {
            validateToken(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logTokenError("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다. Token", token);
        } catch (ExpiredJwtException e) {
            logTokenError("Expired JWT, 만료된 JWT 입니다. Token", token);
        } catch (UnsupportedJwtException e) {
            logTokenError("Unsupported JWT, 지원되지 않는 JWT 입니다. Token", token);
        } catch (IllegalArgumentException e) {
            logTokenError("JWT claims is empty, 잘못된 JWT 입니다. Token", token);
        }
        return false;
    }

    private SecretKey createSecretKey(String key) {
        return new SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    private String createAccessToken(Long memberId, Role role) {
        return Jwts.builder().signWith(this.secretKey)
                .subject(String.valueOf(memberId))
                .claim(CATEGORY_KEY, ACCESS.getValue())
                .claim(AUTHORIZATION_KEY, role)
                .expiration(createExpirationDate(TokenExpiration.ACCESS_TOKEN))
                .compact();
    }

    private String createRefreshToken() {
        return Jwts.builder().signWith(this.secretKey)
                .claim(CATEGORY_KEY, REFRESH.getValue())
                .expiration(createExpirationDate(TokenExpiration.REFRESH_TOKEN))
                .compact();
    }

    private Date createExpirationDate(TokenExpiration expiration) {
        Date date = new Date();
        return new Date(date.getTime() + expiration.getExpirationMs());
    }

    private void validateToken(String token) {
        Claims claims = getClaimsFromToken(token);
        validateExpiredToken(claims);
    }

    private Role getAuthority(String token) {
        Claims claims = getClaimsFromToken(token);
        return Role.valueOf(claims.get(AUTHORIZATION_KEY, String.class));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void validateExpiredToken(Claims claims) {
        if (claims.getExpiration().before(new Date())) {
            throw new ExpiredJwtException(null, claims, "Expired JWT, 만료된 JWT 입니다.");
        }
    }

    private void logTokenError(String message, String token) {
        log.warn("{}: {}", message, token);
    }

}