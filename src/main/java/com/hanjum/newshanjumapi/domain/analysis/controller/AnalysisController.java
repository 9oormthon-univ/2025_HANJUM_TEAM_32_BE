package com.hanjum.newshanjumapi.domain.analysis.controller;

import com.hanjum.newshanjumapi.domain.analysis.dto.AnalysisResponseDto;
import com.hanjum.newshanjumapi.domain.analysis.service.AnalysisService;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
public class AnalysisController implements AnalysisDocsController{

    private final AnalysisService analysisService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AnalysisResponseDto>> getMyAnalysis(
            @AuthenticationPrincipal String email
    ) {
        AnalysisResponseDto analysisResult = analysisService.getAnalysisForUser(email);
        return ResponseEntity.ok(ApiResponse.onSuccess(analysisResult));
    }
}
