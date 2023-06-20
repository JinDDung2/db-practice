package com.example.fasns.domain.member.service;

import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.repository.MemberRepository;
import com.example.fasns.enums.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MemberWriteServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberWriteService memberWriteService;

    @Test
    @DisplayName("맴버 역할 변경")
    void changeRole() throws Exception {
        Member admin = memberRepository.findByEmail("admin@naver.com").get();
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            if (!member.getMemberRole().equals(MemberRole.ROLE_INFLUENCED)) {
                memberWriteService.upgradeMemberRole(admin, member.getId());
            }
        }
    }
}
