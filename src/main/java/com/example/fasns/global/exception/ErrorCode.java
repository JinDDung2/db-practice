package com.example.fasns.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    // Common Error
    COMMON_SYSTEM_ERROR("오류가 발생했습니다. 잠시 후 다시 시도해주세요", INTERNAL_SERVER_ERROR), //장애 상황
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다.", BAD_REQUEST),
    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다.", BAD_REQUEST),
    COMMON_METHOD_NOT_ALLOWED("지원하지 않는 HTTP METHOD 입니다.", HTTP_VERSION_NOT_SUPPORTED),
    COMMON_UNSUPPORTED_MEDIA_TYPE("지원하지 않는 MEDIA TYPE 입니다.", UNSUPPORTED_MEDIA_TYPE),
    UPDATE_COUNT_ZERO("업데이트를 실패했습니다.", BAD_REQUEST),

    UN_AUTHENTICATED("인증되지 않은 접근입니다.", UNAUTHORIZED),
    UNAUTHORIZED_ROLE("현재 유저 권한으로는 접근할 수 없는 리소스 요청입니다.", FORBIDDEN),

    // User Error
    USER_NOT_FOUND("회원을 찾을 수 없습니다.", BAD_REQUEST),
    INVALID_USER("유효하지 않은 회원입니다.", BAD_REQUEST),
    INVALID_TOKEN("유효하지 않은 인증 토큰입니다.", UNAUTHORIZED),
    DUPLICATED_EMAIL("중복된 이메일 입니다.", CONFLICT),
    PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다.", BAD_REQUEST),
    PASSWORD_IS_SAME_BEFORE_PASSWORD("이전 비밀번호와 같습니다.", BAD_REQUEST),

    // Post Error
    POST_NOT_FOUND("게시글을 찾을 수 없습니다.", BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
