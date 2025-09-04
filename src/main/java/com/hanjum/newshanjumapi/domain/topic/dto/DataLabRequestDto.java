package com.hanjum.newshanjumapi.domain.topic.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DataLabRequestDto {
    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<KeywordGroup> keywordGroups;

    @Getter
    @Builder
    public static class KeywordGroup {
        private String groupName;
        private List<String> keywords;
    }

    public static DataLabRequestDto buildForWeeklyTrend(String groupName, List<String> keywords) {
        DataLabRequestDto requestDto = new DataLabRequestDto();
        requestDto.startDate = LocalDate.now().minusWeeks(1).toString();
        requestDto.endDate = LocalDate.now().toString();
        requestDto.timeUnit = "date";
        requestDto.keywordGroups = List.of(
                KeywordGroup.builder()
                        .groupName(groupName)
                        .keywords(keywords)
                        .build()
        );
        return requestDto;
    }
}