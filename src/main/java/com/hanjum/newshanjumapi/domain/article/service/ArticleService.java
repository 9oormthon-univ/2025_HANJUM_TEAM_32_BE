package com.hanjum.newshanjumapi.domain.article.service;

import com.hanjum.newshanjumapi.domain.article.dto.ArticleResponse;
import com.hanjum.newshanjumapi.domain.article.dto.DailyNewsResponse;
import com.hanjum.newshanjumapi.domain.article.dto.NaverNewsResponse;
import com.hanjum.newshanjumapi.domain.article.entity.Article;
import com.hanjum.newshanjumapi.domain.article.repository.ArticleDetailResponseDto;
import com.hanjum.newshanjumapi.domain.article.repository.ArticleRepository;
import com.hanjum.newshanjumapi.domain.article.util.DateTimeUtil;
import com.hanjum.newshanjumapi.domain.article.util.ThumbnailExtractor;
import com.hanjum.newshanjumapi.domain.gpt.service.GptService;
import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import com.hanjum.newshanjumapi.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    private final GptService gptService; // AI 서비스 주입

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

    @Transactional
    public DailyNewsResponse getDailyArticles(int page) {
        List<ArticleResponse> dailyArticles = getDailyArticlesCacheable();

        if (dailyArticles.isEmpty()) {
            // 크롤링된 기사가 없는 경우에 대한 처리
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
                // isPresent()를 사용하여 Optional을 올바르게 처리합니다.
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
                        .queryParam("display", 5) // 토픽별로 5개씩 가져옴
                        .queryParam("sort", "date")
                        .build())
                .retrieve()
                .bodyToMono(NaverNewsResponse.class)
                .block();

        NaverNewsResponse res = Objects.requireNonNull(body, "네이버 응답이 비어있습니다.");

        return res.items().stream()
                .map(it -> {
                    String articleUrl = it.originallink() != null && !it.originallink().isBlank() ? it.originallink() : it.link();

                    // --- Jsoup으로 본문 크롤링 및 AI 해설 생성 로직 ---
                    String content;
                    try {
                        // 각 기사 페이지에 접속
                        Document articleDoc = Jsoup.connect(articleUrl).get();
                        // 언론사마다 다른 본문 태그를 고려하여 여러 선택자 사용
                        content = articleDoc.select("#dic_area, #articeBody, #articleBodyContents").text();
                        if (content.isBlank()) return null; // 본문이 없으면 해당 기사는 건너뜀
                    } catch (Exception e) {
                        return null; // 크롤링 중 오류 발생 시 건너뜀
                    }

                    String summary = summarizeContent(content);
                    String commentary = gptService.generateNewsCommentary(summary);
                    // --- 로직 끝 ---

                    Topic topic = topicRepository.findByName(keyword)
                            .orElseGet(() -> topicRepository.save(Topic.builder().name(keyword).build()));

                    LocalDateTime pubDt = DateTimeUtil.parseNaverDate(it.pubDate());
                    Article.Category category = mapKeywordToCategory(keyword);
                    String thumbnailUrl = ThumbnailExtractor.fetchThumbnail(articleUrl);

                    // 모든 필드를 채워서 Article 객체 생성
                    return Article.builder()
                            .title(it.title())
                            .description(it.description())
                            .content(content)
                            .summary(summary)
                            .aiCommentary(commentary)
                            .articleUrl(articleUrl)
                            .pubDate(pubDt)
                            .topic(topic)
                            .category(category)
                            .imageUrl(thumbnailUrl)
                            .build();
                })
                .filter(Objects::nonNull) // 크롤링에 실패한 기사(null)는 최종 목록에서 제외
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponseDto getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 기사를 찾을 수 없습니다: " + articleId));

        return new ArticleDetailResponseDto(article);
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

    private String summarizeContent(String content) {
        if (content == null || content.length() <= 200) {
            return content;
        }
        return content.substring(0, 200) + "...";
    }


}