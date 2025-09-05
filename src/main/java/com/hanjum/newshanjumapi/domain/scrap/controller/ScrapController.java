package com.hanjum.newshanjumapi.domain.scrap.controller;

import com.hanjum.newshanjumapi.domain.article.entity.Article;
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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scraps")
public class ScrapController implements ScrapDocsController{

    private final ScrapService scrapService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> saveScrap(
            @AuthenticationPrincipal String email,
            @RequestBody ScrapRequestDto requestDto
    ) {
        Long scrapId = scrapService.saveScrap(email, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(scrapId));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ScrapResponseDto>>> getScraps(
            @AuthenticationPrincipal String email,
            @RequestParam(value = "category", required = false) Optional<Article.Category> category,
            @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC, size = 5) Pageable pageable
    ) {
        Page<ScrapResponseDto> scraps = scrapService.getScraps(email, category, pageable);
        return ResponseEntity.ok(ApiResponse.onSuccess(scraps));
    }

    @Override
    @DeleteMapping("/{scrapId}")
    public ResponseEntity<ApiResponse<Void>> deleteScrap(
            @AuthenticationPrincipal String email,
            @PathVariable("scrapId") Long scrapId
    ) {
        scrapService.deleteScrap(email, scrapId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
