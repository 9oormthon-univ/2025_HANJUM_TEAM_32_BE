package com.hanjum.newshanjumapi.jwt;
import com.hanjum.newshanjumapi.domain.member.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())

                .authorizeHttpRequests(authorize -> authorize
                        // --- 👇 This section is modified ---
                        .requestMatchers(
                                "/",
                                "/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/topics/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/members/me",
                                "/api/members/{memberId}/topics"
                        ).authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                );

        return http.build();
    }
}