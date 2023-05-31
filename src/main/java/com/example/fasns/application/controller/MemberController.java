package com.example.fasns.application.controller;

import com.example.fasns.domain.member.dto.*;
import com.example.fasns.domain.member.service.MemberReadService;
import com.example.fasns.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @GetMapping("")
    public Response<MemberDto> getMember(Authentication authentication) {
        return Response.success(memberReadService.getMember(authentication.getName()), OK);
    }

    @GetMapping("/nickname-histories")
    public Response<List<MemberNicknameHistoryDto>> getNicknameHistories(Authentication authentication) {
        return Response.success(memberReadService.getNicknameHistories(authentication.getName()), OK);
    }

    @PostMapping("")
    public Response<MemberDto> changeNickname(Authentication authentication, @RequestBody String nickname) {
        memberWriteService.changeNickname(authentication.getName(), nickname);
        return Response.success(memberReadService.getMember(authentication.getName()), OK);
    }

    @DeleteMapping("")
    public Response<Void> delete(Authentication authentication) {
        memberWriteService.delete(authentication.getName());
        return Response.success(OK);
    }
}
