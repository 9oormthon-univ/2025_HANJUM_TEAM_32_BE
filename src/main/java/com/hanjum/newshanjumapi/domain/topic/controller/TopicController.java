package com.hanjum.newshanjumapi.domain.topic.controller;

import com.hanjum.newshanjumapi.domain.topic.dto.*;
import com.hanjum.newshanjumapi.domain.topic.service.TopicService;
import com.hanjum.newshanjumapi.global.exception.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/topics")
public class TopicController implements TopicDocsController{

    private final TopicService topicService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicResponseDto>>> getAllTopics() {
        return ResponseEntity.ok(ApiResponse.onSuccess(topicService.getAllTopics()));
    }

    @Override
    @PostMapping("/me")
    public ResponseEntity<ApiResponse<String>> saveMyTopics(
            @AuthenticationPrincipal String email,
            @RequestBody TopicRequestDto requestDto
    ) {
        topicService.saveMemberTopics(email, requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("관심 토픽이 성공적으로 저장되었습니다."));
    }

    @Override
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<PopularTopicDto>>> getPopularTopics() {
        return ResponseEntity.ok(ApiResponse.onSuccess(topicService.getPopularTopics()));
    }

    @Override
    @GetMapping("/trends/weekly")
    public ResponseEntity<ApiResponse<List<TopicTrendDto>>> getWeeklyTrends() {
        return ResponseEntity.ok(ApiResponse.onSuccess(topicService.getRealtimeTopicTrends()));
    }
}

