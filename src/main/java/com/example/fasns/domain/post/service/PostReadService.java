package com.example.fasns.domain.post.service;

import com.example.fasns.domain.post.dto.DailyPostCountDto;
import com.example.fasns.domain.post.dto.DailyPostCountRequest;
import com.example.fasns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;

    public List<DailyPostCountDto> getDailyPostCount(DailyPostCountRequest request) {
        /*
        return [작성일자, 작성회원, 작성 게시물 갯수]
        select createdDate, memberId, count(id) from Post
        where memberId = :memberId and createdDate between firstDate and lastDate
        group by createdDate, memberId
         */
        return postRepository.groupByCreatedDate(request);
    }

}
