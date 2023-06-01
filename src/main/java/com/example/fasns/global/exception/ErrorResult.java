package com.example.fasns.global.exception;

import lombok.Getter;

@Getter
public class ErrorResult {

    private ErrorCode errorCode;
    private String message;

    public ErrorResult(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
