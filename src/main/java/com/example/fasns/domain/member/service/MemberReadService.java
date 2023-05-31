package com.example.fasns.domain.member.service;

import com.example.fasns.common.ErrorCode;
import com.example.fasns.common.SystemException;
import com.example.fasns.domain.member.dto.*;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.entity.MemberNicknameHistory;
import com.example.fasns.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fasns.domain.member.repository.MemberRepository;
import com.example.fasns.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.fasns.common.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    public MemberDto getMember(Long id) {
        return toDto(memberRepository.findById(id).orElseThrow());
    }

    public MemberDto getMember(String email) {
        return toDto(memberRepository.findByEmail(email).orElseThrow());
    }

    public List<MemberDto> getMembers(List<Long> ids) {
        return memberRepository.findAllByIdIn(ids)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public List<MemberNicknameHistoryDto> getNicknameHistories(String email) {
        Member member = validateMember(email);
        return memberNicknameHistoryRepository.findAllByMemberId(member.getId())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birth(member.getBirth())
                .build();
    }

    private MemberNicknameHistoryDto toDto(MemberNicknameHistory history) {
        return MemberNicknameHistoryDto.builder()
                .id(history.getId())
                .memberId(history.getMemberId())
                .nickname(history.getNickname())
                .createdAt(history.getCreatedAt())
                .build();
    }

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
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
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

    public TokenDto logout(TokenDto tokenDto) {
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
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(tokenDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(tokenDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    private Member validateMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new SystemException(String.format("%s %s", email, USER_NOT_FOUND.getMessage()),
                        USER_NOT_FOUND)
        );
    }

}
