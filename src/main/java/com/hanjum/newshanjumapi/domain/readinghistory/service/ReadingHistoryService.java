package com.hanjum.newshanjumapi.domain.readinghistory.service;


import com.hanjum.newshanjumapi.domain.article.entity.Article;
import com.hanjum.newshanjumapi.domain.article.repository.ArticleRepository;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.repository.MemberRepository;
import com.hanjum.newshanjumapi.domain.readinghistory.repository.ReadingHistoryRepository;
import com.hanjum.newshanjumapi.domain.topic.dto.ReadingHistoryDto;
import com.hanjum.newshanjumapi.domain.topic.entity.ReadingHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadingHistoryService {

    private final ReadingHistoryRepository readingHistoryRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public void saveReadingHistory(String email, ReadingHistoryDto requestDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Article article = articleRepository.findById(requestDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("기사를 찾을 수 없습니다."));

        ReadingHistory history = ReadingHistory.builder()
                .member(member)
                .article(article)
                .readTimeSeconds(requestDto.getReadTimeSeconds())
                .build();

        readingHistoryRepository.save(history);
    }
}

