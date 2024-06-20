/*
package com.goott.trip.security.service;

import com.goott.trip.domain.Role;
import com.goott.trip.domain.User;
import com.goott.trip.repository.UserRepository;
import com.goott.trip.security.PrincipalDetails;
import com.goott.trip.security.model.Member;
import com.goott.trip.security.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();    // google
        String providerId = oAuth2User.getAttribute("sub");
        String name = provider + "_" + providerId;  // 사용자가 입력한 적은 없지만 만들어준다

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = passwordEncoder.encode("패스워드" + uuid);  // 사용자가 입력한 적은 없지만 만들어준다

        String email = oAuth2User.getAttribute("email");
        Role role = Role.MEMBER;

        Member memName = memberService.getMemberById(name);

        // DB에 없는 사용자라면 회원가입처리
        if (memName == null) {
            memName = Member.oauth2Register()
                    .username(name).password(password).email(email).role(role)
                    .provider(provider).providerId(providerId)
                    .build();
            memberService.saveMember(memName);
        }

        return new PrincipalDetails(byUsername, oAuth2User.getAttributes());
    }
}
*/
