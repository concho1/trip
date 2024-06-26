package com.goott.trip.security.service;

import com.goott.trip.security.mapper.MemberMapper;
import com.goott.trip.security.model.Member;
import com.goott.trip.security.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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

    // 아이디 중복 확인
    public boolean checkDupId(String id){return memberMapper.checkDupId(id);}
    // 비밀번호 중복 확인
    public Boolean checkDupPwd(String pwd){return this.memberMapper.checkDupPwd(pwd);}

    // 비밀번호 변경(비밀번호 찾기)
    public int changePwd(HashMap<String,String> map){return this.memberMapper.changePwd(map);};

    // 수정
    public int updateMem(Member member) { return this.memberMapper.updateMem(member); }

    // 비밀번호 변경
    public int updatePwd(String id, String pw){ return this.memberMapper.updatePwd(id, pw); }
    // 비밀번호 확인
    public boolean checkPwd(String id, String pw){ return this.memberMapper.checkPwd(id, pw); }
    // 회원 탈퇴
    public int deleteUser(String id) {return this.memberMapper.deleteMem(id);}
}
