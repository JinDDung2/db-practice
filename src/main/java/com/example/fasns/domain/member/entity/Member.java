package com.example.fasns.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {
    private final Long id;

    private String nickname;

    private String email;

    private final LocalDate birth;

    private final LocalDateTime createdAt;

    private static final Integer MAX_NICKNAME_LENGTH = 10;

    @Builder
    public Member(Long id, String nickname, String email, LocalDate birth, LocalDateTime createdAt) {
        this.id = id;

        validateNickname(nickname);
        this.nickname = Objects.requireNonNull(nickname);

        this.email = Objects.requireNonNull(email);
        this.birth = Objects.requireNonNull(birth);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public void changeNickname(String nickname) {
        // null 아니여야 함
        Objects.requireNonNull(nickname);
        // 10글자 초과하면 안됨
        validateNickname(nickname);
        this.nickname = nickname;
    }

    public void validateNickname(String nickname) {
        Assert.isTrue(nickname.length() <= MAX_NICKNAME_LENGTH, "닉네임 최대 길이는 10글자 입니다.");
    }
}
