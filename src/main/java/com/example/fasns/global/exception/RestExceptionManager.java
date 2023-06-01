package com.example.fasns.global.exception;

import com.example.fasns.application.controller.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Optional;

import static com.example.fasns.global.exception.ErrorCode.COMMON_SYSTEM_ERROR;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class RestExceptionManager {

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<?> springBootAppException(SystemException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error(new ErrorResult(e.getErrorCode(), e.getMessage())));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlExceptionHandler(SQLException e) {
        ErrorResult errorResult = new ErrorResult(COMMON_SYSTEM_ERROR, e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(Response.error(new ErrorResult(errorResult.getErrorCode(), errorResult.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptionHandler(MethodArgumentNotValidException e) {
        String message = Optional.ofNullable(e.getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse("");

        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(Response.error(message));
    }
}
