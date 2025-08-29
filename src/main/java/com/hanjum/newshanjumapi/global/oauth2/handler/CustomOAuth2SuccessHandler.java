package com.hanjum.newshanjumapi.global.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chungnamthon.zeroroad.domain.member.entity.Role;
import org.chungnamthon.zeroroad.domain.member.service.MemberService;
import org.chungnamthon.zeroroad.global.jwt.dto.TokenResponse;
import org.chungnamthon.zeroroad.global.jwt.provider.JwtProvider;
import org.chungnamthon.zeroroad.global.oauth2.dto.CustomOAuth2Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static org.chungnamthon.zeroroad.global.jwt.common.TokenType.ACCESS;
import static org.chungnamthon.zeroroad.global.jwt.common.TokenType.REFRESH;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();
        TokenResponse tokenResponse = jwtProvider.createTokens(oAuth2Member.getMemberId(), oAuth2Member.getRole());

        memberService.updateRefreshToken(oAuth2Member.getMemberId(), tokenResponse.refreshToken());
        log.info("소셜 로그인 성공: {} ", oAuth2Member.getName());

        String redirectUrl = createRedirectUrl(tokenResponse, oAuth2Member.getRole());

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String createRedirectUrl(TokenResponse tokenResponse, Role role) {
        String path = role.equals(Role.GUEST) ? "/info" : "/";

        // TODO: 배포 시 url 수정 예정
        return UriComponentsBuilder.fromUriString("http://localhost:5173" + path)
                .queryParam(ACCESS.getValue(), tokenResponse.accessToken())
                .queryParam(REFRESH.getValue(), tokenResponse.refreshToken())
                .build()
                .toUriString();
    }

}