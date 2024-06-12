package com.goott.trip.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final VisitorCountFilter visitorCountFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSc) throws Exception {
        // sessionManagement => 세션 정책 : 기본이 IF_REQUIRED
        httpSc
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers("/member/**").authenticated()          // 인증 필요
                                .anyRequest().permitAll()                                 // 이외 인증 생략
                )
                .formLogin(
                        (form) -> form
                                .loginPage("/user/con/log-in").permitAll()
                                .defaultSuccessUrl("/member/con/test",true)     // 로그인 성공시 보낼 url
                                .usernameParameter("id")                                  // 로그인 시 파라메터 이름 변경
                                .passwordParameter("pw")                                  // 로그인 시 파라메터 이름 변경
                )
                .logout(
                        (logout) -> logout
                                .logoutUrl("/user/con/log-out")
                                .logoutSuccessUrl("/").permitAll()                         // 로그아웃시 보낼 url
                )
                .addFilterBefore(visitorCountFilter, UsernamePasswordAuthenticationFilter.class)    // 회원 인증 필터 이전에 실행
                .csrf(
                        (csrf) -> csrf.disable()
                );

        return httpSc.build();
    }
}
