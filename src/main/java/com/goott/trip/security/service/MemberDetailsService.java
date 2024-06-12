package com.goott.trip.security.service;

import com.goott.trip.security.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User.UserBuilder;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberService memberService;
    // 로그인 검증 로직
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Optional<Member> memberOp = Optional.ofNullable(memberService.getMemberById(memberId));
        if(memberOp.isEmpty()) throw new UsernameNotFoundException("User Id 가 없습니다.");
        Member member = memberOp.get();

        UserBuilder builder = withUsername(member.getId());
        builder.password(member.getPw());
        builder.roles(member.getRole().toString());

        return builder.build();
    }
}
