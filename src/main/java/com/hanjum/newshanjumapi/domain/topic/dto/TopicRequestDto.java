package com.hanjum.newshanjumapi.domain.topic.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicRequestDto {
    private List<Long> topicIds;
}