package com.hanjum.newshanjumapi.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResponseDto {
    private String mostViewedCategory;
    private int percentage;
    private String averageReadTime;
    private int weeklyScrapCount;
    private String recommendationMessage;

}

