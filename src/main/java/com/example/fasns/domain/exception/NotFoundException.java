package com.example.fasns.domain.exception;

import com.example.fasns.common.ErrorCode;
import com.example.fasns.common.SystemException;

public class NotFoundException extends SystemException {

    public NotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
