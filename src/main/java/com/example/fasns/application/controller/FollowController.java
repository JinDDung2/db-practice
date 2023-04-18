package com.example.fasns.application.controller;

import com.example.fasns.application.usecase.CreateFollowMemberUseCase;
import com.example.fasns.application.usecase.GetFollowMemberUseCase;
import com.example.fasns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final CreateFollowMemberUseCase createFollowMemberUseCase;
    private final GetFollowMemberUseCase getFollowMemberUseCase;

    @PostMapping("/{fromMemberId}/{toMemberId}")
    public void register(@PathVariable Long fromMemberId, @PathVariable Long toMemberId) {
        createFollowMemberUseCase.execute(fromMemberId, toMemberId);
    }

    @GetMapping("/members/{memberId}")
    public List<MemberDto> getFollowings(@PathVariable Long memberId) {
        return getFollowMemberUseCase.execute(memberId);
    }
}
