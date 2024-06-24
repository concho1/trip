package com.goott.trip.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName();

        // 로그인 성공 후 DB에 저장 필요한 작업 (예: 사용자 로그 기록)
        System.out.println("사용자 " + userId + " 가 로그인했습니다.");

        // 리다이렉트 URL 설정
        String redirectUrl = determineTargetUrl(authentication);

        // 리다이렉트 처리
        response.sendRedirect(redirectUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        // 사용자의 역할에 따라 리다이렉트할 URL을 결정하는 로직 추가 가능
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "/admin/dashboard";
        } else {
            // 일반 사용자일 경우 로그인 후 리다이렉트할 URL 설정
            return "/member/con/test";
        }
    }
}
