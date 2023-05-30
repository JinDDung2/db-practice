package com.example.fasns.application.controller;

import com.example.fasns.domain.member.dto.*;
import com.example.fasns.domain.member.service.MemberReadService;
import com.example.fasns.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
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

    @GetMapping("/{id}")
    public Response<MemberDto> getMember(@PathVariable Long id) {
        return Response.success(memberReadService.getMember(id), OK);
    }

    @GetMapping("/{id}/nickname-histories")
    public Response<List<MemberNicknameHistoryDto>> getNicknameHistories(@PathVariable Long id) {
        return Response.success(memberReadService.getNicknameHistories(id), OK);
    }

    @PostMapping("/{id}")
    public Response<MemberDto> changeNickname(@PathVariable Long id, @RequestBody String nickname) {
        memberWriteService.changeNickname(id, nickname);
        return Response.success(memberReadService.getMember(id), OK);
    }
}
