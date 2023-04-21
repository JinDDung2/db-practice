package com.example.fasns.application.controller;

import com.example.fasns.application.usecase.CreatePostUseCase;
import com.example.fasns.application.usecase.GetTimelinePostUseCase;
import com.example.fasns.domain.post.dto.DailyPostCountDto;
import com.example.fasns.domain.post.dto.DailyPostCountRequest;
import com.example.fasns.domain.post.dto.PostCommand;
import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.service.PostReadService;
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
    private final GetTimelinePostUseCase getTimelinePostUseCase;
    private final CreatePostUseCase createPostUseCase;

    @PostMapping
    public Long create(@RequestBody PostCommand command) {
        return createPostUseCase.execute(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCountDto> getDailyPostCount(DailyPostCountRequest request) {
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<Post> getPosts(@PathVariable Long memberId, Pageable pageable) {
        return postReadService.getPosts(memberId, pageable);
    }

    @GetMapping("/members/{memberId}/cusror")
    public PageCursor<Post> getPostsByCursor(@PathVariable Long memberId, CursorRequest cursorRequest) {
        return postReadService.getPosts(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeline(@PathVariable Long memberId, CursorRequest cursorRequest) {
        return getTimelinePostUseCase.executeByTimeline(memberId, cursorRequest);
    }
}
