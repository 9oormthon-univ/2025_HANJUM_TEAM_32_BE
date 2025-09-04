package com.hanjum.newshanjumapi.domain.article.dto;

import com.hanjum.newshanjumapi.domain.article.entity.Article;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String articleUrl;
    private final LocalDateTime pubDate;
    private final String topic;
    private final String category;
    private final String imageUrl;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.articleUrl = article.getArticleUrl();
        this.pubDate = article.getPubDate();
        this.topic = article.getTopic().getName();
        this.category = article.getCategory().name();
        this.imageUrl = article.getImageUrl();
    }
}