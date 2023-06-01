package com.example.fasns.domain.post.service;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.repository.PostRepository;
import com.example.fasns.global.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fasns.global.exception.ErrorCode.NOT_PERMISSION_FOR_CORRESPONDING_POST;
import static com.example.fasns.global.exception.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;

    public Long create(MemberDto member, String content) {
        Post post = Post.builder()
                .memberId(member.getId())
                .contents(content)
                .build();
        return postRepository.save(post).getId();
    }

    @Caching(evict = {@CacheEvict(cacheNames = "POST", key = "#postId"),
                      @CacheEvict(cacheNames = "FEED", key = "#memberId")})
    public void update(Long memberId, Long postId, String content) {
        Post post = validatePostById(postId, false);
        if (!post.getMemberId().equals(memberId)) {
            throw new SystemException(NOT_PERMISSION_FOR_CORRESPONDING_POST);
        }
        post.updateContent(content);
        postRepository.save(post);
    }

    /*
     * 동시성 발생하기 좋은 구간
     * 1. 조회하고
     * 2. 연산이 일어나고 (=변경하고)
     * 3. 저장한다.
     */
    @Transactional
    public void likePost(Long postId) {
        // 비관적 락은 트랜잭션도 걸어줘야하고, requiredLock도 걸어줘야함
        Post post = validatePostById(postId, true);
        post.increaseLikeCount();
        postRepository.save(post);
    }

    public void likePostWithOptimisticLock(Long postId) {
        Post post = validatePostById(postId, false);
        post.increaseLikeCount();
        postRepository.save(post);
    }

    private Post validatePostById(Long postId, boolean requiredLock) {
        return postRepository.findById(postId, requiredLock).orElseThrow(() -> new SystemException(POST_NOT_FOUND));
    }
}
