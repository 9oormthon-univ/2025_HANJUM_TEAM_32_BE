package com.hanjum.newshanjumapi.domain.article.controller;

import com.hanjum.newshanjumapi.domain.article.dto.DailyNewsResponse;
import com.hanjum.newshanjumapi.domain.article.dto.PreferRequest;
import com.hanjum.newshanjumapi.domain.article.dto.PreferResponse;
import com.hanjum.newshanjumapi.domain.article.service.ArticleService;
import com.hanjum.newshanjumapi.domain.article.service.PreferenceService;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.service.MemberService;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleController {

    private final ArticleService articleService;
    private final PreferenceService preferenceService;
    private final MemberService memberService;

    @GetMapping("/daily-articles")
    public ResponseEntity<ApiResponse<DailyNewsResponse>> getOneByPage(@RequestParam(defaultValue = "1") int page) {
        DailyNewsResponse response = articleService.getDailyArticles(page);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

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
}

