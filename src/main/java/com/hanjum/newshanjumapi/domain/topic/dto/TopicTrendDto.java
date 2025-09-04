package com.hanjum.newshanjumapi.domain.topic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class TopicTrendDto {
    private int rank;
    private String topicName;
    private List<DataPoint> dataPoints;

    @Getter
    @AllArgsConstructor
    public static class DataPoint {
        private LocalDate date;
        private long count;
    }
}
