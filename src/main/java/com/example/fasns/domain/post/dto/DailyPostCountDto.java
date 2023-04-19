package com.example.fasns.domain.post.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailyPostCountDto {
    private final Long memberId;
    private final LocalDate createdDate;
    private final Long count;

    public DailyPostCountDto(Long memberId, LocalDate createdDate, Long count) {
        this.memberId = memberId;
        this.createdDate = createdDate;
        this.count = count;
    }
}
