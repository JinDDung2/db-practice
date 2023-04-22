package com.example.fasns.application.usecase;

import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.service.MemberReadService;
import com.example.fasns.domain.post.dto.PostDto;
import com.example.fasns.domain.post.service.PostLikeWriteService;
import com.example.fasns.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostLikeUseCase {

    private final PostReadService postReadService;
    private final MemberReadService memberReadService;
    private final PostLikeWriteService postLikeWriteService;

    public void execute(Long postId, Long memberId) {
        PostDto post = postReadService.getPost(postId);
        MemberDto member = memberReadService.getMember(memberId);

        postLikeWriteService.create(post.toEntity(), member);
    }

}
