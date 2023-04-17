package com.example.fasns.controller;

import com.example.fasns.domain.member.dto.MemberRegisterCommand;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.service.MemberWriteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberWriteService memberWriteService;

    public MemberController(MemberWriteService memberWriteService) {
        this.memberWriteService = memberWriteService;
    }

    @PostMapping("/members")
    public Member register(@RequestBody MemberRegisterCommand command) {
        return memberWriteService.create(command);
    }
}
