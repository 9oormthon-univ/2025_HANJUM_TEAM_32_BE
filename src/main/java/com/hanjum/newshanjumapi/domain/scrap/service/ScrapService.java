package com.hanjum.newshanjumapi.domain.scrap.service;

import com.hanjum.newshanjumapi.domain.Article;
import com.hanjum.newshanjumapi.domain.ArticleRepository;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.repository.MemberRepository;
import com.hanjum.newshanjumapi.domain.scrap.dto.ScrapRequestDto;
import com.hanjum.newshanjumapi.domain.scrap.dto.ScrapResponseDto;
import com.hanjum.newshanjumapi.domain.scrap.entity.Scrap;
import com.hanjum.newshanjumapi.domain.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    @Transactional
    public Long saveScrap(String email, ScrapRequestDto requestDto) {
        Member member = findMemberByEmail(email);

        Article article = articleRepository.findById(requestDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("기사를 찾을 수 없습니다."));

        if (scrapRepository.findByMemberAndArticle(member, article).isPresent()) {
            throw new IllegalArgumentException("이미 스크랩한 기사입니다.");
        }

        Scrap scrap = Scrap.builder()
                .member(member)
                .article(article)
                .build();

        return scrapRepository.save(scrap).getId();
    }

    @Transactional
    public void deleteScrap(String email, Long scrapId) {
        Member member = findMemberByEmail(email);

        Scrap scrap = scrapRepository.findById(scrapId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스크랩을 찾을 수 없습니다."));

        if (!scrap.getMember().equals(member)) {
            throw new IllegalArgumentException("해당 스크랩을 삭제할 권한이 없습니다.");
        }

        scrapRepository.delete(scrap);
    }

    public long countScraps(String email) {
        Member member = findMemberByEmail(email);
        return scrapRepository.countByMember(member);
    }

    public Page<ScrapResponseDto> getScraps(String email, Optional<Article.Category> category, Pageable pageable) {
        Member member = findMemberByEmail(email);

        Page<Scrap> scraps;
        if (category.isPresent()) {
            scraps = scrapRepository.findByMemberAndArticle_Category(member, category.get(), pageable);
        } else {
            scraps = scrapRepository.findByMember(member, pageable);
        }

        return scraps.map(ScrapResponseDto::new);
    }
}