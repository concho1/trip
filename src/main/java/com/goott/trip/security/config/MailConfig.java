package com.goott.trip.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.transport.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttls;

    @Value("${spring.mail.debug}")
    private String debug;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.default-encoding}")
    private String encoding;

    @Bean
    public JavaMailSender javaMailSender(){

        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        Properties pt = new Properties();
        pt.put("mail.transport.protocol", protocol);
        pt.put("mail.smtp.auth", auth);
        pt.put("mail.smtp.starttls.enable", starttls);
        pt.put("mail.smtp.debug", debug);

        javaMailSenderImpl.setHost(host);
        javaMailSenderImpl.setUsername(username);
        javaMailSenderImpl.setPassword(password);
        javaMailSenderImpl.setPort(port);
        javaMailSenderImpl.setJavaMailProperties(pt);
        javaMailSenderImpl.setDefaultEncoding(encoding);

        return javaMailSenderImpl;
    }
}
