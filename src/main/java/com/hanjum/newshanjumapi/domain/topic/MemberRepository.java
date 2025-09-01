package com.hanjum.newshanjumapi.domain.topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Member 엔티티를 위한 JPA 리포지토리.
 * JpaRepository를 상속받아 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findById(Long[] memberIds);
}
