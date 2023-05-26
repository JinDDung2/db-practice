package com.example.fasns.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
}
