package com.example.fasns.application.usecase;

import com.example.fasns.domain.post.dto.PostDto;
import com.example.fasns.domain.post.service.PostReadService;
import com.example.fasns.domain.post.service.PostWriteService;
import com.example.fasns.domain.post.service.TimelineWriteService;
import com.example.fasns.global.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fasns.global.exception.ErrorCode.NOT_PERMISSION_FOR_CORRESPONDING_POST;

@Service
@RequiredArgsConstructor
public class DeletePostTimelineUseCase {

    private final PostReadService postReadService;
    private final PostWriteService postWriteService;
    private final TimelineWriteService timelineWriteService;

    @Transactional
    @Caching(evict = {@CacheEvict(cacheNames = "post", key = "#postId"),
                      @CacheEvict(cacheNames = "feed", key = "#memberId")})
    public void execute(Long memberId, Long postId) {
        PostDto post = postReadService.getPost(postId);
        if (!post.getMemberId().equals(memberId)) {
            throw new SystemException(NOT_PERMISSION_FOR_CORRESPONDING_POST);
        }
        timelineWriteService.delete(postId);
        postWriteService.delete(memberId, postId);
    }

}
