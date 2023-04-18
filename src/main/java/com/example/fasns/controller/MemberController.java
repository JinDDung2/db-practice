package com.example.fasns.controller;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fasns.domain.member.dto.MemberRegisterCommand;
import com.example.fasns.domain.member.service.MemberReadService;
import com.example.fasns.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping("")
    public MemberDto register(@RequestBody MemberRegisterCommand command) {
        return memberWriteService.register(command);
    }

    @GetMapping("/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }

    @GetMapping("/{id}/nickname-histories")
    public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long id) {
        return memberReadService.getNicknameHistories(id);
    }

    @PostMapping("/{id}")
    public MemberDto changeNickname(@PathVariable Long id, @RequestBody String nickname) {
        memberWriteService.changeNickname(id, nickname);
        return memberReadService.getMember(id);
    }
}
