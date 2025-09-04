package com.hanjum.newshanjumapi.domain.topic.controller;


import com.hanjum.newshanjumapi.domain.topic.dto.PopularTopicDto;
import com.hanjum.newshanjumapi.domain.topic.dto.TopicRequestDto;
import com.hanjum.newshanjumapi.domain.topic.dto.TopicResponseDto;
import com.hanjum.newshanjumapi.domain.topic.dto.TopicTrendDto;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

@Tag(name = "Topic API", description = "토픽 관련 API")
public interface TopicDocsController {

    @Operation(summary = "전체 토픽 목록 조회", description = "사용자가 선택할 수 있는 모든 관심 토픽의 목록을 조회합니다.")
    ResponseEntity<ApiResponse<List<TopicResponseDto>>> getAllTopics();

    @Operation(summary = "인기 토픽 TOP 5 조회", description = "가장 많은 사용자가 선택한 인기 토픽 5개를 조회합니다.")
    ResponseEntity<ApiResponse<List<PopularTopicDto>>> getPopularTopics();

    @Operation(summary = "주간 토픽 트렌드 조회", description = "최근 일주일간의 토픽별 트렌드(검색량 기반)를 조회합니다.")
    ResponseEntity<ApiResponse<List<TopicTrendDto>>> getWeeklyTrends();

    @Operation(summary = "내 관심 토픽 저장", description = "현재 로그인된 사용자의 관심 토픽을 저장/수정합니다.")
    ResponseEntity<ApiResponse<String>> saveMyTopics(
            @Parameter(hidden = true) OAuth2User oauth2User,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "선택한 토픽 ID 리스트") TopicRequestDto requestDto
    );
}