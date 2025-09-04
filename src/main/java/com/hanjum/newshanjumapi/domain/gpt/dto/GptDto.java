package com.hanjum.newshanjumapi.domain.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GptDto {

    public record GptRequest(
            String model,
            List<Message> messages
    ) {}


    public record GptResponse(
            List<Choice> choices
    ) {}

    public record Message(
            String role,
            String content
    ) {}

    public record Choice(
            Message message
    ) {}

    public record SummaryResponse(
            @JsonProperty("summary")
            String summary
    ) {}
}

