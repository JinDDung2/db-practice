package com.example.fasns.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class MemberRegisterDto {
    private String email;
    private String password;
    private String nickname;
    private LocalDate birth;

}
