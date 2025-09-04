package com.hanjum.newshanjumapi.domain.gpt.service;

import com.hanjum.newshanjumapi.domain.gpt.dto.GptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GptService {

    @Qualifier("gptWebClient")
    private final WebClient gptWebClient;

    public String getGptResponse(String prompt) {
        GptDto.GptRequest requestBody = new GptDto.GptRequest(
                "gpt-5",
                List.of(new GptDto.Message("user", prompt))
        );

        GptDto.GptResponse gptResponse = gptWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GptDto.GptResponse.class)
                .block();

        if (gptResponse != null && !gptResponse.choices().isEmpty()) {
            return gptResponse.choices().get(0).message().content();
        }

        return "응답 생성에 실패했습니다.";
    }
}

