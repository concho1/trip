package com.goott.trip.security.config;

import com.goott.trip.security.handler.CustomAuthenticationSuccessHandler;
import com.goott.trip.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final VisitorCountFilter visitorCountFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSc,
            AuthorizationRequestRepository<OAuth2AuthorizationRequest> requestAuthorizationRequestRepository,
            AuthenticationFailureHandler authenticationFailureHandler,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
    ) throws Exception {
        String serverIp = getServerIp();
        System.out.println(serverIp);
        httpSc
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers("/member/**").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(
                        (form) -> form
                                .loginPage("/user/ming/log-in").permitAll()
                                .successHandler(customAuthenticationSuccessHandler)
                                .usernameParameter("id")
                                .passwordParameter("pw")
                )
                .logout(
                        (logout) -> logout
                                .logoutUrl("/member/ming/log-out")
                                .logoutSuccessUrl("/").permitAll()
                )
                .addFilterBefore(visitorCountFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oAuth2LoginConfigurer ->
                        oAuth2LoginConfigurer
                                .authorizationEndpoint(authorizationEndpointConfig ->
                                        authorizationEndpointConfig
                                                .baseUri("/oauth2/authorize")  // baseUri를 /oauth2/authorize 로 설정
                                                .authorizationRequestRepository(requestAuthorizationRequestRepository))
                                .redirectionEndpoint(redirectionEndpointConfig ->
                                        redirectionEndpointConfig
                                                .baseUri("/login/oauth2/code/**"))
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig
                                                .userService(customOAuth2UserService)
                                )
                                .successHandler(customAuthenticationSuccessHandler)
                                .failureHandler(authenticationFailureHandler)
                ).headers(headers -> headers
                        .addHeaderWriter(new StaticHeadersWriter(
                                "Content-Security-Policy", "frame-ancestors 'self' " + serverIp)
                        )
                );

        return httpSc.build();
    }

    private String getServerIp() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return "http://" + localHost.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            return "http://localhost"; // 기본값
        }
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> httpSessionOAuth2AuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/user/ming/log-in?error=true");
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        /*return new DefaultOAuth2UserService();*/
        return customOAuth2UserService;
    }
}
