package com.hanjum.newshanjumapi.domain.topic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    // 회원 ID (기본키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    // 사용자 이름
    @Column(nullable = false, unique = true)
    private String username;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    @Builder
    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }
}