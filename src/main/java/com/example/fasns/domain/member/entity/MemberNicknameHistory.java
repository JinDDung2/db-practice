package com.example.fasns.domain.member.entity;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

public class MemberNicknameHistory {
    private final Long id;

    private final Long memberId;
    private final String nickname;

    private final LocalDateTime createdAt;

    @Builder
    public MemberNicknameHistory(Long id, Long memberId, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.nickname = Objects.requireNonNull(nickname);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
