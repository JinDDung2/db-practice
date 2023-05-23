package com.example.fasns.application.controller;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fasns.domain.member.dto.MemberRegisterDto;
import com.example.fasns.domain.member.service.MemberReadService;
import com.example.fasns.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping("/register")
    public Response<MemberDto> register(@RequestBody MemberRegisterDto command) {
        return Response.success(memberWriteService.register(command));
    }

    @GetMapping("/{id}")
    public Response<MemberDto> getMember(@PathVariable Long id) {
        return Response.success(memberReadService.getMember(id));
    }

    @GetMapping("/{id}/nickname-histories")
    public Response<List<MemberNicknameHistoryDto>> getNicknameHistories(@PathVariable Long id) {
        return Response.success(memberReadService.getNicknameHistories(id));
    }

    @PostMapping("/{id}")
    public Response<MemberDto> changeNickname(@PathVariable Long id, @RequestBody String nickname) {
        memberWriteService.changeNickname(id, nickname);
        return Response.success(memberReadService.getMember(id));
    }
}
