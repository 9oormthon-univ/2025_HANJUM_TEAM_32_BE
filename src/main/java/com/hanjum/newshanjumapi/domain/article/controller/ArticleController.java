package com.hanjum.newshanjumapi.domain.article.controller;

import com.hanjum.newshanjumapi.domain.article.dto.ArticleResponse;
import com.hanjum.newshanjumapi.domain.article.dto.DailyNewsResponse;
import com.hanjum.newshanjumapi.domain.article.dto.PreferRequest;
import com.hanjum.newshanjumapi.domain.article.dto.PreferResponse;
import com.hanjum.newshanjumapi.domain.article.entity.Article;
import com.hanjum.newshanjumapi.domain.article.repository.ArticleDetailResponseDto;
import com.hanjum.newshanjumapi.domain.article.service.ArticleService;
import com.hanjum.newshanjumapi.domain.article.service.PreferenceService;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.service.MemberService;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleController implements ArticleDocsController {

    private final ArticleService articleService;
    private final PreferenceService preferenceService;
    private final MemberService memberService;

    @Override
    @GetMapping("/daily-articles")
    public ResponseEntity<ApiResponse<DailyNewsResponse>> getOneByPage(@RequestParam(defaultValue = "1") int page) {
        DailyNewsResponse response = articleService.getDailyArticles(page);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Override
    @PostMapping("/articles/{articleId}/preference")
    public ResponseEntity<ApiResponse<PreferResponse>> setPrefer(
            @PathVariable Long articleId,
            @RequestBody PreferRequest request,
            @AuthenticationPrincipal String email) {

        Member member = memberService.findByEmail(email);
        Long memberId = member.getId();

        PreferResponse res = preferenceService.setPreference(memberId, articleId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(res));
    }

    @Override
    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailResponseDto>> getArticle(
            @PathVariable Long articleId) {

        ArticleDetailResponseDto response = articleService.getArticleById(articleId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Override
    @GetMapping("/articles")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> getAllArticles() {
        List<ArticleResponse> response = articleService.getAllArticles();
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "수동으로 뉴스 크롤링 실행 (테스트용)",
            description = "네이버 뉴스 크롤링을 즉시 실행하여 5개의 새 기사를 DB에 저장합니다.")
    @PostMapping("/articles/crawl")
    public ResponseEntity<String> manuallyCrawlArticles() {
        // createDailyArticles 메서드는 생성된 기사 목록을 반환합니다.
        List<Article> createdArticles = articleService.createDailyArticles();
        return ResponseEntity.ok("크롤링 성공! 새로 생성된 기사 " + createdArticles.size() + "개.");
    }
}

