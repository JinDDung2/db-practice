package com.example.fasns.domain.post.service;

import com.example.fasns.common.SystemException;
import com.example.fasns.domain.post.dto.PostCommand;
import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fasns.common.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;

    public Long create(PostCommand command) {
        Post post = Post.builder()
                .memberId(command.getMemberId())
                .title(command.getTitle())
                .contents(command.getContents())
                .build();
        return postRepository.save(post).getId();
    }

    /*
     * 동시성 발생하기 좋은 구간
     * 1. 조회하고
     * 2. 연산이 일어나고 (=변경하고)
     * 3. 저장한다.
     */
    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId, true).orElseThrow(() -> new SystemException(POST_NOT_FOUND));
        post.increaseLikeCount();
        postRepository.save(post);
    }
}
