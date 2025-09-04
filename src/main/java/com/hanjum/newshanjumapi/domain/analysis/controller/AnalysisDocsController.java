package com.hanjum.newshanjumapi.domain.analysis.controller;

import com.hanjum.newshanjumapi.domain.analysis.dto.AnalysisResponseDto;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Analysis API", description = "사용자 활동 분석 관련 API")
public interface AnalysisDocsController {

    @Operation(summary = "나의 관심사 분석 조회",
            description = "현재 로그인된 사용자의 활동(읽기 기록, 스크랩 등)을 분석하여 맞춤형 정보를 제공합니다.")
    ResponseEntity<ApiResponse<AnalysisResponseDto>> getMyAnalysis(
            @Parameter(hidden = true) String email // JWT 토큰에서 자동으로 주입되므로 Swagger 문서에서는 숨김 처리
    );
}
