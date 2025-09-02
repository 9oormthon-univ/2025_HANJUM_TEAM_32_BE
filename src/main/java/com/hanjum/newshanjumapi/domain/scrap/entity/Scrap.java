package com.hanjum.newshanjumapi.domain.scrap.entity;

import com.hanjum.newshanjumapi.domain.Article;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 회원별 뉴스 스크랩 정보를 저장하는 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "scrap")
@EntityListeners(AuditingEntityListener.class)
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @Builder
    public Scrap(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
