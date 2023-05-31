package com.example.fasns.domain.follow.service;

import com.example.fasns.domain.follow.entity.Follow;
import com.example.fasns.domain.follow.repository.FollowRepository;
import com.example.fasns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class FollowWriteService {

    private final FollowRepository followRepository;

    public void create(MemberDto fromMember, MemberDto toMember) {
        Assert.isTrue(!fromMember.getId().equals(toMember.getId()), "자기 자신을 팔로우 할 수 없습니다.");

        Follow follow = Follow.builder()
                .fromMemberId(fromMember.getId())
                .toMemberId(toMember.getId())
                .build();

        followRepository.save(follow);
    }

    public void delete(MemberDto fromMember, MemberDto toMember) {
        Assert.isTrue(!fromMember.getId().equals(toMember.getId()), "자기 자신을 언팔로우 할 수 없습니다.");

        followRepository.delete(fromMember.getId(), toMember.getId());
    }


}
