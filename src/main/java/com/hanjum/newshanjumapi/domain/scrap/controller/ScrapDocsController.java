package com.hanjum.newshanjumapi.domain.scrap.controller;

import com.hanjum.newshanjumapi.domain.article.entity.Article;
import com.hanjum.newshanjumapi.domain.scrap.dto.ScrapRequestDto;
import com.hanjum.newshanjumapi.domain.scrap.dto.ScrapResponseDto;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Tag(name = "Scrap API", description = "스크랩 관련 API (로그인 필요)")
public interface ScrapDocsController {

    @Operation(summary = "뉴스 스크랩하기", description = "현재 로그인된 사용자가 특정 기사를 스크랩합니다.")
    ResponseEntity<ApiResponse<Long>> saveScrap(
            @Parameter(hidden = true) String email,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "스크랩할 기사 ID") ScrapRequestDto requestDto
    );

    @Operation(summary = "내 스크랩 목록 조회", description = "현재 로그인된 사용자의 스크랩 목록을 조회합니다.")
    ResponseEntity<ApiResponse<Page<ScrapResponseDto>>> getScraps(
            @Parameter(hidden = true) String email,
            @Parameter(description = "카테고리 필터링 (AI, POLITICS 등)") Optional<Article.Category> category,
            @Parameter(description = "페이징 정보") Pageable pageable
    );

    @Operation(summary = "스크랩 취소하기", description = "현재 로그인된 사용자가 자신의 스크랩을 삭제합니다.")
    ResponseEntity<ApiResponse<Void>> deleteScrap(
            @Parameter(hidden = true) String email,
            @Parameter(description = "삭제할 스크랩의 ID") Long scrapId
    );
}
