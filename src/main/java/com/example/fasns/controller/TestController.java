package com.example.fasns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String testHello() {
        return "hello" + System.currentTimeMillis();
    }

}
