package com.example.fasns.domain.follow.service;

import com.example.fasns.domain.follow.dto.FollowDto;
import com.example.fasns.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  FollowReadService {

    private final FollowRepository followRepository;

    public List<FollowDto> getFollowings(Long memberId) {
        return followRepository.findAllByFromMemberId(memberId).stream()
                .map(FollowDto::toDto)
                .collect(Collectors.toList());
    }
}
