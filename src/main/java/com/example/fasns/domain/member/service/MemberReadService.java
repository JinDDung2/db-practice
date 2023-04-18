package com.example.fasns.domain.member.service;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;

    public MemberDto getMember(Long id) {
        return MemberDto.toDto(memberRepository.findById(id).orElseThrow());
    }
}
