package com.hanjum.newshanjumapi.domain.article.repository;

import com.hanjum.newshanjumapi.domain.article.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByMemberIdAndArticleId(Long memberId, Long articleId); // 나중에 유저아이디 찾기
}
