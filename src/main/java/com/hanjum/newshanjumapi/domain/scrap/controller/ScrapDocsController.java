package com.hanjum.newshanjumapi.domain.scrap.controller;

import com.hanjum.newshanjumapi.domain.article.entity.Article;
import com.hanjum.newshanjumapi.domain.scrap.dto.ScrapRequestDto;
import com.hanjum.newshanjumapi.domain.scrap.dto.ScrapResponseDto;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Tag(name = "Scrap API", description = "스크랩 관련 API")
public interface ScrapDocsController {

    @Operation(summary = "뉴스 스크랩하기", description = "특정 회원이 기사를 스크랩북에 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스크랩 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (이미 스크랩된 기사, 존재하지 않는 회원/기사)"),
    })
    ResponseEntity<ApiResponse<Long>> saveScrap(
            @Parameter(description = "회원 ID") Long memberId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "스크랩할 기사 ID") ScrapRequestDto requestDto
    );

    @Operation(summary = "내 스크랩 목록 조회", description = "특정 회원의 스크랩 목록을 카테고리별, 페이징으로 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크랩 목록 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
    })
    ResponseEntity<ApiResponse<Page<ScrapResponseDto>>> getScraps(
            @Parameter(description = "회원 ID", required = true) @PathVariable("memberId") Long memberId,
            @Parameter(description = "카테고리 (AI, BUSINESS 등)", required = false) @RequestParam(value = "category", required = false) Optional<Article.Category> category,
            @Parameter(description = "페이징 정보") @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    );

    @Operation(summary = "스크랩 취소하기", description = "특정 회원의 특정 스크랩을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크랩 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스크랩을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "삭제 권한이 없음"),
    })
    ResponseEntity<ApiResponse<Void>> deleteScrap(
            @Parameter(description = "회원 ID") Long memberId,
            @Parameter(description = "삭제할 스크랩 ID") Long scrapId
    );
}