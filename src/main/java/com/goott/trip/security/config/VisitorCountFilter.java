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
import java.util.regex.Pattern;

@Component
@Order(1)
public class VisitorCountFilter extends OncePerRequestFilter {

    @Autowired
    private AdminService adminService;
    private static final Pattern BOT_PATTERN = Pattern.compile("bot|crawl|spider|slurp|curl|wget|python", Pattern.CASE_INSENSITIVE);
    private static final String[] FILTERED_IPS = {"172.31.20.221", "172.31.11.63"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userAgent = request.getHeader("User-Agent");
        HttpSession session = request.getSession(true);
        LocalDateTime ldt = LocalDateTime.now();
        String today = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String ipAddress = getClientIp(request);
        // 봇인 경우에는 다음 필터로 바로 넘어가기
        // 방문자 카운트를 세션에 저장

        if (session.getAttribute("visitorCounted") == null) {
            System.out.println("IP Address: " + ipAddress);
            if (userAgent != null || BOT_PATTERN.matcher(userAgent).find()) {
                System.out.println("봇 + 1");
                this.adminService.countBot(today);
            } else {
                if(isFilteredIp(ipAddress)){
                    System.out.println("AWS 봇 + 1");
                    this.adminService.countBot(today);
                }else{
                    System.out.println("사람 + 1");
                    this.adminService.countGuest(today);
                }
            }
            session.setAttribute("visitorCounted", true);  // 방문자 카운트를 세션에 저장
        }

        filterChain.doFilter(request, response);
    }
    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        } else {
            // X-Forwarded-For 헤더는 쉼표로 구분된 IP 주소 목록일 수 있습니다. 첫 번째 IP 주소가 실제 클라이언트 IP 주소입니다.
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }
    private boolean isFilteredIp(String ipAddress) {
        for (String filteredIp : FILTERED_IPS) {
            if (filteredIp.equals(ipAddress)) {
                return true;
            }
        }
        return false;
    }
}
