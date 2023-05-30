package com.example.fasns.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class MemberDto {
    private Long id;
    private String nickname;
    private String email;
    private LocalDate birth;

    public MemberDto() {}

    @Builder
    public MemberDto(Long id, String nickname, String email, LocalDate birth) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.birth = birth;
    }
}
