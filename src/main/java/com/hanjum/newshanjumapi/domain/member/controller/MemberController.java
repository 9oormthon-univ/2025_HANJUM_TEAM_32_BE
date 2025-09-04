package com.hanjum.newshanjumapi.domain.member.controller;

import com.hanjum.newshanjumapi.domain.member.dto.MemberResponseDto;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.service.MemberService;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberDocsController{

    private final MemberService memberService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponseDto>> getMyInfo(
            @AuthenticationPrincipal String email
    ) {
        Member member = memberService.findByEmail(email);
        MemberResponseDto responseDto = new MemberResponseDto(member);
        return ResponseEntity.ok(ApiResponse.onSuccess(responseDto));
    }
}

