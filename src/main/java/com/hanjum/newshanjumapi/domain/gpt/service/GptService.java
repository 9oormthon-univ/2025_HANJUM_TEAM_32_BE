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

    private static final String COMMENTARY_PROMPT_TEMPLATE = """
            너는 친절하고 통찰력 있는 IT 전문기자야.
            20-30대 직장인이 아래 뉴스 요약을 쉽게 이해할 수 있도록 다음 두 가지를 반드시 포함해서 3~4문장으로 설명해줘:
            1. '이 뉴스가 왜 중요한지' (핵심 가치 또는 영향력)
            2. '이해를 돕는 핵심 배경지식' (관련 기술 또는 시장 상황)
            
            [뉴스 요약]
            %s
            """;
    public String generateNewsCommentary(String newsSummary) {
        if (newsSummary == null || newsSummary.isBlank()) {
            return "해설을 생성할 수 없습니다.";
        }

        String finalPrompt = String.format(COMMENTARY_PROMPT_TEMPLATE, newsSummary);

        return getGptResponse(finalPrompt);
    }

    public String generateTopicRecommendation(String category) {
        String prompt = String.format(
                "%s 분야에 대한 사용자의 관심을 바탕으로, 함께 관심을 가질만한 관련 토픽 2가지를 '토픽1, 토픽2' 형식으로만 간결하게 추천해줘.",
                category
        );

        return getGptResponse(prompt);
    }

    private String getGptResponse(String prompt) {
        GptDto.GptRequest requestBody = new GptDto.GptRequest(
                "gpt-5",
                List.of(new GptDto.Message("user", prompt))
        );

        try {
            GptDto.GptResponse gptResponse = gptWebClient.post()
                    .uri("/v1/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(GptDto.GptResponse.class)
                    .block();

            if (gptResponse != null && !gptResponse.choices().isEmpty()) {
                return gptResponse.choices().get(0).message().content();
            }
        } catch (Exception e) {

            return "오류: AI 해설을 가져오는 데 실패했습니다.";
        }

        return "생성된 AI 해설이 없습니다.";
    }
}