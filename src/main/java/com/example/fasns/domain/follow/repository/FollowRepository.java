package com.example.fasns.domain.follow.repository;

import com.example.fasns.domain.follow.entity.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

    static final String TABLE = "Follow";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<Follow> ROW_MAPPER = (ResultSet rs, int rowNums) -> Follow.builder()
            .id(rs.getLong("id"))
            .fromMemberId(rs.getLong("fromMemberId"))
            .toMemberId(rs.getLong("toMemberId"))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<Follow> findAllByFromMemberId(Long fromMemberId) {
        String sql = String.format("SELECT * FROM %s WHERE fromMemberId = :fromMemberId", TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("fromMemberId", fromMemberId);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Follow> findAllByToMemberId(Long toMemberId) {
        String sql = String.format("SELECT * FROM %s WHERE toMemberId = :toMemberId", TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("toMemberId", toMemberId);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public Follow save(Follow follow) {
        if (follow.getId() == null) {
            return insert(follow);
        }

        throw new UnsupportedOperationException("Follow는 업데이트를 지원하지 않습니다.");
    }

    public void delete(Long fromMemberId, Long toMemberId) {
        String sql = String.format("DELETE FROM %s WHERE fromMemberId = :fromMemberId AND toMemberId = :toMemberId", TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("fromMemberId", fromMemberId)
                .addValue("toMemberId", toMemberId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    private Follow insert(Follow follow) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(follow);

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Follow.builder()
                .id(id)
                .fromMemberId(follow.getFromMemberId())
                .toMemberId(follow.getToMemberId())
                .createdAt(follow.getCreatedAt())
                .build();
    }

}
