package com.example.fasns.application.controller;

import com.example.fasns.domain.post.dto.PostCommand;
import com.example.fasns.domain.post.dto.PostDto;
import com.example.fasns.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostWriteService postWriteService;

    @PostMapping
    public PostDto create(PostCommand command) {
        return postWriteService.create(command);
    }
}
