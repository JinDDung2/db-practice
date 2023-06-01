package com.example.fasns.application.controller;

import com.example.fasns.application.usecase.CreatePostLikeUseCase;
import com.example.fasns.application.usecase.CreatePostUseCase;
import com.example.fasns.application.usecase.GetTimelinePostUseCase;
import com.example.fasns.domain.post.dto.DailyPostCountDto;
import com.example.fasns.domain.post.dto.DailyPostCountRequest;
import com.example.fasns.domain.post.dto.PostDto;
import com.example.fasns.domain.post.service.PostReadService;
import com.example.fasns.domain.post.service.PostWriteService;
import com.example.fasns.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import utils.CursorRequest;
import utils.PageCursor;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostReadService postReadService;
    private final PostWriteService postWriteService;
    private final GetTimelinePostUseCase getTimelinePostUseCase;
    private final CreatePostUseCase createPostUseCase;
    private final CreatePostLikeUseCase postLikeUseCase;

    @PostMapping
    public Response<Long> create(@AuthenticationPrincipal MemberDetail member,
                                 @RequestBody String content) {
        return Response.success(createPostUseCase.execute(member.getUsername(), content), CREATED);
    }

    @PostMapping("/{postId}/change/content")
    public Response<PostDto> updateContent(@AuthenticationPrincipal MemberDetail member,
                                           @PathVariable Long postId,
                                           @RequestBody String content) {
        postWriteService.update(member.getMember().getId(), postId, content);
        return Response.success(OK);
    }

    @GetMapping("/daily-post-counts")
    public Response<List<DailyPostCountDto>> getDailyPostCount(DailyPostCountRequest request) {
        return Response.success(postReadService.getDailyPostCount(request), OK);
    }

    @GetMapping("/members")
    public Response<Page<PostDto>> getPosts(@AuthenticationPrincipal MemberDetail member, Pageable pageable) {
        return Response.success(postReadService.getPosts(member.getMember().getId(), pageable), OK);
    }

    @GetMapping("/members/cursor")
    public Response<PageCursor<PostDto>> getPostsByCursor(@AuthenticationPrincipal MemberDetail member, CursorRequest cursorRequest) {
        return Response.success(postReadService.getPosts(member.getMember().getId(), cursorRequest), OK);
    }

    @GetMapping("/members/timeline")
    public Response<PageCursor<PostDto>> getTimeline(@AuthenticationPrincipal MemberDetail member, CursorRequest cursorRequest) {
        return Response.success(getTimelinePostUseCase.executeByTimeline(member.getMember().getId(), cursorRequest), OK);
    }

    @GetMapping("/{postId}")
    public Response<PostDto> getPost(@PathVariable Long postId) {
        return Response.success(postReadService.getPost(postId), OK);
    }

    @PostMapping("/{postId}/like/v1")
    public Response<Void> likePost(@PathVariable Long postId) {
        postWriteService.likePostWithOptimisticLock(postId);
        return Response.success(OK);
    }

    @PostMapping("/{postId}/like/v2")
    public Response<Void> likePostV2(@AuthenticationPrincipal MemberDetail member,
                                     @PathVariable Long postId) {
        postLikeUseCase.execute(member.getMember().getId(), postId);
        return Response.success(OK);
    }

}
