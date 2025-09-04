package com.hanjum.newshanjumapi.domain.topic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadingHistoryDto {
    private Long articleId;
    private int readTimeSeconds;
}

