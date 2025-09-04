package com.hanjum.newshanjumapi.domain.topic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 응답의 모든 필드를 매핑하지 않을 경우 사용
public class DataLabResponseDto {
    private List<Result> results;

    @Getter
    @NoArgsConstructor
    public static class Result {
        private String title;
        private List<String> keywords;
        private List<DataPoint> data;
    }

    @Getter
    @NoArgsConstructor
    public static class DataPoint {
        private String period; // 날짜 (예: "2025-09-01")
        private Double ratio;  // 검색량 상대적 비율
    }
}