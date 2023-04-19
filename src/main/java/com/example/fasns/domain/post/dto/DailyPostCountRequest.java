package com.example.fasns.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
public class DailyPostCountRequest {

    private final Long memberId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate firstDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate lastDate;

    public DailyPostCountRequest(Long memberId, LocalDate firstDate, LocalDate lastDate) {
        this.memberId = memberId;
        this.firstDate = firstDate;
        this.lastDate = lastDate;
    }
}
