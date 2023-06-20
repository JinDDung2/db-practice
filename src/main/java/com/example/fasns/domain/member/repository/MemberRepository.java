package com.example.fasns.domain.member.repository;

import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String TABLE = "Member";

    private static final RowMapper<Member> ROW_MAPPER = (ResultSet rs, int rowNums) -> Member.builder()
            .id(rs.getLong("id"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .nickname(rs.getString("nickname"))
            .birth(rs.getObject("birth", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .memberRole(MemberRole.valueOf(rs.getString("memberRole")))
            .provider(rs.getString("provider"))
            .providerId(rs.getString("provider"))
            .build();

    public Optional<Member> findByEmail(String email) {
        String sql = String.format("select * from %s where email = :email", TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("email", email);

        try {
            Member member = namedParameterJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long id) {
        String sql = String.format("select * from %s where id = :id", TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            Member member = namedParameterJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        String sql = String.format("SELECT * FROM %s", TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource();

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Member> findAllByIdIn(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        String sql = String.format("SELECT * FROM %s WHERE id in (:ids)", TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("ids", ids);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            return insert(member);
        }
        return update(member);
    }

    public void delete(Member member) {
        String sql = String.format("DELETE FROM %s WHERE id = :id", TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql, params);
    }

    private Member insert(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(param).longValue();

        return Member.builder()
                .id(id)
                .email(member.getEmail())
                .password(member.getPassword())
                .birth(member.getBirth())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member update(Member member) {
        String sql = String.format("UPDATE %s set email = :email, nickname = :nickname, password = :password, memberRole = :memberRole, birth = :birth WHERE id = :id", TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", member.getId())
                .addValue("email", member.getEmail())
                .addValue("nickname", member.getNickname())
                .addValue("password", member.getPassword())
                .addValue("memberRole", member.getMemberRole().getName())
                .addValue("birth", member.getBirth());
        namedParameterJdbcTemplate.update(sql, params);
        return member;
    }
}
