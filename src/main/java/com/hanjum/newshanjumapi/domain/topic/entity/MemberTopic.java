package com.hanjum.newshanjumapi.domain.topic.entity;

import com.hanjum.newshanjumapi.domain.topic.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_topic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_topic_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Builder
    public MemberTopic(Member member, Topic topic) {
        this.member = member;
        this.topic = topic;
    }
}