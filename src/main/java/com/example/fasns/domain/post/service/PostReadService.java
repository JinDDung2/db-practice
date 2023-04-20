package com.example.fasns.domain.post.service;

import com.example.fasns.domain.post.dto.DailyPostCountDto;
import com.example.fasns.domain.post.dto.DailyPostCountRequest;
import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import utils.CursorRequest;
import utils.PageCursor;

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

    public Page<Post> getPosts(Long memberId, Pageable pageable) {
        return postRepository.findAllByMemberId(memberId, pageable);
    }

    public PageCursor<Post> getPosts(Long memberId, CursorRequest request) {
        /*
        SELECT *
        FROM POST
        WHERE memberId = :memberId and id > key // 단 key IS NULL 경우도 생각(맨처음)
         */
        List<Post> posts = findAll(memberId, request);
        long nextKey = getNextKey(posts);

        return new PageCursor<>(request.next(nextKey), posts);
    }

    public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest request) {
        /*
        SELECT *
        FROM POST
        WHERE memberId = :memberId and id > key // 단 key IS NULL 경우도 생각(맨처음)
         */
        List<Post> posts = findAll(memberIds , request);
        long nextKey = getNextKey(posts);

        return new PageCursor<>(request.next(nextKey), posts);
    }

    private long getNextKey(List<Post> posts) {
        return posts.stream()
                .mapToLong(Post::getId).min()
                .orElse(CursorRequest.NONE_KEY);
    }

    private List<Post> findAll(Long memberId, CursorRequest request) {
        if (request.hasKey()) {
            return postRepository.findAllByLessThanIdAndMemberIdInAndOrderByIdDesc(request.getKey(), memberId, request.getSize());
        }

        return postRepository.findAllByMemberIdInAndOrderByIdDesc(memberId, request.getSize());
    }

    private List<Post> findAll(List<Long> memberIds, CursorRequest request) {
        if (request.hasKey()) {
            return postRepository.findAllByLessThanIdAndMemberIdInAndOrderByIdDesc(request.getKey(), memberIds, request.getSize());
        }

        return postRepository.findAllByMemberIdInAndOrderByIdDesc(memberIds, request.getSize());
    }

}
