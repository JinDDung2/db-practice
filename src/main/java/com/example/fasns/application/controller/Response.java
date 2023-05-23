package com.example.fasns.application.controller;

import lombok.Getter;

@Getter
public class Response<T> {
    private String result;
    private T data;

    public Response(String result) {
        this.result = result;
    }

    public Response(T data) {
        this.data = data;
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

    public static <T> Response<T> success(T data) {
        return new Response<>("success", data);
    }
    public static <T> Response success() {
        return new Response<>("success");
    }
}
