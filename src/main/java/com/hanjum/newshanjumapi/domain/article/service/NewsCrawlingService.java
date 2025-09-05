package com.hanjum.newshanjumapi.domain.article.service;

import com.hanjum.newshanjumapi.domain.article.dto.NaverNewsResponse;
import com.hanjum.newshanjumapi.domain.article.entity.Article;
import com.hanjum.newshanjumapi.domain.article.repository.ArticleRepository;
import com.hanjum.newshanjumapi.domain.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class NewsCrawlingService {

    private final ArticleRepository articleRepository;
    private final GptService gptService;

    @Qualifier("naverWebClient")
    private final WebClient naverWebClient;

    @Transactional
    public void crawlAndSaveNaverNews() {
        NaverNewsResponse response = naverWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/news.json")
                        .queryParam("query", "IT") // 검색어
                        .queryParam("display", 10) // 가져올 기사 수
                        .queryParam("sort", "sim")   // 관련도순 (date: 최신순)
                        .build())
                .retrieve()
                .bodyToMono(NaverNewsResponse.class)
                .block();

        if (response != null && response.items() != null) {
            for (NaverNewsResponse.Item item : response.items()) {
                if (item.link().contains("naver.com")) {
                    if (!articleRepository.existsByArticleUrl(item.originallink())) {

                        String summaryForGpt = removeHtmlTags(item.title()) + ". " + removeHtmlTags(item.description());
                        String commentary = gptService.generateNewsCommentary(summaryForGpt);

                        Article article = Article.builder()
                                .title(removeHtmlTags(item.title()))
                                .description(removeHtmlTags(item.description()))
                                .articleUrl(item.originallink())
                                .pubDate(parseNaverDate(item.pubDate()))
                                .aiCommentary(commentary)
                                // TODO: category, topic, imageUrl 등은 추가적인 로직으로 채워야 합니다.
                                .build();

                        articleRepository.save(article);
                    }
                }
            }
        }
    }

    private LocalDateTime parseNaverDate(String pubDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        return LocalDateTime.parse(pubDate, formatter);
    }

    private String removeHtmlTags(String text) {
        return text.replaceAll("<[^>]*>", "").replace("&quot;", "\"");
    }
}
