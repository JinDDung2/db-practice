package com.example.fasns.domain.member.entity;

import com.example.fasns.domain.member.util.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

    @Test
    @DisplayName("회원 닉네임을 변경")
    void ChangeName() throws Exception {
        // given
        Member member = MemberFixtureFactory.create();
        // when
        String newNickname = "heogak";
        member.changeNickname(newNickname);

        // then
        Assertions.assertEquals("heogak", member.getNickname());
    }

    @Test
    @DisplayName("닉네임 변경 실패 - 10글자 이상")
    void ChangeNameMaxLength() throws Exception {
        // given
        Member member = MemberFixtureFactory.create();
        // when
        String newNickname = "heogakheogak";
        // then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> member.changeNickname(newNickname));
    }

}