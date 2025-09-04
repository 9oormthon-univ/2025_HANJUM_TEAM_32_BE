package com.hanjum.newshanjumapi.domain.member.controller;

import com.hanjum.newshanjumapi.domain.member.dto.MemberResponseDto;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member API", description = "회원 정보 관련 API (로그인 필요)")
public interface MemberDocsController {

    @Operation(summary = "내 정보 조회",
            description = "현재 로그인된 사용자의 상세 정보(ID, 이름, 이메일, 역할)를 조회합니다.")
    ResponseEntity<ApiResponse<MemberResponseDto>> getMyInfo(
            @Parameter(hidden = true) String email
    );
}
