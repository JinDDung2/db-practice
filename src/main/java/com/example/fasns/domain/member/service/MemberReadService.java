package com.example.fasns.domain.member.service;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.entity.MemberNicknameHistory;
import com.example.fasns.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fasns.domain.member.repository.MemberRepository;
import com.example.fasns.global.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.fasns.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

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

    private Member validateMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new SystemException(String.format("%s %s", email, USER_NOT_FOUND.getMessage()),
                        USER_NOT_FOUND)
        );
    }

}
