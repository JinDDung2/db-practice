package com.example.fasns.global.config;

import com.example.fasns.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class OAuth2Config {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

//    @Bean
//    public AuthenticationSuccessHandler oAuth2LoginSuccessHandler() {
//        return new OAuth2LoginSuccessHandler(jwtTokenProvider, redisTemplate);
//    }
//
//    @Bean
//    public AuthenticationFailureHandler oAuth2LoginFailureHandler() {
//        return new OAuth2LoginFailureHandler();
//    }
}
