package com.example.fasns.application.usecase;

import com.example.fasns.domain.follow.dto.FollowDto;
import com.example.fasns.domain.follow.service.FollowReadService;
import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetFollowMemberUseCase {

    private final FollowReadService followReadService;
    private final MemberReadService memberReadService;

    /**
     * @param memberId
     * MemberId를 조회하며 Follow List 찾기
     */
    public List<MemberDto> execute(Long memberId) {
        List<FollowDto> followings = followReadService.getFollowings(memberId);
        List<Long> followingMemberIds = followings.stream().map(FollowDto::getToMemberId).collect(Collectors.toList());
        return memberReadService.getMembers(followingMemberIds);
    }

}
