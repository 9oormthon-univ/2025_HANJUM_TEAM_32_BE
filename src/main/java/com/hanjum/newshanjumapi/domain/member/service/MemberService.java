package com.hanjum.newshanjumapi.domain.member.service;

import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.entity.Role;
import com.hanjum.newshanjumapi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member saveOrUpdate(String email, String nickname) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        Member member;
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            member.updateName(nickname);
        } else {
            member = Member.builder()
                    .email(email)
                    .name(nickname)
                    .role(Role.USER)
                    .build();
            memberRepository.save(member);
        }
        return member;
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + email));
    }
}