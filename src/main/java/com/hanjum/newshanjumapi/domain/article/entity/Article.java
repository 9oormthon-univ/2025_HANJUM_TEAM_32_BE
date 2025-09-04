package com.hanjum.newshanjumapi.domain.article.entity;

import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, nullable = false)
    private String articleUrl;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime pubDate;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime selectedAt;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public enum Category {
        POLITICS,
        ECONOMY,
        SOCIETY,
        CULTURE,
        IT,
        WORLD,
        UNKNOWN
    }
}

