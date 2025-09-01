package com.hanjum.newshanjumapi.domain.topic.service;

import com.hanjum.newshanjumapi.domain.topic.Member;
import com.hanjum.newshanjumapi.domain.topic.MemberRepository;
import com.hanjum.newshanjumapi.domain.topic.dto.TopicRequestDto;
import com.hanjum.newshanjumapi.domain.topic.dto.TopicResponseDto;
import com.hanjum.newshanjumapi.domain.topic.entity.MemberTopic;
import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import com.hanjum.newshanjumapi.domain.topic.repository.MemberTopicRepository;
import com.hanjum.newshanjumapi.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicService {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final MemberTopicRepository memberTopicRepository;

    public List<TopicResponseDto> getAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(TopicResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveMemberTopics(Long memberId, TopicRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
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
}

//원래 코드
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class TopicService {
//
//    private final MemberRepository memberRepository;
//    private final TopicRepository topicRepository;
//    private final MemberTopicRepository memberTopicRepository;
//
//    public List<TopicResponseDto> getAllTopics() {
//        return topicRepository.findAll()
//                .stream()
//                .map(TopicResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public void saveMemberTopics(Long memberId, TopicRequestDto requestDto) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
//
//        memberTopicRepository.deleteAllByMember(member);
//
//        List<Long> topicIds = requestDto.getTopicIds();
//        for (Long topicId : topicIds) {
//            Topic topic = topicRepository.findById(topicId)
//                    .orElseThrow(() -> new IllegalArgumentException("해당 토픽을 찾을 수 없습니다."));
//
//            MemberTopic memberTopic = MemberTopic.builder()
//                    .member(member)
//                    .topic(topic)
//                    .build();
//
//            memberTopicRepository.save(memberTopic);
//        }
//    }
//}