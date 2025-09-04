package com.hanjum.newshanjumapi.domain.article.service;

import com.hanjum.newshanjumapi.domain.article.dto.PreferRequest;
import com.hanjum.newshanjumapi.domain.article.dto.PreferResponse;
import com.hanjum.newshanjumapi.domain.article.repository.ArticleRepository;
import com.hanjum.newshanjumapi.domain.article.repository.PreferenceRepository;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PreferenceService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository; // 기사
    private final PreferenceRepository preferenceRepository;

    public void analyzePreferences(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + memberId));
        System.out.println(member.getName() + "님의 선호도를 분석합니다.");
    }

    @Transactional
    public PreferResponse setPreference(Long memberId, Long articleId, PreferRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + memberId));

        System.out.println(member.getName() + "님이 " + articleId + "번 기사에 대한 선호도를 '" + request.getPreferenceType() + "'(으)로 설정했습니다.");

        return new PreferResponse(true, "선호도가 성공적으로 처리되었습니다.");
    }
}

