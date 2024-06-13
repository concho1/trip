package com.goott.trip.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Order(1)
public class VisitorCountFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userAgent = request.getHeader("User-Agent");
        HttpSession session = request.getSession(true);

        // 봇인 경우에는 다음 필터로 바로 넘어가기
        // 방문자 카운트를 세션에 저장
        if (session.getAttribute("visitorCounted") == null) {
            if (userAgent != null && (userAgent.contains("bot") || userAgent.contains("crawl") || userAgent.contains("spider"))) {
                System.out.println("봇 + 1");
            } else {
                System.out.println("사람 + 1");
            }
            session.setAttribute("visitorCounted", true);  // 방문자 카운트를 세션에 저장
        }

        filterChain.doFilter(request, response);
    }
}
