package com.example.fasns.domain.member.service;

import com.example.fasns.common.ErrorCode;
import com.example.fasns.common.SystemException;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.entity.MemberDetail;
import com.example.fasns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info(">>> 회원 정보 찾기, {}", email);
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new SystemException(String.format("%s %s", email, ErrorCode.USER_NOT_FOUND),
                        ErrorCode.USER_NOT_FOUND)
                );
    }

    private UserDetails createUserDetails(Member member) {
        return new MemberDetail(member);
    }
}
