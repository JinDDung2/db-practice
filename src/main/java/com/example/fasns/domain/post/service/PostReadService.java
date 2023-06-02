package com.example.fasns.domain.post.service;

import com.example.fasns.domain.post.dto.DailyPostCountDto;
import com.example.fasns.domain.post.dto.DailyPostCountRequest;
import com.example.fasns.domain.post.dto.PostDto;
import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.repository.PostLikeRepository;
import com.example.fasns.domain.post.repository.PostRepository;
import com.example.fasns.global.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import utils.CursorRequest;
import utils.PageCursor;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.fasns.global.exception.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public List<DailyPostCountDto> getDailyPostCount(DailyPostCountRequest request) {
        /*
        return [작성일자, 작성회원, 작성 게시물 갯수]
        select createdDate, memberId, count(id) from Post
        where memberId = :memberId and createdDate between firstDate and lastDate
        group by createdDate, memberId
         */
        List<DailyPostCountDto> dailyPostCountList = postRepository.groupByCreatedDate(request);

        if (dailyPostCountList.isEmpty()) {
            throw new SystemException(POST_NOT_FOUND);
        }
        return dailyPostCountList;
    }

    @Cacheable(cacheNames = "post", key = "#postId")
    public PostDto getPost(Long postId) {
        return toDto(postRepository.findById(postId, false).orElseThrow(() ->
                new SystemException(POST_NOT_FOUND)));
    }

    public Page<PostDto> getPosts(Long memberId, Pageable pageable) {
        Page<Post> postPages = postRepository.findAllByMemberId(memberId, pageable);
        if (postPages.isEmpty()) {
            throw new SystemException(POST_NOT_FOUND);
        }
        return postPages.map(this::toDto);
    }

    @Cacheable(cacheNames = "feed", key = "#memberId")
    public PageCursor<PostDto> getPosts(Long memberId, CursorRequest request) {
        /*
        SELECT *
        FROM POST
        WHERE memberId = :memberId and id > key // 단 key IS NULL 경우도 생각(맨처음)
         */
        List<PostDto> posts = findAll(memberId, request).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        long nextKey = getNextKey(posts);

        return new PageCursor<>(request.next(nextKey), posts);
    }

    public PageCursor<PostDto> getPosts(List<Long> memberIds, CursorRequest request) {
        /*
        SELECT *
        FROM POST
        WHERE memberId = :memberId and id > key // 단 key IS NULL 경우도 생각(맨처음)
         */
        List<PostDto> posts = findAll(memberIds, request).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        long nextKey = getNextKey(posts);

        return new PageCursor<>(request.next(nextKey), posts);
    }

    public List<PostDto> getPosts(List<Long> ids) {
        List<PostDto> postList = postRepository.findAllByInId(ids).stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (postList.isEmpty()) {
            throw new SystemException(POST_NOT_FOUND);
        }

        return postList;
    }

    private long getNextKey(List<PostDto> posts) {
        return posts.stream()
                .mapToLong(PostDto::getId).min()
                .orElse(CursorRequest.NONE_KEY);
    }

    private List<Post> findAll(Long memberId, CursorRequest request) {
        if (request.hasKey()) {
            return postRepository.findAllByLessThanIdAndMemberIdInAndOrderByIdDesc(request.getKey(), memberId, request.getSize());
        }

        List<Post> postList = postRepository.findAllByMemberIdInAndOrderByIdDesc(memberId, request.getSize());
        if (postList.isEmpty()) {
            throw new SystemException(POST_NOT_FOUND);
        }

        return postList;
    }

    private List<Post> findAll(List<Long> memberIds, CursorRequest request) {
        if (request.hasKey())
            return postRepository.findAllByLessThanIdAndMemberIdInAndOrderByIdDesc(request.getKey(), memberIds, request.getSize());

        List<Post> postList = postRepository.findAllByMemberIdInAndOrderByIdDesc(memberIds, request.getSize());
        if (postList.isEmpty()) {
            throw new SystemException(POST_NOT_FOUND);
        }

        return postList;
    }

    private PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .likeCount(postLikeRepository.countByPostId(post.getId()))
                .createdAt(post.getCreatedAt())
                .createdDate(post.getCreatedDate())
                .build();
    }

}
