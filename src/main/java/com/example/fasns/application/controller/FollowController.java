package com.example.fasns.application.controller;

import com.example.fasns.application.usecase.FollowMemberUseCase;
import com.example.fasns.application.usecase.GetFollowMemberUseCase;
import com.example.fasns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowMemberUseCase followMemberUseCase;
    private final GetFollowMemberUseCase getFollowMemberUseCase;

    @PostMapping("/{toMemberId}")
    public Response<?> following(Authentication authentication,
                                 @PathVariable Long toMemberId) {
        followMemberUseCase.following(authentication.getName(), toMemberId);
        return Response.success(OK);
    }

    @DeleteMapping("/{toMemberId}")
    public Response<?> unFollowing(Authentication authentication,
                                  @PathVariable Long toMemberId) {
        followMemberUseCase.unFollowing(authentication.getName(), toMemberId);
        return Response.success(OK);
    }

    @GetMapping("/members/{memberId}")
    public Response<List<MemberDto>> getFollowings(@PathVariable Long memberId) {
        return Response.success(getFollowMemberUseCase.execute(memberId), OK);
    }
}
