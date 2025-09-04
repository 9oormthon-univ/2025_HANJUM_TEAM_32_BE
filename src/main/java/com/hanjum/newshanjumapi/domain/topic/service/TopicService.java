package com.hanjum.newshanjumapi.domain.topic.service;

import com.hanjum.newshanjumapi.TrendApiClient;
import com.hanjum.newshanjumapi.domain.member.entity.Member;
import com.hanjum.newshanjumapi.domain.member.repository.MemberRepository;
import com.hanjum.newshanjumapi.domain.topic.dto.*;
import com.hanjum.newshanjumapi.domain.topic.entity.MemberTopic;
import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import com.hanjum.newshanjumapi.domain.topic.repository.MemberTopicRepository;
import com.hanjum.newshanjumapi.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicService {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final MemberTopicRepository memberTopicRepository;
    private final TrendApiClient trendApiClient;

    public List<TopicResponseDto> getAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(TopicResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveMemberTopics(String email, TopicRequestDto requestDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        memberTopicRepository.deleteAllByMember(member);

        List<Long> topicIds = requestDto.getTopicIds();
        for (Long topicId : topicIds) {
            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 토픽을 찾을 수 없습니다."));

            MemberTopic memberTopic = MemberTopic.builder()
                    .member(member)
                    .topic(topic)
                    .build();

            memberTopicRepository.save(memberTopic);
        }
    }

    public List<PopularTopicDto> getPopularTopics() {
        List<Topic> popularTopics = topicRepository.findPopularTopics(PageRequest.of(0, 5));

        return IntStream.range(0, popularTopics.size())
                .mapToObj(i -> {
                    Topic topic = popularTopics.get(i);
                    return new PopularTopicDto(i + 1, topic.getId(), topic.getName());
                })
                .collect(Collectors.toList());
    }

    public List<TopicTrendDto> getRealtimeTopicTrends() {
        List<String> keywords = List.of("AI", "반도체", "부동산", "주식", "NVIDIA");

        Map<String, Integer> keywordRankMap = IntStream.range(0, keywords.size())
                .boxed()
                .collect(Collectors.toMap(keywords::get, i -> i + 1));

        DataLabRequestDto requestDto = DataLabRequestDto.buildForWeeklyTrend("주간 주요 토픽 트렌드", keywords);
        DataLabResponseDto response = trendApiClient.fetchTrendData(requestDto);

        if (response == null || response.getResults() == null) {
            return List.of();
        }

        return response.getResults().stream()
                .map(result -> {
                    String topicName = result.getTitle();
                    int rank = keywordRankMap.getOrDefault(topicName, 0);

                    List<TopicTrendDto.DataPoint> dataPoints = result.getData().stream()
                            .map(dataPoint -> new TopicTrendDto.DataPoint(
                                    LocalDate.parse(dataPoint.getPeriod()),
                                    Math.round(dataPoint.getRatio())
                            ))
                            .collect(Collectors.toList());

                    return new TopicTrendDto(rank, topicName, dataPoints);
                })
                .collect(Collectors.toList());
    }
}