package com.example.fasns.domain.follow.service;

import com.example.fasns.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowReadService {

    private final FollowRepository followRepository;

}
