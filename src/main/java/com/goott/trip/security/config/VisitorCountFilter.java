package com.goott.trip.security.config;

import com.goott.trip.jhm.service.AdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(1)
public class VisitorCountFilter extends OncePerRequestFilter {

    @Autowired
    private AdminService adminService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userAgent = request.getHeader("User-Agent");
        HttpSession session = request.getSession(true);
        LocalDateTime ldt = LocalDateTime.now();
        String today = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));

        // 봇인 경우에는 다음 필터로 바로 넘어가기
        // 방문자 카운트를 세션에 저장
        if (session.getAttribute("visitorCounted") == null) {
            if (userAgent != null && (userAgent.contains("bot") || userAgent.contains("crawl") || userAgent.contains("spider"))) {
                System.out.println("봇 + 1");
                this.adminService.countBot(today);
            } else {
                System.out.println("사람 + 1");
                this.adminService.countGuest(today);
            }
            session.setAttribute("visitorCounted", true);  // 방문자 카운트를 세션에 저장
        }

        filterChain.doFilter(request, response);
    }
}
