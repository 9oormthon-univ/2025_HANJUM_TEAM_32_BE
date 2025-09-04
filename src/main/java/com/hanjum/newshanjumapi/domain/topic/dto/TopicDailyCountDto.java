package com.hanjum.newshanjumapi.domain.topic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TopicDailyCountDto {
    private String topicName;
    private LocalDate date;
    private long count;
}