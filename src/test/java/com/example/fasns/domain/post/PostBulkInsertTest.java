package com.example.fasns.domain.post;

import com.example.fasns.domain.post.entity.Post;
import com.example.fasns.domain.post.repository.PostRepository;
import com.example.fasns.util.PostFixtureFactory;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {

    @Autowired
    private PostRepository postRepository;
    
    @Test
    @DisplayName("100만건 데이터 넣기")
    void bulkInsert() throws Exception {
        EasyRandom easyRandom = PostFixtureFactory.get(2L,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 2, 1)
        );

        StopWatch objectStopWatch = new StopWatch();
        objectStopWatch.start();

        // 100만건 객체 생성 시간 : 7.000445625
        List<Post> posts = IntStream.range(0, 10000 * 100)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .collect(Collectors.toList());


        objectStopWatch.stop();
        System.out.println("객체 생성 시간 : " + objectStopWatch.getTotalTimeSeconds());

        StopWatch queryStopWatch = new StopWatch();
        queryStopWatch.start();

        // 쿼리 시간 : 47.013433417
        postRepository.bulkInsert(posts);

        queryStopWatch.stop();
        System.out.println("쿼리 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }
}
