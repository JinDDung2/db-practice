package com.example.fasns.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
