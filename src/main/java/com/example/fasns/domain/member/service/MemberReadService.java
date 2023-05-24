package com.example.fasns.domain.member.service;

import com.example.fasns.common.SystemException;
import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.dto.MemberLoginDto;
import com.example.fasns.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.entity.MemberNicknameHistory;
import com.example.fasns.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fasns.domain.member.repository.MemberRepository;
import com.example.fasns.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.fasns.common.ErrorCode.PASSWORD_NOT_MATCHED;
import static com.example.fasns.common.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberDto getMember(Long id) {
        return toDto(memberRepository.findById(id).orElseThrow());
    }

    public List<MemberDto> getMembers(List<Long> ids) {
        return memberRepository.findAllByIdIn(ids)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public List<MemberNicknameHistoryDto> getNicknameHistories(Long memberId) {
        return memberNicknameHistoryRepository.findAllByMemberId(memberId)
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

    public MemberLoginDto.TokenResDto login(MemberLoginDto loginDto) {
        Member member = validateMember(loginDto);

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new SystemException(PASSWORD_NOT_MATCHED);
        }

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // authentication 에서 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthenticationToken();
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomMemberDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        MemberLoginDto.TokenResDto tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    private Member validateMember(MemberLoginDto loginDto) {
        return memberRepository.findByEmail(loginDto.getEmail()).orElseThrow(() ->
                new SystemException(String.format("%s %s", loginDto.getEmail(), USER_NOT_FOUND.getMessage()),
                        USER_NOT_FOUND)
        );
    }
}
