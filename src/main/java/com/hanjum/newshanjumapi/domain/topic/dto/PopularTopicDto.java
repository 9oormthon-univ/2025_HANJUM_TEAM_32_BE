package com.hanjum.newshanjumapi.domain.topic.dto;


import lombok.Getter;

@Getter
public class PopularTopicDto {

    private final int rank;
    private final Long topicId;
    private final String name;

    public PopularTopicDto(int rank, Long topicId, String name) {
        this.rank = rank;
        this.topicId = topicId;
        this.name = name;

    }

}