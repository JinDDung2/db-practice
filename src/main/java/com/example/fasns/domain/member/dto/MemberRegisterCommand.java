package com.example.fasns.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class MemberRegisterCommand {
    private String email;
    private String nickname;
    private LocalDate birth;

}
