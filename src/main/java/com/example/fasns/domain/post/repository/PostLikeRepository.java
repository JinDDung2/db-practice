package com.example.fasns.domain.post.repository;

import com.example.fasns.domain.post.entity.PostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String TABLE = "PostLike";

    private static final RowMapper<PostLike> ROW_MAPPER = ((rs, rowNum) -> PostLike.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .postId(rs.getLong("postId"))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build()
    );


    public Long countByPostId(Long postId) {
        String sql = String.format("SELECT count(id) FROM %s WHERE postId = :postId", TABLE);

        SqlParameterSource params = new MapSqlParameterSource().addValue("postId", postId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public PostLike save(PostLike postLike) {
        if (postLike.getId() == null) {
            return insert(postLike);
        }
        throw new UnsupportedOperationException("좋아요는 업데이트를 지원하지 않습니다.");
    }

    public void bulkInsert(List<PostLike> timelines) {
        String sql = String.format("INSERT INTO %s (memberId, postId , createdAt) \n" +
                "VALUES (:memberId, :postId, :createdAt)", TABLE);

        SqlParameterSource[] params = timelines.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    private PostLike insert(PostLike postLike) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(postLike);
        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return PostLike.builder()
                .id(id)
                .memberId(postLike.getMemberId())
                .postId(postLike.getPostId())
                .createdAt(postLike.getCreatedAt())
                .build();
    }
}
