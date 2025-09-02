package com.hanjum.newshanjumapi.domain.scrap.controller;

import com.hanjum.newshanjumapi.domain.Article;
import com.hanjum.newshanjumapi.domain.scrap.dto.ScrapRequestDto;
import com.hanjum.newshanjumapi.domain.scrap.dto.ScrapResponseDto;
import com.hanjum.newshanjumapi.domain.scrap.service.ScrapService;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scraps")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> saveScrap(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestBody ScrapRequestDto requestDto
    ) {
        String email = getEmailFromOAuth2User(oauth2User);
        Long scrapId = scrapService.saveScrap(email, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(scrapId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ScrapResponseDto>>> getScraps(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestParam(value = "category", required = false) Optional<Article.Category> category,
            @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        String email = getEmailFromOAuth2User(oauth2User);
        Page<ScrapResponseDto> scraps = scrapService.getScraps(email, category, pageable);
        return ResponseEntity.ok(ApiResponse.onSuccess(scraps));
    }

    @DeleteMapping("/{scrapId}")
    public ResponseEntity<ApiResponse<Void>> deleteScrap(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @PathVariable("scrapId") Long scrapId
    ) {
        String email = getEmailFromOAuth2User(oauth2User);
        scrapService.deleteScrap(email, scrapId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    private String getEmailFromOAuth2User(OAuth2User oauth2User) {
        Map<String, Object> kakaoAccount = oauth2User.getAttribute("kakao_account");
        return (String) kakaoAccount.get("email");
    }
}