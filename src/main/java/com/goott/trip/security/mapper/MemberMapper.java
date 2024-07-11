package com.goott.trip.security.mapper;

import com.goott.trip.security.model.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface MemberMapper {
    Member findById(String id);
    void save(Member member);
    Boolean checkDupId(String id); // 아이디 중복 확인
    int changePwd(HashMap<String,String> map);
    int updateMem(Member member); // 회원 수정
    int updatePwd(String id, String pw); // 비밀번호 변경
    int deleteMem(String id); // 회원 삭제(탈퇴)

    // 예약 완료 건수를 기반으로 회원의 VIP 등급을 업데이트하는 메서드
    void updateMemberVipRank(String memberId, String vipRank);

}
