package com.example.fasns.domain.member.util;

import com.example.fasns.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFixtureFactory {
    public static Member create() {
        // seed를 다르게 줘야 다른 객체가 생성
        EasyRandomParameters params = new EasyRandomParameters();
        return new EasyRandom(params).nextObject(Member.class);
    }

    public static Member create(Long seed) {
        // seed를 다르게 줘야 다른 객체가 생성
        EasyRandomParameters params = new EasyRandomParameters()
                .seed(seed);
        return new EasyRandom(params).nextObject(Member.class);
    }
}
