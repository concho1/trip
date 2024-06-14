package com.goott.trip.security.service;

import com.goott.trip.security.mapper.MemberMapper;
import com.goott.trip.security.model.Member;
import com.goott.trip.security.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public void saveMember(Member member){
        member.setRole(Role.MEMBER); //MEMBER 역할 부여
        member.setPw(passwordEncoder.encode(member.getPw()));
        memberMapper.save(member);
    }

    public Member getMemberById(String id){
        return memberMapper.findById(id);
    }

}
