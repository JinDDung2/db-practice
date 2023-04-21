package com.example.fasns.application.usecase;

import com.example.fasns.domain.follow.dto.FollowDto;
import com.example.fasns.domain.follow.service.FollowReadService;
import com.example.fasns.domain.post.dto.PostCommand;
import com.example.fasns.domain.post.service.PostWriteService;
import com.example.fasns.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatePostUseCase {

    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimelineWriteService timelineWriteService;

    public Long execute(PostCommand command) {
        Long postId = postWriteService.create(command);

        List<Long> followMemberIds = followReadService.getFollowers(command.getMemberId()).stream()
                .map(FollowDto::getFromMemberId)
                .collect(Collectors.toList());

        timelineWriteService.deliveryToTimeline(postId, followMemberIds);

        return postId;
    }

}
