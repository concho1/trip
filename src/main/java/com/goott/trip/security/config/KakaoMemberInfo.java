/*
package com.goott.trip.security.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KakaoMemberInfo implements OAuth2User {

    private Map<String, Object> attributes;
    private Map<String, Object> kakaoAccountAttributes;
    private ClientRegistrationRepository clientRegistrationRepository; // 클라이언트 등록 리포지토리 추가

    public KakaoMemberInfo(Map<String, Object> attributes, ClientRegistrationRepository clientRegistrationRepository) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public String getName() {
        return (String) kakaoAccountAttributes.get("nickname");
    }

    @Override
    public <A> A getAttribute(String name) {
        return (A) attributes.get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String toString() {
        return "KakaoMemberInfo{" +
                "attributes=" + attributes +
                ", kakaoAccountAttributes=" + kakaoAccountAttributes +
                '}';
    }
}
*/
