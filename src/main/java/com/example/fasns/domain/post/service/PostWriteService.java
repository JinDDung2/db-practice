package com.example.fasns.domain.post.service;

import com.example.fasns.domain.post.dto.PostCommand;
import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
