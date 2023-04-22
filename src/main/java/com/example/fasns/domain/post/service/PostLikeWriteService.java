package com.example.fasns.domain.post.service;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.entity.PostLike;
import com.example.fasns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeWriteService {

    private final PostRepository postLikeRepository;

    public Long create(Post post, MemberDto memberDto) {
        PostLike postLike = PostLike.builder()
                .memberId(memberDto.getId())
                .postId(post.getId())
                .build();
        return postLikeRepository.save(post).getId();
    }

    /*
     * 동시성 발생하기 좋은 구간
     * 1. 조회하고
     * 2. 연산이 일어나고 (=변경하고)
     * 3. 저장한다.
     */

}
