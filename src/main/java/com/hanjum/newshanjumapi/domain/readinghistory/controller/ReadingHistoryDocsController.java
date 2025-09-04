package com.hanjum.newshanjumapi.domain.readinghistory.controller;

import com.hanjum.newshanjumapi.domain.topic.dto.ReadingHistoryDto;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Reading History API", description = "사용자 기사 읽기 기록 관련 API")
public interface ReadingHistoryDocsController {

    @Operation(summary = "기사 읽기 기록 저장",
            description = "사용자가 특정 기사를 읽었음을 기록합니다. 이 데이터는 '나의 관심사 분석'에 사용됩니다.")
    ResponseEntity<ApiResponse<String>> recordReadingHistory(
            @Parameter(hidden = true) String email,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "읽은 기사 ID와 읽은 시간(초)") ReadingHistoryDto requestDto
    );
}
