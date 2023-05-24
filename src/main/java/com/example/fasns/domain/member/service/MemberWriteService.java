package com.example.fasns.domain.member.service;

import com.example.fasns.common.ErrorCode;
import com.example.fasns.common.SystemException;
import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.dto.MemberRegisterDto;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.entity.MemberNicknameHistory;
import com.example.fasns.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fasns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원정보등록(이메일, 비밀번호, 닉네임, 생년월일)
     * param - memberRegisterDto
     */
    @Transactional
    public MemberDto register(MemberRegisterDto registerDto) {
        Member member = Member.builder()
                .email(registerDto.getEmail())
                .nickname(registerDto.getNickname())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .birth(registerDto.getBirth())
                .build();

        Member saveMember = memberRepository.save(member);
        saveNicknameHistory(saveMember);

        return toDto(saveMember);
    }

    @Transactional
    public void changeNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new SystemException(String.format("%s %s", memberId, ErrorCode.USER_NOT_FOUND.getMessage()),
                        ErrorCode.USER_NOT_FOUND));
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
