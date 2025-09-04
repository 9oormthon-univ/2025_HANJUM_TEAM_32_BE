package com.hanjum.newshanjumapi.domain.article.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class NaverApiConfig {

    @Value("${naver.api.client-id}")
    private String naverClientId;

    @Value("${naver.api.client-secret}")
    private String naverClientSecret;

    @Value("${naver.api.url}")
    private String naverApiUrl;

    @Bean(name = "naverWebClient")
    public WebClient naverWebClient() {
        // --- 👇 디버깅을 위해 추가된 부분 ---
        System.out.println("### Loading Naver API Config ###");
        System.out.println("Client ID: [" + naverClientId + "]");
        System.out.println("Client Secret: [" + naverClientSecret + "]");
        System.out.println("##############################");
        // --- 여기까지 ---

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(naverApiUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(naverApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeaders(headers -> {
                    headers.set("X-Naver-Client-Id", naverClientId);
                    headers.set("X-Naver-Client-Secret", naverClientSecret);
                })
                .build();
    }
}


