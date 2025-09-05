package com.hanjum.newshanjumapi.domain.article.controller;

import com.hanjum.newshanjumapi.domain.article.dto.ArticleResponse;
import com.hanjum.newshanjumapi.domain.article.dto.DailyNewsResponse;
import com.hanjum.newshanjumapi.domain.article.dto.PreferRequest;
import com.hanjum.newshanjumapi.domain.article.dto.PreferResponse;
import com.hanjum.newshanjumapi.domain.article.repository.ArticleDetailResponseDto;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Article API", description = "기사 조회 및 선호도 관련 API")
public interface ArticleDocsController {

    @Operation(summary = "기사 전체 목록 조회",
            description = "데이터베이스에 저장된 모든 기사의 목록을 조회합니다.")
    ResponseEntity<ApiResponse<List<ArticleResponse>>> getAllArticles();

    @Operation(summary = "기사 상세 조회",
            description = "기사 ID를 사용하여 특정 기사의 상세 내용을 조회합니다.")
    ResponseEntity<ApiResponse<ArticleDetailResponseDto>> getArticle(
            @Parameter(description = "조회할 기사의 ID") @PathVariable Long articleId
    );

    @Operation(summary = "오늘의 뉴스 조회",
            description = "매일 랜덤으로 선정된 5개의 뉴스 중 하나를 페이지 번호로 조회합니다.")
    ResponseEntity<ApiResponse<DailyNewsResponse>> getOneByPage(
            @Parameter(description = "페이지 번호 (1~5)") @RequestParam(defaultValue = "1") int page
    );

    @Operation(summary = "기사 선호도 설정 (로그인 필요)",
            description = "현재 로그인된 사용자가 특정 기사에 대한 선호도(LIKE/PASS)를 설정합니다.")
    ResponseEntity<ApiResponse<PreferResponse>> setPrefer(
            @Parameter(description = "선호도를 설정할 기사의 ID") @PathVariable Long articleId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "설정할 선호도 타입") @RequestBody PreferRequest request,
            @Parameter(hidden = true) String email
    );

}