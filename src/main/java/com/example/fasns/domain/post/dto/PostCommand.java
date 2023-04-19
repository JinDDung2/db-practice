package com.example.fasns.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostCommand {
    private Long memberId;
    private String title;
    private String contents;

    public PostCommand(Long memberId, String title, String contents) {
        this.memberId = memberId;
        this.title = title;
        this.contents = contents;
    }
}
