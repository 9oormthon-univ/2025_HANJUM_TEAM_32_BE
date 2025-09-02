package com.hanjum.newshanjumapi.domain.member.dto;

import com.hanjum.newshanjumapi.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private final Long id;
    private final String name;
    private final String email;
    private final String role;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.role = member.getRole().getTitle();
    }
}
