package com.hanjum.newshanjumapi.domain.scrap.repository;

import com.hanjum.newshanjumapi.domain.Article;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.scrap.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    List<Scrap> findByMember(Member member);

    Optional<Scrap> findByMemberAndArticle(Member member, Article article);

    long countByMember(Member member);

    Page<Scrap> findByMember(Member member, Pageable pageable);

    Page<Scrap> findByMemberAndArticle_Category(Member member, Article.Category category, Pageable pageable);

    void deleteById(Long scrapId);
}