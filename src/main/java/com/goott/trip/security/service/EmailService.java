package com.goott.trip.security.service;

import com.goott.trip.hamster.model.Payment;
import com.goott.trip.hamster.model.Testproduct;
import com.goott.trip.jhm.model.CartDuration;
import com.goott.trip.jhm.model.CartFlight;
import com.goott.trip.jhm.model.CartPricing;
import com.goott.trip.jhm.model.CartSegment;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    public  static final String code = createcode();

    public String sendEmail(String email, HttpSession session) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO,email);
        message.setSubject("[tripHamsic] 본인확인 인증 코드입니다.");

        String msgg="";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<h1> 안녕하세요 </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= code + "</strong><div><br/> ";
        msgg+= "</div>";
        session.setAttribute("sCode", code);
        message.setText(msgg,"UTF-8","HTML");
        javaMailSender.send(message);

        return msgg;
    }

    public String sendPwdEmail(String email, HttpSession session) throws MessagingException {
        System.out.println(email);
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO,email);
        message.setSubject("[tripHamsic] 비밀번호 찾기 인증 코드입니다.");

        String msgg="";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<h1> 안녕하세요 </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 비밀번호 찾기 이메일 인증 창으로 돌아가 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>비밀번호 찾기 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= code + "</strong><div><br/> ";
        msgg+= "</div>";
        session.setAttribute("sCode", code);
        message.setText(msgg,"UTF-8","HTML");
        javaMailSender.send(message);

        return msgg;
    }

    public void sendAirplaneEmail(String email, String UUID, Payment paymentInfo, List<CartFlight> airInfo,
                                  List<CartDuration> DepDur,List<CartDuration> CombDur,List<CartSegment> segDep,
                                  List<CartSegment> segComb,List<CartPricing> price) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        Date currentDate = new Date();

        // 원하는 형식으로 출력하기
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(currentDate);


        String[] rideFirstName = paymentInfo.getRideFirstName();
        String[] rideLastName = paymentInfo.getRideLastName();
        String[] rideBirth = paymentInfo.getRideBirth();
        String[] rideCountry = paymentInfo.getRideCountry();
        String[] ridePassport = paymentInfo.getRidePassport();
        String[] ridePassportCountry = paymentInfo.getRidePassportCountry();
        String[] ridePassportExdate = paymentInfo.getRidePassportExdate();

        message.addRecipients(Message.RecipientType.TO,email);
        message.setSubject("[tripHamsic]"+email+"고객님 전자 영수증입니다.");

        String msgg = "";
        msgg += "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;line-height:1.5;color:#444;\">";
        msgg += "<tbody><tr><td align=\"center\">";

        msgg += "<div style=\"max-width:600px; margin:0 auto;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;border:1px solid #ddd;\">";
        msgg += "<tbody><tr><td height=\"38\" style=\"vertical-align:top;text-align:left;\">";
        msgg += "</td></tr><tr><td style=\"padding:0 30px\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;\">";
        msgg += "<tbody><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\"></td></tr><tr><td height=\"40\"></td></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<p style=\"margin:0;font-size:23px;line-height:33px;color:#444;font-weight:bold;\">";
        msgg += "항공권 구매가 완료되었습니다.";
        msgg += "</p>";
        msgg += "</td></tr><tr><td height=\"50\"></td></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;border-top:2px solid #666;border-bottom:1px solid #ddd;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:30%\"><col style=\"width:auto\"></colgroup>";
        msgg += "<tbody><tr><td colspan=\"2\" height=\"20\">";
        msgg += "</td></tr><tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\" height=\"30\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">예약번호</th><td align=\"left\" height=\"30\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\"><span style=\"font-weight:bold;color:#ab7d55;\">"+UUID+"</span></td></tr><tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\" height=\"30\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">예약일자(GMT)</th><td align=\"left\" height=\"30\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">"+date+"</td></tr><tr><td colspan=\"2\" height=\"20\">";
        msgg += "</td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"30\"></td></tr><tr><td align=\"center\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"30\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\"></td></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";

        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<p style=\"margin:0;font-size:18px;\">";
        msgg += "<strong>탑승객 정보</strong>";
        msgg += "</p>";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";

        for (int i = 0; i < paymentInfo.getRideLastName().length; i ++) {
            msgg += "<tbody><tr><td align=\"left\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;vertical-align:middle;padding:25px 15px;border-right:1px solid #ddd;word-break:break-all;\">";
            msgg += "<p style=\"margin:0;padding:0;color:#555;\">" + price.get(i).getType() + "</p>";
            msgg += "<p style=\"margin:0;padding:0;color:#555;font-weight:bold;font-size:15px;\">" + rideLastName[i] + " / " + rideFirstName[i] + "</p>";
            msgg += "</td><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;padding:15px 0 15px 15px;\">";
            msgg += "<p style=\"margin:0;padding:0;\">항공권 번호</p>";
            msgg += "<p style=\"margin:0;padding-bottom:5px;color:#d60815;font-weight:bold;font-size:15px;\">" + ridePassport[i] + "</p>";
            msgg += "</td></tr><tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\">";
        }
            msgg += "</td></tr></tbody></table>";
            msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";


        msgg += "</td></tr><tr><td height=\"60\"></td></tr><tr><td valign=\"top\" height=\"40\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<p style=\"margin:0;font-size:18px;\">";
        msgg += "<strong>예약 여정</strong>";
        msgg += "</p>";

        for(int i = 0; i < segDep.size(); i ++){
            msgg += "<p style=\"margin:10px 0 0 0;font-size:16px;\">";
            msgg += "가는편 "+(i+1)+" 번째 여정";
            msgg += "</p>";
            msgg += "</td></tr><tr><td height=\"10\">";
            msgg += "</td></tr><tr></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
            msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
            msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
            msgg += "<tbody><tr><td align=\"left\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;vertical-align:middle;padding:25px 15px;border-right:1px solid #ddd;word-break:break-all;\">";
            msgg += "<p style=\"margin:0;padding:0;\">출발지</p>";
            msgg += "</td><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;padding:15px 0 15px 15px;\">";
            msgg += "<p style=\"margin:0;padding:0;\">"+segDep.get(i).getDepartureIata()+"</p>";
            msgg += "<p style=\"margin:0;padding-bottom:5px;color:#d60815;font-weight:bold;font-size:15px;\">"+
                    segDep.get(i).getDepartureAt().substring(5,10) +" "+ segDep.get(i).getDepartureAt().substring(11,16)+"</p>";
            msgg += "</td></tr><tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\">";
        }

        msgg += "</td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";

        msgg += "</td></tr><tr><td height=\"60\"></td></tr><tr><td valign=\"top\" height=\"40\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<p style=\"margin:0;font-size:18px;\">";
        msgg += "항공편 정보";
        msgg += "</p>";
        msgg += "<p style=\"margin:10px 0 0 0;font-size:16px;\">";
        msgg += "추가 미정";
        msgg += "</p>";
        msgg += "</td></tr><tr><td height=\"10\">";
        msgg += "</td></tr><tr></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
        msgg += "<tbody><tr><td align=\"left\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;vertical-align:middle;padding:25px 15px;border-right:1px solid #ddd;word-break:break-all;\">";
        msgg += "<p style=\"margin:0;padding:0;color:#555;\">출발일</p>";
        msgg += "<p style=\"margin:0;padding:0;color:#555;font-weight:bold;font-size:15px;\">2020-01-22 20:20:00</p>";
        msgg += "</td><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;padding:15px 0 15px 15px;\">";
        msgg += "<p style=\"margin:0;padding:0;\">도착일</p>";
        msgg += "<p style=\"margin:0;padding-bottom:5px;color:#d60815;font-weight:bold;font-size:15px;\">2020-01-23 00:00:00</p>";
        msgg += "</td></tr><tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\">";
        msgg += "</td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"60\"></td></tr><tr><td valign=\"top\" height=\"40\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<p style=\"margin:0;font-size:18px;\">";
        msgg += "결제 정보";
        msgg += "</p>";
        msgg += "<p style=\"margin:10px 0 0 0;font-size:16px;\">";
        msgg += "추가 미정";
        msgg += "</p>";
        msgg += "</td></tr><tr><td height=\"10\">";
        msgg += "</td></tr><tr></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
        msgg += "<tbody><tr><td align=\"left\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;vertical-align:middle;padding:25px 15px;border-right:1px solid #ddd;word-break:break-all;\">";
        msgg += "<p style=\"margin:0;padding:0;color:#555;\">결제금액</p>";
        msgg += "<p style=\"margin:0;padding:0;color:#555;font-weight:bold;font-size:15px;\">USD 800.00</p>";
        msgg += "</td><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;padding:15px 0 15px 15px;\">";
        msgg += "<p style=\"margin:0;padding:0;\">결제방식</p>";
        msgg += "<p style=\"margin:0;padding-bottom:5px;color:#d60815;font-weight:bold;font-size:15px;\">신용카드</p>";
        msgg += "</td></tr><tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\">";
        msgg += "</td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"60\"></td></tr><tr><td valign=\"top\" height=\"40\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<p style=\"margin:0;font-size:18px;\">";
        msgg += "이메일로 항공권 수령";
        msgg += "</p>";
        msgg += "<p style=\"margin:10px 0 0 0;font-size:16px;\">";
        msgg += "예약 확인을 위해 항공권이 이메일로 발송됩니다. 항공권 및 예약 관련 문의가 있으시면 ";
        msgg += "</p>";
        msgg += "</td></tr><tr><td height=\"10\">";
        msgg += "</td></tr><tr></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
        msgg += "<tbody><tr><td align=\"left\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;vertical-align:middle;padding:25px 15px;border-right:1px solid #ddd;word-break:break-all;\">";
        msgg += "<p style=\"margin:0;padding:0;color:#555;\">연락처</p>";
        msgg += "<p style=\"margin:0;padding:0;color:#555;font-weight:bold;font-size:15px;\">010-0000-0000</p>";
        msgg += "</td><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;padding:15px 0 15px 15px;\">";
        msgg += "<p style=\"margin:0;padding:0;\">이메일</p>";
        msgg += "<p style=\"margin:0;padding-bottom:5px;color:#d60815;font-weight:bold;font-size:15px;\">example@example.com</p>";
        msgg += "</td></tr><tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\">";
        msgg += "</td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"60\"></td></tr><tr><td valign=\"top\" height=\"40\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;text-align:left;\">";
        msgg += "<p style=\"margin:0;font-size:18px;\">";
        msgg += "기타 안내 사항";
        msgg += "</p>";
        msgg += "<p style=\"margin:10px 0 0 0;font-size:16px;\">";
        msgg += "예약을 취소하려면 홈페이지에서 바로 처리해주시거나 연락처로 문의해 주세요.";
        msgg += "</p>";
        msgg += "</td></tr></tbody></table>";
        msgg += "</div>";
        msgg += "</div>";
        msgg += "</td></tr><tr><td align=\"center\" style=\"font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;font-size:13px;color:#777;\">";
        msgg += "<p style=\"margin:0;padding:0;\">© 2024 myApp.com | ";
        msgg += "<a href=\"#\" style=\"color:#777;text-decoration:none;\">개인정보처리방침</a> ";
        msgg += "| ";
        msgg += "<a href=\"#\" style=\"color:#777;text-decoration:none;\">서비스이용약관</a> | ";
        msgg += "<a href=\"#\" style=\"color:#777;text-decoration:none;\">이메일수집거부</a>";
        msgg += "</p>";
        msgg += "</td></tr></tbody></table>";
        msgg += "</td></tr></tbody></table>";
        msgg += "</td></tr></tbody></table>";
        msgg += "</body></html>";

        message.setText(msgg,"UTF-8","HTML");
        javaMailSender.send(message);
    }

    public static String createcode() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 4; i++) {
            key.append(rnd.nextInt(10));
        }

        return key.toString();
    }


}
