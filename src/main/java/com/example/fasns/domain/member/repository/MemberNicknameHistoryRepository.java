package com.example.fasns.domain.member.repository;

import com.example.fasns.domain.member.entity.MemberNicknameHistory;
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
public class MemberNicknameHistoryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String TABLE = "MemberNicknameHistory";

    private static final RowMapper<MemberNicknameHistory> rowMapper = (ResultSet rs, int rowNums) -> MemberNicknameHistory.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .nickname(rs.getString("nickname"))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<MemberNicknameHistory> findAllByMemberId(Long memberId) {
        String sql = String.format("SELECT * FROM %s WHERE memberId = :memberId", TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("memberId", memberId);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public MemberNicknameHistory save(MemberNicknameHistory history) {
        if (history.getId() == null) {
            return insert(history);
        }
        throw new UnsupportedOperationException("MemberNicknameHistory은 업데이트를 지원하지 않습니다.");
    }

    private MemberNicknameHistory insert(MemberNicknameHistory history) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource param = new BeanPropertySqlParameterSource(history);
        Long id = simpleJdbcInsert.executeAndReturnKey(param).longValue();

        return MemberNicknameHistory.builder()
                .id(id)
                .memberId(history.getMemberId())
                .nickname(history.getNickname())
                .createdAt(history.getCreatedAt())
                .build();
    }

    public void delete(Long memberId) {
        String sql = String.format("DELETE FROM %s WHERE memberId = :memberId", TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("memberId", memberId);
        namedParameterJdbcTemplate.update(sql, params);
    }
}
