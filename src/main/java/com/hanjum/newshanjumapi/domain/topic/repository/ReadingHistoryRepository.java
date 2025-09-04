package com.hanjum.newshanjumapi.domain.topic.repository;

import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.topic.entity.ReadingHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Long> {

    @Query("SELECT rh.article.topic.id, COUNT(rh.article.topic.id) as count " +
            "FROM ReadingHistory rh " +
            "WHERE rh.member = :member AND rh.createdAt >= :startDate " +
            "GROUP BY rh.article.topic.id ORDER BY count DESC")
    List<Object[]> findMostViewedTopicIdsByMember(@Param("member") Member member, @Param("startDate") LocalDateTime startDate, Pageable pageable);

    @Query("SELECT AVG(rh.readTimeSeconds) FROM ReadingHistory rh " +
            "WHERE rh.member = :member AND rh.createdAt >= :startDate")
    Optional<Double> findAverageReadTimeByMember(@Param("member") Member member, @Param("startDate") LocalDateTime startDate);
}