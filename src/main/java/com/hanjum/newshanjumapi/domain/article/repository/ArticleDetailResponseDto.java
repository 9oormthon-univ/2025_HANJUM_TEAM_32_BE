package com.hanjum.newshanjumapi.domain.article.repository;

import com.hanjum.newshanjumapi.domain.article.entity.Article;
import lombok.Getter;

@Getter
public class ArticleDetailResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String summary;
    private final String aiCommentary;

    public ArticleDetailResponseDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.summary = article.getSummary();
        this.aiCommentary = article.getAiCommentary();
    }
}