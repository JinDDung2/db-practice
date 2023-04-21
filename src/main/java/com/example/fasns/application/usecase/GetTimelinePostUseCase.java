package com.example.fasns.application.usecase;

import com.example.fasns.domain.follow.dto.FollowDto;
import com.example.fasns.domain.follow.service.FollowReadService;
import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utils.CursorRequest;
import utils.PageCursor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetTimelinePostUseCase {

    private final FollowReadService followReadService;
    private final PostReadService postReadService;

    /**
     *
     * @param memberId -> follow 조회 -> 게시물 조회
     * @param cursorRequest
     * @return
     */
    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
        List<FollowDto> followings = followReadService.getFollowings(memberId);
        List<Long> followingIds = followings.stream()
                .map(FollowDto::getToMemberId)
                .collect(Collectors.toList());

        return postReadService.getPosts(followingIds, cursorRequest);
    }

    /**
     1. 타임라인 조회
     2. 1번 조건에 해당하는 게시글들 조회
     */
    public PageCursor<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
        List<FollowDto> followings = followReadService.getFollowings(memberId);
        List<Long> followingIds = followings.stream()
                .map(FollowDto::getToMemberId)
                .collect(Collectors.toList());

        return postReadService.getPosts(followingIds, cursorRequest);
    }
}
