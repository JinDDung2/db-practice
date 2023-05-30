package com.example.fasns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FasnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FasnsApplication.class, args);
    }

}
