package com.hanjum.newshanjumapi.domain.topic.controller;

import com.hanjum.newshanjumapi.domain.topic.dto.TopicResponseDto;
import com.hanjum.newshanjumapi.domain.topic.dto.TopicRequestDto;
import com.hanjum.newshanjumapi.domain.topic.service.TopicService;
import com.hanjum.newshanjumapi.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<List<TopicResponseDto>>> getAllTopics() {
        List<TopicResponseDto> topics = topicService.getAllTopics();
        return ResponseEntity.ok(ApiResponse.onSuccess(topics));
    }

    //    @PostMapping("/members/topics")
//    public ResponseEntity<ApiResponse<String>> saveMemberTopics(
//            @AuthMember Member member,
//            @RequestBody TopicsRequestDto requestDto
//    ) {
//        topicService.saveMemberTopics(member.getId(), requestDto);
//        return ResponseEntity.ok(ApiResponse.onSuccess("관심 토픽이 성공적으로 저장되었습니다."));
//    }

    @PostMapping("/members/{memberId}/topics")
    public ResponseEntity<ApiResponse<String>> saveMemberTopics(
            @PathVariable("memberId") Long memberId,
            @RequestBody TopicRequestDto requestDto
    ) {
        topicService.saveMemberTopics(memberId, requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("관심 토픽이 성공적으로 저장되었습니다."));
    }
}