package com.example.fasns.application.controller;

import com.example.fasns.application.usecase.CreatePostLikeUseCase;
import com.example.fasns.application.usecase.CreatePostUseCase;
import com.example.fasns.application.usecase.GetTimelinePostUseCase;
import com.example.fasns.domain.post.dto.DailyPostCountDto;
import com.example.fasns.domain.post.dto.DailyPostCountRequest;
import com.example.fasns.domain.post.dto.PostCommand;
import com.example.fasns.domain.post.dto.PostDto;
import com.example.fasns.domain.post.service.PostReadService;
import com.example.fasns.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import utils.CursorRequest;
import utils.PageCursor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostReadService postReadService;
    private final PostWriteService postWriteService;
    private final GetTimelinePostUseCase getTimelinePostUseCase;
    private final CreatePostUseCase createPostUseCase;
    private final CreatePostLikeUseCase postLikeUseCase;

    @PostMapping
    public Long create(@RequestBody PostCommand command) {
        return createPostUseCase.execute(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCountDto> getDailyPostCount(DailyPostCountRequest request) {
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<PostDto> getPosts(@PathVariable Long memberId, Pageable pageable) {
        return postReadService.getPosts(memberId, pageable);
    }

    @GetMapping("/members/{memberId}/cursor")
    public PageCursor<PostDto> getPostsByCursor(@PathVariable Long memberId, CursorRequest cursorRequest) {
        return postReadService.getPosts(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<PostDto> getTimeline(@PathVariable Long memberId, CursorRequest cursorRequest) {
        return getTimelinePostUseCase.executeByTimeline(memberId, cursorRequest);
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable Long postId) {
        return postReadService.getPost(postId);
    }

    @PostMapping("/{postId}/like/v1")
    public void likePost(@PathVariable Long postId) {
//        postWriteService.likePost(postId);
        postWriteService.likePostWithOptimisticLock(postId);
    }

    @PostMapping("/{postId}/like/v2")
    public void likePostV2(@PathVariable Long postId,
                           @RequestParam Long memberId) {
//        postWriteService.likePost(postId);
        postLikeUseCase.execute(postId,  memberId);
    }
}
