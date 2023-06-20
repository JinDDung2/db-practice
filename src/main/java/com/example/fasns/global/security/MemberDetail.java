package com.example.fasns.global.security;

import com.example.fasns.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class MemberDetail implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attributes;

    // default login
    public MemberDetail(Member member) {
        this.member = member;
    }

    // social login
    public MemberDetail(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 역할 목록
        GrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_BASIC");
        authorities.add(roleAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    @Override
    public String getUsername() {
        return this.member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 하위 메서드 OAuth2User 구현
    @Override
    public String getName() {
        return attributes.get("sub").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
