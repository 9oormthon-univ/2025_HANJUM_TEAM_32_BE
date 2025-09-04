package com.hanjum.newshanjumapi;

import com.hanjum.newshanjumapi.domain.topic.dto.DataLabRequestDto;
import com.hanjum.newshanjumapi.domain.topic.dto.DataLabResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TrendApiClient {

    private final RestTemplate restTemplate;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    private static final String DATALAB_API_URL = "https://openapi.naver.com/v1/datalab/search";

    public DataLabResponseDto fetchTrendData(DataLabRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<DataLabRequestDto> entity = new HttpEntity<>(requestDto, headers);

        return restTemplate.postForObject(DATALAB_API_URL, entity, DataLabResponseDto.class);
    }
}