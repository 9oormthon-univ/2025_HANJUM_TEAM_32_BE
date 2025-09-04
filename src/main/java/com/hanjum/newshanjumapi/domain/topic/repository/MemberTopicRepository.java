package com.hanjum.newshanjumapi.domain.topic.repository;


import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.topic.entity.MemberTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTopicRepository extends JpaRepository<MemberTopic, Long> {

    void deleteAllByMember(Member member);
}