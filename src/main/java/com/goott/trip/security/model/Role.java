package com.goott.trip.security.model;

public enum Role {
    GUEST,          // 게스트 사용자
    SNS,            // 카카오, 구글 로그인
    MEMBER,         // 멤버
    SUB_ADMIN,      // 초보 관리자
    ADMIN,          // 관리자
    SUPER_ADMIN;    // 슈퍼 관리자
}
