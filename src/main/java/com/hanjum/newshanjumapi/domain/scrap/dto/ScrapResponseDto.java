package com.hanjum.newshanjumapi.domain.scrap.dto;

import com.hanjum.newshanjumapi.domain.scrap.entity.Scrap;
import lombok.Getter;

@Getter
public class ScrapResponseDto {

    private final Long scrapId;
    private final Long articleId;
    private final String title;
    private final String description;
    private final String imageUrl;

    public ScrapResponseDto(Scrap scrap) {
        this.scrapId = scrap.getId();
        this.articleId = scrap.getArticle().getId();
        this.title = scrap.getArticle().getTitle();
        this.description = scrap.getArticle().getDescription();
        this.imageUrl = scrap.getArticle().getImageUrl();
    }
}
