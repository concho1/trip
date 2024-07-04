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

    // 비밀번호 변경(비밀번호 찾기)
    public int changePwd(HashMap<String,String> map){return this.memberMapper.changePwd(map);}

    // 수정
    public int updateMem(Member member) { return this.memberMapper.updateMem(member); }

    // 비밀번호 확인
    public boolean checkPwd(String id, String pw) {
        Member member = memberMapper.findById(id);
        return passwordEncoder.matches(pw, member.getPw());
    }
    /*// 비밀번호 변경
    public int updatePwd(String id, String newPw) {
        String encodedPwd = passwordEncoder.encode(newPw);
        return this.memberMapper.updatePwd(id, encodedPwd);
    }*/
    // 비밀번호 변경 메서드
    public int updatePwd(String id, String pw, String newPw) {
        // 기존 비밀번호 확인
        if (!checkPwd(id, pw)) {
            return 0; // 기존 비밀번호가 일치하지 않으면 실패
        }

        // 새 비밀번호와 기존 비밀번호가 동일한지 확인
        if (pw.equals(newPw)) {
            return -1; // 새 비밀번호가 기존 비밀번호와 동일하면 실패 (-1을 사용하여 실패 상황을 나타냄)
        }

        // 비밀번호 업데이트
        String encodedPwd = passwordEncoder.encode(newPw);
        return memberMapper.updatePwd(id, encodedPwd); // 업데이트 결과 반환
    }

    /*// 회원 탈퇴
    public int deleteMem(String id) {return this.memberMapper.deleteMem(id);}*/

    // 회원 탈퇴
    public int deleteMem(String id, String pw) {
        // 기존 비밀번호 확인
        if (!checkPwd(id, pw)) {
            return 0; // 기존 비밀번호가 일치하지 않으면 실패
        }
        // 회원 탈퇴 처리
        return memberMapper.deleteMem(id); // 탈퇴 결과 반환
    }
}
