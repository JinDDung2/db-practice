package com.example.fasns.domain.post.service;

import com.example.fasns.domain.post.entity.Timeline;
import com.example.fasns.domain.post.repository.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utils.CursorRequest;
import utils.PageCursor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineReadService {

    private final TimelineRepository timelineRepository;

    public PageCursor<Timeline> getTimelines(Long memberId, CursorRequest cursorRequest) {
        List<Timeline> timelines = findAllBy(memberId, cursorRequest);
        long nextKey = timelines.stream()
                .mapToLong(Timeline::getId)
                .min().orElse(CursorRequest.NONE_KEY);
        return new PageCursor<>(cursorRequest.next(nextKey), timelines);
    }

    private List<Timeline> findAllBy(Long memberId, CursorRequest cursorRequest) {
         if (cursorRequest.hasKey()) {
             return timelineRepository.findAllByLessThanIdAndMemberIdInAndOrderByIdDesc(cursorRequest.getKey(), memberId, cursorRequest.getSize());
         }
        return timelineRepository.findAllByMemberIdAndOrderByIds(memberId, cursorRequest.getSize());
    }

}
