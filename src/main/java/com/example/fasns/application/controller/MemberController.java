package com.example.fasns.application.controller;

import com.example.fasns.domain.member.dto.*;
import com.example.fasns.domain.member.service.MemberReadService;
import com.example.fasns.domain.member.service.MemberWriteService;
import com.example.fasns.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping("/register")
    public Response<MemberDto> register(@RequestBody MemberRegisterDto registerDto) {
        return Response.success(memberWriteService.register(registerDto), CREATED);
    }

    @PostMapping("/login")
    public Response<TokenInfo> login(@RequestBody MemberLoginDto loginDto) {
        return Response.success(memberReadService.login(loginDto), OK);
    }

    @PostMapping("/reissue")
    public Response<TokenDto> reissue(@RequestBody TokenDto tokenDto) {
        return Response.success(memberReadService.reissue(tokenDto), OK);
    }

    @PostMapping("/logout")
    public Response<TokenDto> logout(@RequestBody TokenDto tokenDto) {
        return Response.success(memberReadService.logout(tokenDto), OK);
    }

    @GetMapping("")
    public Response<MemberDto> getMember(@AuthenticationPrincipal MemberDetail member) {
        return Response.success(memberReadService.getMember(member.getUsername()), OK);
    }

    @GetMapping("/nickname-histories")
    public Response<List<MemberNicknameHistoryDto>> getNicknameHistories(@AuthenticationPrincipal MemberDetail member) {
        return Response.success(memberReadService.getNicknameHistories(member.getUsername()), OK);
    }

    @PostMapping("")
    public Response<MemberDto> changeNickname(@AuthenticationPrincipal MemberDetail member, @RequestBody String nickname) {
        memberWriteService.changeNickname(member.getUsername(), nickname);
        return Response.success(memberReadService.getMember(member.getUsername()), OK);
    }

    @DeleteMapping("")
    public Response<Void> delete(@AuthenticationPrincipal MemberDetail member) {
        memberWriteService.delete(member.getUsername());
        return Response.success(OK);
    }
}
