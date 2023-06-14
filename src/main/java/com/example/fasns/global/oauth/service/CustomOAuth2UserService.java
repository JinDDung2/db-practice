package com.example.fasns.global.oauth.service;

import com.example.fasns.domain.member.entity.Member;
import com.example.fasns.domain.member.repository.MemberRepository;
import com.example.fasns.global.oauth.OAuthAttributes;
import com.example.fasns.global.oauth.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";


    /**
     * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
     * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
     * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
     * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth 서비스 이름(ex. kakao, naver, google)
        SocialType socialType = getSocialType(registrationId);
        // OAuth2 로그인 시 키(PK)가 되는 값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        saveOrUpdate(extractAttributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                extractAttributes.getNameAttributeKey()
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        UUID uuid = UUID.randomUUID();
        String password = uuid.toString().substring(8);
        Optional<Member> optionalMember = memberRepository.findByEmail(attributes.getOauth2UserInfo().getEmail());
        if (optionalMember.isEmpty()) {
            Member member = Member.builder()
                    .nickname(attributes.getOauth2UserInfo().getNickname())
                    .email(attributes.getOauth2UserInfo().getEmail())
                    .password(passwordEncoder.encode(password))
                    .birth(LocalDate.now())
                    .build();
            return memberRepository.save(member);
        }
        else {
            return optionalMember.get();
        }

    }

    private SocialType getSocialType(String registrationId) {
        if (NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }

        if (KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }

        return SocialType.GOOGLE;
    }
}
