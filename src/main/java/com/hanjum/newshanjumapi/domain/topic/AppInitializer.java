package com.hanjum.newshanjumapi.domain.topic;

import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import com.hanjum.newshanjumapi.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;

    /**
     * 애플리케이션 시작 시점에 테스트용 데이터를 생성합니다.
     */
    @Override
    public void run(String... args) throws Exception {
        // 테스트용 회원 데이터 생성 (ID는 1L)
        // 로그인 기능 구현 전까지 이 회원을 사용합니다.
        if (memberRepository.count() == 0) {
            Member testMember = Member.builder()
                    .username("testuser")
                    .password("password123")
                    .build();
            memberRepository.save(testMember);
        }

        // 테스트용 토픽 데이터 생성
        if (topicRepository.count() == 0) {
            topicRepository.save(Topic.builder().name("경제").build());
            topicRepository.save(Topic.builder().name("IT").build());
            topicRepository.save(Topic.builder().name("스포츠").build());
            topicRepository.save(Topic.builder().name("정치").build());
            topicRepository.save(Topic.builder().name("사회").build());
        }

        System.out.println("테스트용 회원과 토픽 데이터가 성공적으로 생성되었습니다.");
    }
}

