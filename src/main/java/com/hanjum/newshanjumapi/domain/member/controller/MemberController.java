package com.hanjum.newshanjumapi.domain.member.controller;

import com.hanjum.newshanjumapi.domain.member.dto.MemberResponseDto;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.service.MemberService;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponseDto>> getMyInfo(
            @AuthenticationPrincipal OAuth2User oauth2User
    ) {
        Map<String, Object> kakaoAccount = oauth2User.getAttribute("kakao_account");
        String email = (String) kakaoAccount.get("email");

        Member member = memberService.findByEmail(email);

        MemberResponseDto responseDto = new MemberResponseDto(member);
        return ResponseEntity.ok(ApiResponse.onSuccess(responseDto));
    }
}