package com.hanjum.newshanjumapi.domain.article.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyNewsResponse {

    private final ArticleResponse article;
    private final int currentPage;
    private final int totalPages;

    public static DailyNewsResponse from(ArticleResponse article, int currentPage, int totalPages) {
        return new DailyNewsResponse(article, currentPage, totalPages);
    }
}




