package com.example.fasns.domain.member.service;

import com.example.fasns.domain.exception.NotFoundException;
import com.example.fasns.domain.member.dto.MemberDto;
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
    public MemberDto register(MemberRegisterCommand command) {
        Member member = Member.builder()
                .email(command.getEmail())
                .nickname(command.getNickname())
                .birth(command.getBirth())
                .build();

        return MemberDto.toDto(memberRepository.save(member));
    }

    public void changeNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException());
        member.changeNickname(nickname);
        memberRepository.save(member);
        // TODO: 2023/04/18 변경내역 히스토리 저장하기
    }
}
