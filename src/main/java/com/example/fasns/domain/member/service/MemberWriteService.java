package com.example.fasns.domain.member.service;

import com.example.fasns.domain.member.dto.MemberRegisterCommand;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;

    /**
     * 회원정보등록(이메일, 닉네임, 생년월일)
     * param - memberRegisterCommand
     */
    public Member create(MemberRegisterCommand command) {
        Member member = Member.builder()
                .email(command.getEmail())
                .nickname(command.getNickname())
                .birth(command.getBirth())
                .build();

        return memberRepository.save(member);
    }
}
