package com.hanjum.newshanjumapi.domain.topic.repository;

import com.hanjum.newshanjumapi.domain.topic.dto.TopicDailyCountDto;
import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {


    Optional<Topic> findByName(String name);

    @Query("SELECT mt.topic FROM MemberTopic mt GROUP BY mt.topic ORDER BY COUNT(mt.member) DESC")
    List<Topic> findPopularTopics(Pageable pageable);

    @Query("SELECT new com.hanjum.newshanjumapi.domain.topic.dto.TopicDailyCountDto(" +
            "t.name, " +
            "CAST(a.pubDate AS java.time.LocalDate), " +
            "COUNT(a.id)) " +
            "FROM Article a JOIN a.topic t " +
            "WHERE a.pubDate BETWEEN :startDate AND :endDate " +
            "AND t.name IN :topicNames " +
            "GROUP BY t.name, CAST(a.pubDate AS java.time.LocalDate) " +
            "ORDER BY t.name, CAST(a.pubDate AS java.time.LocalDate)")
    List<TopicDailyCountDto> findTopicTrendsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("topicNames") List<String> topicNames);
}

