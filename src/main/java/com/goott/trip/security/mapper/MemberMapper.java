package com.goott.trip.security.mapper;

import com.goott.trip.jhm.model.CartFlight;
import com.goott.trip.security.model.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {

    Member findById(String id);
    void save(Member member);
    Boolean checkDupId(String id); // 아이디 중복 확인

    int changePwd(HashMap<String, String> map);
    int updateMem(Member member); // 회원 수정
    int updatePwd(String id, String pw); // 비밀번호 변경
    int deleteMem(String id); // 회원 삭제(탈퇴)

    int updatePaymentStatus(String airKey,  String status); // 결제 상태 업데이트
    int countCompletedPayments(String memberId); // 회원의 2년간 완료된 결제 건수 조회
    void updateMemberVipRank(String memberId, String vipRank);  /// 회원의 VIP 등급 업데이트
    String getDeparture(String airKey); // airKey로 출발지(departure) 가져오기
    String getComeback(String airKey); // airKey로 도착지(comeback) 가져오기
    List<String> getAirKeyList(String memberId); // memberId로 예약한 항공편의 airKey 리스트 가져오기

    int updateMemberRank(Map<String, Object> map); // 회원 등급 업데이트


}
