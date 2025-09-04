package com.hanjum.newshanjumapi.domain.article.repository;

import com.hanjum.newshanjumapi.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    Optional<Article> findByArticleUrl(String articleUrl);

    @Query("SELECT a FROM Article a WHERE DATE(a.selectedAt) = :selectedDate")
    List<Article> findBySelectedAtDate(@Param("selectedDate") LocalDate selectedDate);

}