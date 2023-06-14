package com.example.fasns.global.config;

import com.example.fasns.global.jwt.JwtAuthenticationFilter;
import com.example.fasns.global.jwt.JwtTokenProvider;
import com.example.fasns.global.oauth.handler.OAuth2LoginFailureHandler;
import com.example.fasns.global.oauth.handler.OAuth2LoginSuccessHandler;
import com.example.fasns.global.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig {

    private final String[] SWAGGER = {
            "/swagger-ui/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    private final String[] MEMBER_PERMIT = {
            "/api/members/register",
            "/api/members/login"
    };

    private final String[] MEMBER_AUTH = {
            "/api/members",
            "/api/members/**/nickname-histories",
            "/api/members/**"
    };

    private final String[] POST_PERMIT = {
            "/api/posts/**",
    };

    private final String[] POST_AUTH = {
            "/api/posts",
            "/api/posts/members/**",
            "/api/posts/members/**/cursor",
            "/api/posts/members/**/timeline",
    };

    private final String[] FOLLOW_AUTH = {
            "/api/follow",
            "/api/follow/**"
    };

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomOAuth2UserService customOAuth2UserService;
//    private final AuthenticationSuccessHandler authenticationSuccessHandler;
//    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors();

        //세션을 생성하지 않고, 요청마다 새로운 인증을 수행하도록 구성하는 옵션으로 REST API와 같은 환경에서 사용
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //== URL별 권한 관리 옵션 ==//
        httpSecurity
                .authorizeHttpRequests()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers(MEMBER_PERMIT).permitAll()
                .antMatchers(POST_PERMIT).permitAll()
                .antMatchers(MEMBER_AUTH).authenticated()
                .antMatchers(FOLLOW_AUTH).authenticated()
                .antMatchers(POST_AUTH).authenticated();

        // 아이콘, css, js 관련
        // 기본 페이지, css, image, js 하위 폴더에 있는 자료들은 모두 접근 가능, h2-console에 접근 가능
        httpSecurity
                .authorizeHttpRequests()
                .antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
                .antMatchers("/sign-up").permitAll() // 회원가입 접근 가능
                .anyRequest().authenticated(); // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능

        httpSecurity
                .oauth2Login() //OAuth 2.0 기반 인증을 처리하기위해 Provider와의 연동을 지원
                .successHandler(oAuth2LoginSuccessHandler()) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler()) // 소셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint()  //OAuth 2.0 Provider로부터 사용자 정보를 가져오는 엔드포인트를 지정하는 메서드
                .userService(customOAuth2UserService); //OAuth 2.0 인증이 처리되는데 사용될 사용자 서비스를 지정하는 메서드

        httpSecurity
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(jwtTokenProvider, redisTemplate);
    }

    @Bean
    public AuthenticationFailureHandler oAuth2LoginFailureHandler() {
        return new OAuth2LoginFailureHandler();
    }
}
