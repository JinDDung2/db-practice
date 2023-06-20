package com.example.fasns.domain.member.entity;

import com.example.fasns.enums.MemberRole;
import com.example.fasns.global.exception.ErrorCode;
import com.example.fasns.global.exception.SystemException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.fasns.enums.MemberRole.*;

@Getter
public class Member {
    private final Long id;

    private String nickname;

    private final String email;

    private String password;

    private final LocalDate birth;

    private final LocalDateTime createdAt;

    private MemberRole memberRole;

    private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지

    private String providerId;  // oauth2를 이용할 경우 아이디값

    private static final Integer MAX_NICKNAME_LENGTH = 10;

    @Builder
    public Member(Long id, String nickname, String email, String password, LocalDate birth, LocalDateTime createdAt,
                  MemberRole memberRole, String provider, String providerId) {
        this.id = id;
        validateNickname(nickname);
        this.nickname = Objects.requireNonNull(nickname);
        this.password = Objects.requireNonNull(password);
        this.email = Objects.requireNonNull(email);
        this.birth = Objects.requireNonNull(birth);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.memberRole = memberRole;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void changeNickname(String nickname) {
        // null 아니여야 함
        Objects.requireNonNull(nickname);
        // 10글자 초과하면 안됨
        validateNickname(nickname);
        this.nickname = nickname;
    }

    public void changePassword(PasswordEncoder encoder, String password) {
        if (encoder.matches(password, this.password)) {
            throw new SystemException(ErrorCode.PASSWORD_IS_SAME_BEFORE_PASSWORD);
        }
        this.password = encoder.encode(password);
    }

    public void addMemberRole() {
        if (this.memberRole == null) this.memberRole = ROLE_BASIC;
    }
    public void upgradeRole() {
        MemberRole nextLevel = this.memberRole.getNextLevel();
        if (nextLevel == null) {
            throw new SystemException(ErrorCode.NONE_NEXT_ROLE);
        } else {
            this.memberRole = nextLevel;
        }
    }

    public void validateNickname(String nickname) {
        Assert.isTrue(nickname.length() <= MAX_NICKNAME_LENGTH, "닉네임 최대 길이는 10글자 입니다.");
    }
}
