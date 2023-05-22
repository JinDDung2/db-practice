package com.example.fasns.application.usecase;

import com.example.fasns.domain.follow.entity.Follow;
import com.example.fasns.domain.follow.repository.FollowRepository;
import com.example.fasns.domain.member.dto.MemberDto;
import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class GetFollowMemberUseCaseTest {

    @Autowired
    GetFollowMemberUseCase useCase;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FollowRepository followRepository;

    @Test
    @DisplayName("팔로잉 목록 조회")
    public void getFollowMember() {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .nickname("member1")
                .email("e1")
                .birth(LocalDate.now())
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .nickname("member2")
                .email("e2")
                .birth(LocalDate.now())
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        Follow follow = Follow.builder()
                .fromMemberId(member1.getId())
                .toMemberId(member2.getId())
                .build();
        // when
        followRepository.save(follow);
        // then
        List<MemberDto> followings = useCase.execute(follow.getToMemberId());
        for (MemberDto following : followings) {
            System.out.println("id: " + following.getId());
        }
    }

}