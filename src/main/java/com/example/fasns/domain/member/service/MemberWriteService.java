package com.example.fasns.domain.member.service;

import com.example.fasns.domain.exception.NotFoundException;
import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.dto.MemberRegisterCommand;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.entity.MemberNicknameHistory;
import com.example.fasns.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fasns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

    /**
     * 회원정보등록(이메일, 닉네임, 생년월일)
     * param - memberRegisterCommand
     */
    @Transactional
    public MemberDto register(MemberRegisterCommand command) {
        Member member = Member.builder()
                .email(command.getEmail())
                .nickname(command.getNickname())
                .birth(command.getBirth())
                .build();

        Member saveMember = memberRepository.save(member);
        saveNicknameHistory(saveMember);

        return toDto(saveMember);
    }

    @Transactional
    public void changeNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException());
        member.changeNickname(nickname);
        memberRepository.save(member);

        // 이름 변경 히스토리 저장
        saveNicknameHistory(member);
    }

    private void saveNicknameHistory(Member member) {
        MemberNicknameHistory memberNicknameHistory = MemberNicknameHistory.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();

        memberNicknameHistoryRepository.save(memberNicknameHistory);
    }

    private MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birth(member.getBirth())
                .build();
    }

}
