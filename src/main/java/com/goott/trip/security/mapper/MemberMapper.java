package com.goott.trip.security.mapper;

import com.goott.trip.security.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    Member findById(String id);
    void save(Member member);
    Boolean checkDupId(String id); // 아이디 중복 확인

    // 회원 정보
    int updateMem(Member member); // 회원 수정
    int updatePwd(String id, String pw); // 비밀번호 변경
    int deleteMem(String id); // 회원 삭제(탈퇴)

    // VIP
    void updateVIP(Member member);
    void updateTotalSpentByMember(Member member);

    // 예약

}
