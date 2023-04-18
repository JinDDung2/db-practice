package com.example.fasns.domain.follow.dto;

import com.example.fasns.domain.follow.entity.Follow;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FollowDto {

    private Long id;
    private Long fromMemberId;
    private Long toMemberId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Builder
    public FollowDto(Long id, Long fromMemberId, Long toMemberId, LocalDateTime createdAt) {
        this.id = id;
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.createdAt = createdAt;
    }

    public static FollowDto toDto(Follow follow) {
        return FollowDto.builder()
                .id(follow.getId())
                .fromMemberId(follow.getFromMemberId())
                .toMemberId(follow.getToMemberId())
                .createdAt(follow.getCreatedAt())
                .build();
    }

}
