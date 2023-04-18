package com.example.fasns.controller;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.dto.MemberRegisterCommand;
import com.example.fasns.domain.member.service.MemberReadService;
import com.example.fasns.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping("/members")
    public MemberDto register(@RequestBody MemberRegisterCommand command) {
        return memberWriteService.register(command);
    }

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }

    @PostMapping("/members/{id}")
    public MemberDto changeNickname(@PathVariable Long id, @RequestBody String nickname) {
        memberWriteService.changeNickname(id, nickname);
        return memberReadService.getMember(id);
    }
}
