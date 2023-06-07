package com.example.fasns.domain.member.service;

import com.example.fasns.domain.member.dto.MemberLoginDto;
import com.example.fasns.domain.member.dto.TokenDto;
import com.example.fasns.domain.member.dto.TokenInfo;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.repository.MemberRepository;
import com.example.fasns.global.exception.ErrorCode;
import com.example.fasns.global.exception.SystemException;
import com.example.fasns.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

import static com.example.fasns.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenInfo login(MemberLoginDto loginDto) {
        validateMember(loginDto.getEmail());

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // authentication 에서 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthenticationToken();
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomMemberDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue().set(
                "RT:" + authentication.getName(),
                tokenInfo.getRefreshToken(),
                tokenInfo.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS);

        return tokenInfo;
    }
    public TokenDto reissue(TokenDto tokenDto) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(tokenDto.getRefreshToken())) {
            throw new SystemException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken) || !refreshToken.equals(tokenDto.getRefreshToken())) {
            throw new SystemException(ErrorCode.INVALID_TOKEN);
        }

        // 4. 새로운 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue().set(
                "RT:" + authentication.getName(),
                tokenInfo.getRefreshToken(),
                tokenInfo.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS);
        return tokenDto;
    }

    public void logout(TokenDto tokenDto) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(tokenDto.getAccessToken())) {
            throw new SystemException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
            redisTemplate.delete(authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(tokenDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(tokenDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
    }

    private Member validateMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new SystemException(String.format("%s %s", email, USER_NOT_FOUND.getMessage()),
                        USER_NOT_FOUND)
        );
    }

}
