package com.hanjum.newshanjumapi.domain.article.service;

import com.hanjum.newshanjumapi.domain.article.dto.ArticleResponse;
import com.hanjum.newshanjumapi.domain.article.dto.DailyNewsResponse;
import com.hanjum.newshanjumapi.domain.article.dto.NaverNewsResponse;
import com.hanjum.newshanjumapi.domain.article.entity.Article;
import com.hanjum.newshanjumapi.domain.article.repository.ArticleRepository;
import com.hanjum.newshanjumapi.domain.article.util.DateTimeUtil;
import com.hanjum.newshanjumapi.domain.article.util.ThumbnailExtractor;
import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import com.hanjum.newshanjumapi.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TopicRepository topicRepository;

    @Qualifier("naverWebClient")
    private final WebClient naverWebClient;

    @Transactional
    @Cacheable(value = "dailyArticles", key = "#root.target.getCacheKey()")
    public List<ArticleResponse> getDailyArticlesCacheable() {
        LocalDate today = LocalDate.now();
        List<Article> articles = articleRepository.findBySelectedAtDate(today);
        if (articles.isEmpty()) {
            articles = createDailyArticles();
        }
        return articles.stream()
                .map(ArticleResponse::new)
                .collect(Collectors.toList());
    }

    // --- 👇 여기가 핵심 수정 사항입니다 ---
    @Transactional // 👈 readOnly = true를 제거하여 쓰기 작업을 허용합니다.
    public DailyNewsResponse getDailyArticles(int page) {
        List<ArticleResponse> dailyArticles = getDailyArticlesCacheable();

        if (dailyArticles.isEmpty()) {
            return DailyNewsResponse.from(null, 0, 0);
        }

        if (page < 1 || page > dailyArticles.size()) {
            throw new IllegalArgumentException("page 범위는 1-" + dailyArticles.size() + " 입니다.");
        }

        return DailyNewsResponse.from(dailyArticles.get(page - 1), page, dailyArticles.size());
    }

    public String getCacheKey() {
        return LocalDate.now().toString();
    }

    @Transactional
    public List<Article> createDailyArticles() {
        List<String> topics = List.of("정치", "경제", "사회", "문화", "IT", "세계");

        List<Article> allArticles = topics.stream()
                .flatMap(topic -> getArticlesFromNaver(topic).stream())
                .collect(Collectors.toList());

        Collections.shuffle(allArticles);
        List<Article> selectedArticles = allArticles.stream()
                .filter(article -> !articleRepository.findByArticleUrl(article.getArticleUrl()).isPresent())
                .limit(5)
                .collect(Collectors.toList());

        LocalDateTime today = LocalDateTime.now();
        selectedArticles.forEach(article -> article.setSelectedAt(today));

        return articleRepository.saveAll(selectedArticles);
    }

    @Transactional
    public List<Article> getArticlesFromNaver(String keyword) {
        NaverNewsResponse body = naverWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/news.json")
                        .queryParam("query", keyword)
                        .queryParam("display", 5)
                        .queryParam("sort", "date")
                        .build())
                .retrieve()
                .bodyToMono(NaverNewsResponse.class)
                .block();

        NaverNewsResponse res = Objects.requireNonNull(body, "네이버 응답이 비어있습니다.");

        return res.items().stream()
                .map(it -> {
                    Topic topic = topicRepository.findByName(keyword)
                            .orElseGet(() -> topicRepository.save(Topic.builder().name(keyword).build()));

                    LocalDateTime pubDt = DateTimeUtil.parseNaverDate(it.pubDate());
                    Article.Category category = mapKeywordToCategory(keyword);

                    String articleUrl = it.originallink() != null && !it.originallink().isBlank() ? it.originallink() : it.link();
                    String thumbnailUrl = ThumbnailExtractor.fetchThumbnail(articleUrl);

                    return Article.builder()
                            .title(it.title())
                            .description(it.description())
                            .articleUrl(articleUrl)
                            .pubDate(pubDt)
                            .topic(topic)
                            .category(category)
                            .imageUrl(thumbnailUrl)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Article.Category mapKeywordToCategory(String keyword) {
        return switch (keyword) {
            case "정치" -> Article.Category.POLITICS;
            case "경제" -> Article.Category.ECONOMY;
            case "사회" -> Article.Category.SOCIETY;
            case "문화" -> Article.Category.CULTURE;
            case "IT" -> Article.Category.IT;
            case "세계" -> Article.Category.WORLD;
            default -> Article.Category.UNKNOWN;
        };
    }
}

