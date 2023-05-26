package com.example.fasns.application.controller;

import com.example.fasns.domain.member.dto.*;
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
    public Response<MemberDto> register(@RequestBody MemberRegisterDto registerDto) {
        return Response.success(memberWriteService.register(registerDto));
    }

    @PostMapping("/login")
    public Response<TokenInfo> login(@RequestBody MemberLoginDto loginDto) {
        return Response.success(memberReadService.login(loginDto));
    }

    @PostMapping("/reissue")
    public Response<TokenDto> reissue(@RequestBody TokenDto tokenDto) {
        return Response.success(memberReadService.reissue(tokenDto));
    }

    @PostMapping("/logout")
    public Response<TokenDto> logout(@RequestBody TokenDto tokenDto) {
        return Response.success(memberReadService.logout(tokenDto));
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
