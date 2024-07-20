package com.goott.trip.security.service;

import com.goott.trip.hamster.mapper.AirplaneMapper;
import com.goott.trip.hamster.mapper.paymentMapper;
import com.goott.trip.hamster.model.ConHotelCartAll;
import com.goott.trip.hamster.model.ConPayment;
import com.goott.trip.hamster.model.Payment;
import com.goott.trip.hamster.service.ConHotelCartService;
import com.goott.trip.jhm.model.CartFlight;
import com.goott.trip.security.mapper.MemberMapper;
import com.goott.trip.security.model.Member;
import com.goott.trip.security.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final paymentMapper paymentMapper;
    private final AirplaneMapper airplaneMapper;
    private final ConHotelCartService hotelCartService;

    public void saveMember(Member member){
        member.setRole(Role.MEMBER); //MEMBER 역할 부여
        member.setPw(passwordEncoder.encode(member.getPw()));
        member.setVip("Bronze");
        memberMapper.save(member);
    }

    public Member getMemberById(String id){
        return memberMapper.findById(id);
    }

    // 아이디 중복 확인
    public boolean checkDupId(String id){return memberMapper.checkDupId(id);}

    // 비밀번호 변경(새 비밀번호로 변경)
    public int changePwd(String id, String newPw) {
        // 기존 비밀번호 확인
        if (checkPwd(id, newPw)) {
            return 0; // 새 비밀번호가 기존 비밀번호와 동일하면 변경하지 않음
        }
        // 새 비밀번호 암호화
        String encodedPwd = passwordEncoder.encode(newPw);

        // 비밀번호 업데이트
        return memberMapper.updatePwd(id, encodedPwd); // 업데이트 결과 반환
    }

    // 수정
    public int updateMem(Member member) { return this.memberMapper.updateMem(member); }

    // 비밀번호 확인
    public boolean checkPwd(String id, String pw) {
        Member member = memberMapper.findById(id);
        return passwordEncoder.matches(pw, member.getPw());
    }
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

    // 회원 탈퇴
    public int deleteMem(String id, String pw) {
        // 기존 비밀번호 확인
        if (!checkPwd(id, pw)) {
            return 0; // 기존 비밀번호가 일치하지 않으면 실패
        }
        // 회원 탈퇴 처리
        return memberMapper.deleteMem(id); // 탈퇴 결과 반환
    }

    // VIP
    // VIP 등급 업데이트
    public void updateVIPStatus(String memberId) {
        Member member = memberMapper.findById(memberId);
        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        double totalSpent = member.getTotal(); // 이미 DB에 저장된 총 소비 금액을 가져옴

        if (totalSpent >= 5000000) {
            member.setVip("Platinum");
        } else if (totalSpent >= 3000000) {
            member.setVip("Gold");
        } else if (totalSpent >= 1000000) {
            member.setVip("Silver");
        } else {
            member.setVip("Bronze");
        }

        memberMapper.updateVIP(member);
    }

    // 총 소비 금액 업데이트 및 티켓 상태 업데이트
    public void updateTotalSpentByMember(String memberId) {

        double totalSpent = 0.0;
        // 여기서 호텔 이용 완료 목록 금액 업데이트
        List<ConHotelCartAll> cartAllList = hotelCartService.getConHotelCartAllListByMemberId(memberId);
        for(ConHotelCartAll hotelCartAll : cartAllList){
            Optional<ConPayment> paymentOp = Optional.ofNullable(hotelCartAll.getPaymentObj());
            // 결제 한거 중에 => 체크아웃 날짜가 오늘을 넘었으면
            if(paymentOp.isPresent()
                    && !hotelCartAll.getOfferObj().getCheckOut().toLocalDate().isAfter(LocalDate.now())){
                totalSpent += hotelCartAll.getOfferObj().getTotalCost();
                // 티켓 상태를 completed로 업데이트
                paymentMapper.updateTicketStatusByOrderUuid(paymentOp.get().getOrderUuid(), "completed");
            }
        }

        // 항공권 결제만 조회하여 업데이트
        List<Payment> allPayments = paymentMapper.findByMemberId(memberId);
        for (Payment payment : allPayments) {
            // airKey가 있는 결제만 처리
            if (payment.getAirKey() != null) {
                List<CartFlight> cartFlights = airplaneMapper.getAirInfo(payment.getAirKey());
                if (cartFlights != null && !cartFlights.isEmpty()) {
                    CartFlight cartFlight = cartFlights.get(0);
                    String comeback = cartFlight.getComeback();
                    String departure = cartFlight.getDeparture();

                    // comeback 필드가 빈 문자열이 아닌지 확인
                    if (comeback != null && !comeback.isEmpty()) {
                        LocalDate comebackDate = LocalDate.parse(comeback, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        // 티켓의 예약 날짜가 오늘 날짜보다 이전일 경우 상태를 completed로 변경
                        if (comebackDate.isBefore(LocalDate.now())) {
                            totalSpent += cartFlight.getTotalPrice();
                            paymentMapper.updateTicketStatus(payment.getAirKey(), "completed");
                        }
                    } else if(departure != null && !departure.isEmpty()){
                        LocalDate departureDate = LocalDate.parse(departure, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        // 티켓의 예약 날짜가 오늘 날짜보다 이전일 경우 상태를 completed로 변경
                        if (departureDate.isBefore(LocalDate.now())) {
                            totalSpent += cartFlight.getTotalPrice();
                            paymentMapper.updateTicketStatus(payment.getAirKey(), "completed");
                        }

                    }
                }
            }
        }

        Member member = memberMapper.findById(memberId);

        member.setTotal(totalSpent);
        memberMapper.updateTotalSpentByMember(member);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행 (cron 표현식)
    public void updateReservationStatus() {
        List<Payment> allPayments = paymentMapper.findAllPayments();

        // 사용자별로 총 소비 금액을 계산하여 업데이트
        Map<String, Double> memberTotalSpentMap = new HashMap<>();

        for (Payment payment : allPayments) {
            updateAirTicketStatus(payment);
            updateHotelReservationStatus(payment);

            // 총 소비 금액 업데이트
            String memberId = payment.getMemberId();
            if (!memberTotalSpentMap.containsKey(memberId)) {
                memberTotalSpentMap.put(memberId, 0.0);
            }

            if (payment.getAirKey() != null) {
                List<CartFlight> cartFlights = airplaneMapper.getAirInfo(payment.getAirKey());
                if (cartFlights != null && !cartFlights.isEmpty()) {
                    CartFlight cartFlight = cartFlights.get(0);
                    String comeback = cartFlight.getComeback();
                    String departure = cartFlight.getDeparture();

                    if (comeback != null && !comeback.isEmpty()) {
                        LocalDate comebackDate = LocalDate.parse(comeback, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (comebackDate.isBefore(LocalDate.now())) {
                            memberTotalSpentMap.put(memberId, memberTotalSpentMap.get(memberId) + cartFlight.getTotalPrice());
                        }
                    } else if (departure != null && !departure.isEmpty()) {
                        LocalDate departureDate = LocalDate.parse(departure, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (departureDate.isBefore(LocalDate.now())) {
                            memberTotalSpentMap.put(memberId, memberTotalSpentMap.get(memberId) + cartFlight.getTotalPrice());
                        }
                    }
                }
            }

            List<ConHotelCartAll> cartAllList = hotelCartService.getConHotelCartAllListByMemberId(memberId);
            for (ConHotelCartAll hotelCartAll : cartAllList) {
                Optional<ConPayment> paymentOp = Optional.ofNullable(hotelCartAll.getPaymentObj());
                if (paymentOp.isPresent()
                        && !hotelCartAll.getOfferObj().getCheckOut().toLocalDate().isAfter(LocalDate.now())) {
                    memberTotalSpentMap.put(memberId, memberTotalSpentMap.get(memberId) + hotelCartAll.getOfferObj().getTotalCost());
                }
            }
        }

        // 총 소비 금액을 업데이트
        for (Map.Entry<String, Double> entry : memberTotalSpentMap.entrySet()) {
            String memberId = entry.getKey();
            double totalSpent = entry.getValue();
            Member member = memberMapper.findById(memberId);
            member.setTotal(totalSpent);
            memberMapper.updateTotalSpentByMember(member);
        }
    }

    private void updateAirTicketStatus(Payment payment) {
        if (payment.getAirKey() != null) {
            List<CartFlight> cartFlights = airplaneMapper.getAirInfo(payment.getAirKey());
            if (cartFlights != null && !cartFlights.isEmpty()) {
                CartFlight cartFlight = cartFlights.get(0);
                String comeback = cartFlight.getComeback();
                String departure = cartFlight.getDeparture();

                if (comeback != null && !comeback.isEmpty()) {
                    LocalDate comebackDate = LocalDate.parse(comeback, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (comebackDate.isBefore(LocalDate.now())) {
                        paymentMapper.updateTicketStatus(payment.getAirKey(), "completed");
                    }
                } else if (departure != null && !departure.isEmpty()) {
                    LocalDate departureDate = LocalDate.parse(departure, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (departureDate.isBefore(LocalDate.now())) {
                        paymentMapper.updateTicketStatus(payment.getAirKey(), "completed");
                    }
                }
            }
        }
    }

    private void updateHotelReservationStatus(Payment payment) {
        List<ConHotelCartAll> cartAllList = hotelCartService.getConHotelCartAllListByMemberId(payment.getMemberId());
        for (ConHotelCartAll hotelCartAll : cartAllList) {
            Optional<ConPayment> paymentOp = Optional.ofNullable(hotelCartAll.getPaymentObj());
            if (paymentOp.isPresent()
                    && !hotelCartAll.getOfferObj().getCheckOut().toLocalDate().isAfter(LocalDate.now())) {
                paymentMapper.updateTicketStatusByOrderUuid(paymentOp.get().getOrderUuid(), "completed");
            }
        }
    }


    // 예약
    // payment 테이블에서 airKey 가져오기
    public List<String> getPaymentAirKey(String memberId){return this.paymentMapper.getPaymentAirKey(memberId);}
    public List<Payment> getPayment(String AirKey){
        List<Payment> paymentList = this.paymentMapper.findByAirKeyHam(AirKey);
        System.out.println(paymentList);
        paymentList.forEach(payment -> payment.AllStrToArray());
        System.out.println(paymentList);
        return paymentList;
    }
    public List<Payment> payAir(String memberId){return this.paymentMapper.payAir(memberId);}
    public List<Payment> payHotel(String memberId){return this.paymentMapper.payHotel(memberId);}
}

