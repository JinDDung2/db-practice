package com.example.fasns.domain.member.dto;

import com.example.fasns.domain.member.entity.MemberNicknameHistory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class MemberNicknameHistoryDto {
    private Long id;
    private Long memberId;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Builder
    public MemberNicknameHistoryDto(Long id, Long memberId, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static MemberNicknameHistoryDto toDto(MemberNicknameHistory history) {
        return MemberNicknameHistoryDto.builder()
                .id(history.getId())
                .memberId(history.getMemberId())
                .nickname(history.getNickname())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
