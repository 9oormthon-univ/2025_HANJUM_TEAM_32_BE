package com.hanjum.newshanjumapi.domain.topic.dto;

import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import lombok.Getter;

@Getter
public class TopicResponseDto {
    private final Long topicId;
    private final String name;

    public TopicResponseDto(Topic topic) {
        this.topicId = topic.getId();
        this.name = topic.getName();
    }
}
