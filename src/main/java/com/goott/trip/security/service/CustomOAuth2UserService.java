package com.goott.trip.security.service;

import com.goott.trip.security.model.Member;
import com.goott.trip.security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        if ("Google".equalsIgnoreCase(clientName)) {
            return processGoogleUser(oAuth2User);
        } else if ("Kakao".equalsIgnoreCase(clientName)) {
            return processKakaoUser(oAuth2User);
        } else {
            throw new OAuth2AuthenticationException("Unsupported client name: " + clientName);
        }
    }
    // 구글
    private OAuth2User processGoogleUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String nameAttributeKey = "sub"; // 구글에서는 일반적으로 sub을 사용

        String email = (String) attributes.get("email");
        Member existingMember = memberService.getMemberById(email);

        if (existingMember != null) {
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    attributes,
                    nameAttributeKey
            );
        }

        Member member = new Member();
        member.setId(email);
        member.setRole(Role.MEMBER);
        /*member.setName((String) attributes.get("name"));*/
        member.setName("영문 이름을 등록하세요");
        member.setImgKey((String) attributes.get("picture"));
        member.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        String defaultPassword = "defaultPassword";
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        member.setPw(encodedPassword);

        memberService.saveMember(member);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                nameAttributeKey
        );
    }

    // 카카오
    private OAuth2User processKakaoUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String email = kakaoAccount != null ? kakaoAccount.get("email").toString() : null;
        String profileImage = null;

        if (kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                profileImage = profile.get("profile_image_url") != null ? profile.get("profile_image_url").toString() : null;
            }
        }

        Member existingMember = memberService.getMemberById(email);

        if (existingMember != null) {
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    attributes,
                    "id"
            );
        }

        Member member = new Member();
        member.setId(email);
        member.setRole(Role.MEMBER);
        member.setName("영문 이름을 등록하세요");
        member.setImgKey(profileImage);
        member.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        String defaultPassword = "defaultPassword";
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        member.setPw(encodedPassword);

        memberService.saveMember(member);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id"
        );
    }
}
