package com.example.fasns.domain.post.service;

import com.example.fasns.domain.post.entity.Timeline;
import com.example.fasns.domain.post.repository.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimelineWriteService {

    private final TimelineRepository timelineRepository;

    /**
     * 포스트 작성시 포스트 작성 유저를 팔로우한 유저들에게 포스트 전달하기
     */
    public void deliveryToTimeline(Long postId, List<Long> toMemberIds) {
        List<Timeline> timelines = toMemberIds.stream()
                .map(memberId -> Timeline.builder()
                        .memberId(memberId)
                        .postId(postId)
                        .build())
                .collect(Collectors.toList());

        timelineRepository.bulkInsert(timelines);
    }

}
