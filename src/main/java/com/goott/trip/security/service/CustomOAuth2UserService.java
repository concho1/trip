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
import java.util.HashMap;
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
        String nameAttributeKey = "email";

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
        /*member.setName("");*/
        member.setVip("Bronze");
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
        // 카카오의 사용자 정보를 가져옴
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // attributes 맵을 복사하여 수정 가능하도록 함
        Map<String, Object> modifiableAttributes = new HashMap<>(attributes);

        // kakao_account JSON 객체에서 email 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        System.out.println(kakaoAccount.keySet().toString());
        String email = (String) kakaoAccount.get("email");
        System.out.println("User email: " + email);
        modifiableAttributes.put("email", email);
        // 이제 email 변수를 사용하여 필요한 작업을 수행할 수 있습니다.

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
                    modifiableAttributes,
                    "email"
            );
        }

        Member member = new Member();
        member.setId(email);
        member.setRole(Role.MEMBER);
        /*member.setName("이름등록필요 이름등록필요");*/
        member.setVip("Bronze");
        member.setImgKey(profileImage);
        member.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        String defaultPassword = "defaultPassword";
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        member.setPw(encodedPassword);

        memberService.saveMember(member);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                modifiableAttributes,
                "email"
        );
    }
}
