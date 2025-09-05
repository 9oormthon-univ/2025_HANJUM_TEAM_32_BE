package com.hanjum.newshanjumapi.domain.analysis.service;

import com.hanjum.newshanjumapi.domain.analysis.dto.AnalysisResponseDto;
import com.hanjum.newshanjumapi.domain.article.entity.Article;
import com.hanjum.newshanjumapi.domain.gpt.service.GptService;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.repository.MemberRepository;
import com.hanjum.newshanjumapi.domain.scrap.repository.ScrapRepository;
import com.hanjum.newshanjumapi.domain.readinghistory.repository.ReadingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisService {

    private final MemberRepository memberRepository;
    private final ReadingHistoryRepository readingHistoryRepository;
    private final ScrapRepository scrapRepository;
    private final GptService gptService;

    public AnalysisResponseDto getAnalysisForUser(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Map<String, Object> categoryAnalysis = analyzeMostViewedCategory(member);
        String averageReadTime = analyzeAverageReadTime(member);
        int weeklyScrapCount = countWeeklyScraps(member);
        String recommendation = createRecommendation((String) categoryAnalysis.get("category"));

        return AnalysisResponseDto.builder()
                .mostViewedCategory((String) categoryAnalysis.get("category"))
                .percentage((int) categoryAnalysis.get("percentage"))
                .averageReadTime(averageReadTime)
                .weeklyScrapCount(weeklyScrapCount)
                .recommendationMessage(recommendation)
                .build();
    }

    private Map<String, Object> analyzeMostViewedCategory(Member member) {
        List<Object[]> counts = readingHistoryRepository.countByCategory(member);
        if (counts.isEmpty()) {
            return Map.of("category", "없음", "percentage", 0);
        }

        long totalViews = counts.stream().mapToLong(row -> (long) row[1]).sum();

        Optional<Object[]> mostViewed = counts.stream()
                .max(Comparator.comparingLong(row -> (long) row[1]));

        Article.Category category = (Article.Category) mostViewed.get()[0];
        long count = (long) mostViewed.get()[1];
        int percentage = (int) Math.round((double) count / totalViews * 100);

        return Map.of("category", category.name(), "percentage", percentage);
    }

    private String analyzeAverageReadTime(Member member) {
        double avgSeconds = readingHistoryRepository.findAverageReadTimeByMember(member).orElse(0.0);
        long minutes = (long) avgSeconds / 60;
        long seconds = (long) avgSeconds % 60;
        return String.format("%d분 %d초", minutes, seconds);
    }

    private int countWeeklyScraps(Member member) {
        LocalDateTime startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        return scrapRepository.countByMemberAndCreatedAtAfter(member, startOfWeek);
    }

    private String createRecommendation(String category) {
        if ("없음".equals(category)) {
            return "아직 읽은 기사가 없어요. 오늘의 뉴스를 읽고 관심사를 분석해보세요!";
        }
        String prompt = String.format("%s 분야에 대한 사용자의 관심을 바탕으로, 함께 관심을 가질만한 관련 토픽 2가지를 '토픽1'과 '토픽2' 형식으로만 추천해줘.", category);
        String recommendedTopics = gptService.generateTopicRecommendation(category);

        return String.format("%s 분야에 관심이 높으시네요! %s 토픽도 추가해보세요.", category, recommendedTopics);
    }
}

