package com.hanjum.newshanjumapi.domain.article.dto;


import java.time.LocalDateTime;
import java.util.List;


public record NaverNewsResponse(
    Integer total,
    Integer start,
    Integer display,
    List<Item> items
) {
    public record Item(
            String title,
            String originallink,
            String link,
            String description,
            String pubDate
    ){ }

}
