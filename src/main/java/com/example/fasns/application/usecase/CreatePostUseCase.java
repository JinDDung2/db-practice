package com.example.fasns.application.usecase;

import com.example.fasns.domain.follow.dto.FollowDto;
import com.example.fasns.domain.follow.service.FollowReadService;
import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.service.MemberReadService;
import com.example.fasns.domain.post.service.PostWriteService;
import com.example.fasns.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatePostUseCase {

    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final MemberReadService memberReadService;
    private final TimelineWriteService timelineWriteService;

    @Transactional
    @CacheEvict(cacheNames = "feed", key = "#email")
    public Long execute(String email, String content) {
        MemberDto member = memberReadService.getMember(email);
        Long postId = postWriteService.create(member, content);

        List<Long> followMemberIds = followReadService.getFollowers(member.getId()).stream()
                .map(FollowDto::getFromMemberId)
                .collect(Collectors.toList());

        timelineWriteService.deliveryToTimeline(postId, followMemberIds);

        return postId;
    }

}
