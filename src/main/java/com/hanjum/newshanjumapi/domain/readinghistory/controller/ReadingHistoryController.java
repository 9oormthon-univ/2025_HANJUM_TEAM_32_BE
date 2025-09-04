package com.hanjum.newshanjumapi.domain.readinghistory.controller;

import com.hanjum.newshanjumapi.domain.readinghistory.service.ReadingHistoryService;
import com.hanjum.newshanjumapi.domain.topic.dto.ReadingHistoryDto;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class ReadingHistoryController implements ReadingHistoryDocsController{

    private final ReadingHistoryService readingHistoryService;

    @Override
    @PostMapping("/read")
    public ResponseEntity<ApiResponse<String>> recordReadingHistory(
            @AuthenticationPrincipal String email,
            @RequestBody ReadingHistoryDto requestDto
    ) {
        readingHistoryService.saveReadingHistory(email, requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("읽기 기록이 저장되었습니다."));
    }
}