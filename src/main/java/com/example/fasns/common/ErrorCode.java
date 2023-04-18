package com.example.fasns.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common Error
    COMMON_SYSTEM_ERROR("오류가 발생했습니다. 잠시 후 다시 시도해주세요"), //장애 상황
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다."),
    COMMON_METHOD_NOT_ALLOWED("지원하지 않는 HTTP METHOD 입니다."),
    COMMON_UNSUPPORTED_MEDIA_TYPE("지원하지 않는 MEDIA TYPE 입니다."),

    UN_AUTHENTICATED("인증되지 않은 접근입니다."),
    UNAUTHORIZED_ROLE("현재 유저 권한으로는 접근할 수 없는 리소스 요청입니다."),

    // User Error
    USER_NOT_FOUND("회원을 찾을 수 없습니다."),
    INVALID_USER("유효하지 않은 회원입니다."),
    INVALID_TOKEN("유효하지 않은 인증 토큰입니다."),
    DUPLICATED_EMAIL("중복된 이메일 입니다."),
    PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}