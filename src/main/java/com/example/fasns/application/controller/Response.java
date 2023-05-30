package com.example.fasns.application.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response<T> {
    private String result;
    private HttpStatus httpStatus;
    private T data;

    public Response(String result) {
        this.result = result;
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(String result, HttpStatus httpStatus, T data) {
        this.result = result;
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public Response(String result, HttpStatus httpStatus) {
        this.result = result;
        this.httpStatus = httpStatus;
    }

    public Response(String result, T data) {
        this.result = result;
        this.data = data;
    }

    public static <T> Response<T> error(T data) {
        return new Response<>("fail", data);
    }
    public static <T> Response error() {
        return new Response<>("fail");
    }

    public static <T> Response<T> success(T data, HttpStatus httpStatus) {
        return new Response<>("success", httpStatus, data);
    }
    public static <T> Response success(HttpStatus httpStatus) {
        return new Response<>("success", httpStatus);
    }
}
