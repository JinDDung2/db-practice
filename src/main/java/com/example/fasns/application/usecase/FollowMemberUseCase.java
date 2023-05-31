package com.example.fasns.application.usecase;

import com.example.fasns.domain.follow.service.FollowWriteService;
import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowMemberUseCase {

    private final MemberReadService memberReadService;
    private final FollowWriteService followWriteService;

    /**
     * @param email
     * @param toMemberId
     * memberId로 회원조회
     * FollowService.create()
     */
    public void following(String email, Long toMemberId) {
        MemberDto fromMember = memberReadService.getMember(email);
        MemberDto toMember = memberReadService.getMember(toMemberId);

        followWriteService.create(fromMember, toMember);
    }

    public void unFollowing(String email, Long toMemberId) {
        MemberDto fromMember = memberReadService.getMember(email);
        MemberDto toMember = memberReadService.getMember(toMemberId);

        followWriteService.delete(fromMember, toMember);
    }
}
