package com.example.fasns.application.controller;

import com.example.fasns.application.usecase.CreateFollowMemberUseCase;
import com.example.fasns.application.usecase.GetFollowMemberUseCase;
import com.example.fasns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final CreateFollowMemberUseCase createFollowMemberUseCase;
    private final GetFollowMemberUseCase getFollowMemberUseCase;

    @PostMapping("/{fromMemberId}/{toMemberId}")
    public Response<?> following(@PathVariable Long fromMemberId,
                                 @PathVariable Long toMemberId) {
        createFollowMemberUseCase.execute(fromMemberId, toMemberId);
        return Response.success();
    }

    @GetMapping("/members/{memberId}")
    public Response<List<MemberDto>> getFollowings(@PathVariable Long memberId) {
        return Response.success(getFollowMemberUseCase.execute(memberId));
    }
}
