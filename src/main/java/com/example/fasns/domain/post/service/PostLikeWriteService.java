package com.example.fasns.domain.post.service;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.post.dto.PostDto;
import com.example.fasns.domain.post.entity.PostLike;
import com.example.fasns.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeWriteService {

    private final PostLikeRepository postLikeRepository;

    public Long create(PostDto post, MemberDto memberDto) {
        PostLike postLike = PostLike.builder()
                .memberId(memberDto.getId())
                .postId(post.getId())
                .build();
        return postLikeRepository.save(postLike).getId();
    }

}
