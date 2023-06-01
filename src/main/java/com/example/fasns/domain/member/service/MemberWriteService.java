package com.example.fasns.domain.member.service;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.dto.MemberRegisterDto;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.entity.MemberNicknameHistory;
import com.example.fasns.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fasns.domain.member.repository.MemberRepository;
import com.example.fasns.global.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fasns.global.exception.ErrorCode.USER_NOT_FOUND;

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
    public void changeNickname(String email, String nickname) {
        Member member = validateMember(email);
        member.changeNickname(nickname);
        memberRepository.save(member);

        // 이름 변경 히스토리 저장
        saveNicknameHistory(member);
    }

    @Transactional
    public void changePassword(String email, String password) {
        Member member = validateMember(email);
        member.changePassword(passwordEncoder, password);
        memberRepository.save(member);
    }

    @Transactional
    public void delete(String email) {
        Member member = validateMember(email);
        memberRepository.delete(member);
        memberNicknameHistoryRepository.delete(member.getId());
    }

    private Member validateMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new SystemException(String.format("%s %s", email, USER_NOT_FOUND.getMessage()),
                        USER_NOT_FOUND));
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
