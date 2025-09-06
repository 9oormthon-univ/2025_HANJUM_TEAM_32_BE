package com.hanjum.newshanjumapi.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * 클라이언트의 모든 요청에 대해 JWT 토큰을 검증하는 필터입니다.
 * Key를 직접 주입받는 대신, 모든 JWT 관련 로직을 JwtTokenProvider에 위임하도록 수정되었습니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // --- [핵심 수정 사항] ---
        // Preflight 요청(OPTIONS)은 인증 필터를 통과시킵니다.
        // CORS 사전 요청은 인증 정보(JWT)를 담고 있지 않으므로, 여기서 토큰 검증 로직을 실행하면 안됩니다.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        // --- [여기까지 수정] ---

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰에서 인증 정보 조회
    private Authentication getAuthentication(String token) {
        Claims claims = jwtTokenProvider.getClaims(token);
        String email = claims.getSubject();

        return new UsernamePasswordAuthenticationToken(email, "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }
}


