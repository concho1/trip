package com.goott.trip.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email"); // 소셜 로그인에서 이메일 정보 추출

            // 이메일을 memberId로 사용하거나 필요한 처리를 수행
            String memberId = email;

            // 예시로 로그를 출력
            System.out.println("사용자 " + memberId + " 가 로그인했습니다.");

            // 리다이렉트 URL 설정
            String redirectUrl = determineTargetUrl(authentication);

            // 리다이렉트 처리
            response.sendRedirect(redirectUrl);
        } else {
            throw new IllegalStateException("Unexpected authentication principal type");
        }
    }

    protected String determineTargetUrl(Authentication authentication) {
        // 사용자의 역할에 따라 리다이렉트할 URL을 결정하는 로직 추가 가능
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "/admin/dashboard";
        } else {
            // 일반 사용자일 경우 로그인 후 리다이렉트할 URL 설정
            return "/member/ming/myPage";
        }
    }
}