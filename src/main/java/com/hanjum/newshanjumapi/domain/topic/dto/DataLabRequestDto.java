package com.hanjum.newshanjumapi.domain.topic.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DataLabRequestDto {
    private String startDate;
    private String endDate;
    private String timeUnit; // date, week, month
    private List<KeywordGroup> keywordGroups;

    @Getter
    @Builder
    public static class KeywordGroup {
        private String groupName;
        private List<String> keywords;
    }

    // 서비스 요구사항에 맞게 요청 DTO를 생성하는 정적 메서드
    public static DataLabRequestDto buildForWeeklyTrend(String groupName, List<String> keywords) {
        DataLabRequestDto requestDto = new DataLabRequestDto();
        requestDto.startDate = LocalDate.now().minusWeeks(1).toString();
        requestDto.endDate = LocalDate.now().toString();
        requestDto.timeUnit = "date"; // 일간 트렌드
        requestDto.keywordGroups = List.of(
                KeywordGroup.builder()
                        .groupName(groupName)
                        .keywords(keywords)
                        .build()
        );
        return requestDto;
    }
}