package com.goott.trip.security.service;

import com.goott.trip.hamster.model.ConHotelCartAll;
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

import java.text.DecimalFormat;
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
        message.setSubject("[놀러감] 본인확인 인증 코드입니다.");

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
        message.setSubject("[놀러감] 비밀번호 찾기 인증 코드입니다.");

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


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String date = dateFormat.format(currentDate);

        String[] rideFirstName = paymentInfo.getRideFirstName();
        String[] rideLastName = paymentInfo.getRideLastName();
        String[] rideBirth = paymentInfo.getRideBirth();
        String[] rideCountry = paymentInfo.getRideCountry();
        String[] ridePassport = paymentInfo.getRidePassport();
        String[] ridePassportCountry = paymentInfo.getRidePassportCountry();
        String[] ridePassportExdate = paymentInfo.getRidePassportExdate();


        message.addRecipients(Message.RecipientType.TO,email);
        message.setSubject("[놀러감]"+email+"고객님 항공권 예약 전자 영수증입니다.");


        String msgg = "";

        msgg += "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;line-height:1.5;color:#444;\">";
        msgg += "<tbody><tr><td align=\"center\">";
        msgg += "<div style=\"max-width:600px; margin:0 auto;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;border:1px solid #ddd;\">";
        msgg += "<tbody><tr><td height=\"38\" style=\"vertical-align:top;text-align:left;\"></td></tr>";
        msgg += "<tr><td style=\"padding:0 30px\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;\">";
        msgg += "<tbody><tr><td></td></tr>";
        msgg += "<tr><td height=\"40\"></td></tr><tr><td>";
        msgg += "<p style=\"margin:0;font-size:23px;line-height:33px;color:#444;font-weight:bold;\">항공권 구매가 완료되었습니다.</p>";
        msgg += "</td></tr><tr><td height=\"50\"></td></tr><tr><td>";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;border-top:2px solid #666;border-bottom:1px solid #ddd;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:30%\"><col style=\"width:auto\"></colgroup>";
        msgg += "<tbody><tr><td colspan=\"2\" height=\"20\"></td></tr>";
        msgg += "<tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\" height=\"30\">예약번호</th><td align=\"left\" height=\"30\"><span style=\"font-weight:bold;color:#ab7d55;\">"+UUID+"</span></td></tr>";
        msgg += "<tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\" height=\"30\">예약일자(GMT)</th><td align=\"left\" height=\"30\">"+date+"</td></tr>";
        msgg += "<tr><td colspan=\"2\" height=\"20\"></td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"30\"></td></tr><tr><td align=\"center\"></td></tr><tr><td height=\"30\"></td></tr><tr><td>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr></tr><tr><td>";
        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>탑승객 정보</strong></p>";


        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
        for(int i = 0; i < paymentInfo.getRideLastName().length; i++){
            msgg += "<tbody><tr><td align=\"left\" style=\"padding:25px 15px;border-right:1px solid #ddd;\"><p style=\"margin:0;color:#555;\">"+
                    price.get(i).getType()+"</p><p style=\"margin:0;font-weight:bold;font-size:15px;color:#555;\">" +
                    rideLastName[i]+" "+rideFirstName[i]+"</p></td><td style=\"padding:15px 0 15px 15px;\"><p style=\"margin:0;\">" +
                    ridePassport[i] +"</p><p style=\"margin:0;padding-bottom:5px;font-weight:bold;font-size:15px;color:#d60815;\">9882480107273</p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr></tbody>";
        }
        msgg += "</table></td></tr><tr><td height=\"20\"></td></tr><tr><td>";


        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>예약 여정</strong></p>";
        msgg += "</td></tr><tr><td height=\"40\"></td></tr><tr><td valign=\"top\" style=\"text-align:left;\">";


       for(int i = 0; i < segDep.size(); i ++){
           msgg += "<p style=\"margin:0;font-size:16px;\">가는편 "+(i+1)+ " 번째 여정</p>";
           msgg += "</td></tr><tr><td height=\"10\"></td></tr><tr><td>";
           msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
           msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
           msgg += "<tbody><tr><td align=\"left\" style=\"padding:15px;border-right:1px solid #ddd;\"><p style=\"margin:0;\">출발</p></td><td style=\"padding:15px;\"><p style=\"margin:0;font-weight:bold;\">" +
                   segDep.get(i).getDepartureIata()+"</p><p style=\"margin:0;\">"+segDep.get(i).getDepartureAt().substring(0,10)+" "+segDep.get(i).getDepartureAt().substring(11,16)+"</p></td></tr>";
           msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr><tr><td align=\"left\" style=\"padding:15px;border-right:1px solid #ddd;\"><p style=\"margin:0;\">도착</p></td><td style=\"padding:15px;\"><p style=\"margin:0;font-weight:bold;\">" +
                   segDep.get(i).getArrivalIata()+"</p><p style=\"margin:0;\">"+segDep.get(i).getArrivalAt().substring(0,10)+" "+segDep.get(i).getArrivalAt().substring(11,16)+"</p></td></tr>";
           msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr></tbody></table>";
           msgg += "</td></tr><tr><td height=\"30\"></td></tr><tr><td valign=\"top\" style=\"text-align:left;\">";
       }


        for(int i = 0; i < segDep.size(); i ++){
            msgg += "<p style=\"margin:0;font-size:16px;\">오는편 "+(i+1)+ " 번째 여정</p>";
            msgg += "</td></tr><tr><td height=\"10\"></td></tr><tr><td>";
            msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
            msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
            msgg += "<tbody><tr><td align=\"left\" style=\"padding:15px;border-right:1px solid #ddd;\"><p style=\"margin:0;\">출발</p></td><td style=\"padding:15px;\"><p style=\"margin:0;font-weight:bold;\">" +
                    segComb.get(i).getDepartureIata()+"</p><p style=\"margin:0;\">"+segComb.get(i).getDepartureAt().substring(0,10)+" "+segComb.get(i).getDepartureAt().substring(11,16)+"</p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr><tr><td align=\"left\" style=\"padding:15px;border-right:1px solid #ddd;\"><p style=\"margin:0;\">도착</p></td><td style=\"padding:15px;\"><p style=\"margin:0;font-weight:bold;\">" +
                    segComb.get(i).getArrivalIata()+"</p><p style=\"margin:0;\">"+segComb.get(i).getArrivalAt().substring(0,10)+" "+segComb.get(i).getArrivalAt().substring(11,16)+"</p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr></tbody></table>";
            msgg += "</td></tr><tr><td height=\"30\"></td></tr><tr><td valign=\"top\" style=\"text-align:left;\">";
        }


        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>결제 내역</strong></p>";
        msgg += "</td></tr><tr><td height=\"10\"></td></tr><tr><td style=\"background:#f0f0f0;padding:20px;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;\">";
        msgg += "<tbody><tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\">총 지불금액 (결제완료)</th><td align=\"right\"><p style=\"padding:0;margin:0;color:#d60815;\">" +
                "<strong>KRW <span style=\"font-size:23px;\">"+" "+ decimalFormat.format(airInfo.get(0).getTotalPrice())+"</span></strong></p></td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "본 예약은 항공권 구입이 완료된 건으로, 구매한 항공권에 대한 예약 변경 및 환불 시 운임규정에 따른 예약 변경 수수료 (재발행 수수료) 및 환불 위약금 또는 환불 수수료가 징수될 수 있습니다. 자세한 내용은 운임 규정을 확인하시기 바랍니다.";
        msgg += "</td></tr><tr><td height=\"60\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr></tbody></table></div></td></tr></tbody></table>";



        message.setText(msgg,"UTF-8","HTML");
        javaMailSender.send(message);
    }

    public void sendAirplaneEmail(String email, String UUID, Payment paymentInfo, List<CartFlight> airInfo,
                                  List<CartDuration> DepDur,List<CartDuration> CombDur,List<CartSegment> segDep,
                                  List<CartPricing> price) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        Date currentDate = new Date();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String date = dateFormat.format(currentDate);

        String[] rideFirstName = paymentInfo.getRideFirstName();
        String[] rideLastName = paymentInfo.getRideLastName();
        String[] rideBirth = paymentInfo.getRideBirth();
        String[] rideCountry = paymentInfo.getRideCountry();
        String[] ridePassport = paymentInfo.getRidePassport();
        String[] ridePassportCountry = paymentInfo.getRidePassportCountry();
        String[] ridePassportExdate = paymentInfo.getRidePassportExdate();


        message.addRecipients(Message.RecipientType.TO,email);
        message.setSubject("[놀러감]"+email+"고객님 항공권 예약 전자 영수증입니다.");


        String msgg = "";

        msgg += "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;line-height:1.5;color:#444;\">";
        msgg += "<tbody><tr><td align=\"center\">";
        msgg += "<div style=\"max-width:600px; margin:0 auto;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;border:1px solid #ddd;\">";
        msgg += "<tbody><tr><td height=\"38\" style=\"vertical-align:top;text-align:left;\"></td></tr>";
        msgg += "<tr><td style=\"padding:0 30px\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;\">";
        msgg += "<tbody><tr><td></td></tr>";
        msgg += "<tr><td height=\"40\"></td></tr><tr><td>";
        msgg += "<p style=\"margin:0;font-size:23px;line-height:33px;color:#444;font-weight:bold;\">항공권 구매가 완료되었습니다.</p>";
        msgg += "</td></tr><tr><td height=\"50\"></td></tr><tr><td>";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;border-top:2px solid #666;border-bottom:1px solid #ddd;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:30%\"><col style=\"width:auto\"></colgroup>";
        msgg += "<tbody><tr><td colspan=\"2\" height=\"20\"></td></tr>";
        msgg += "<tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\" height=\"30\">예약번호</th><td align=\"left\" height=\"30\"><span style=\"font-weight:bold;color:#ab7d55;\">"+UUID+"</span></td></tr>";
        msgg += "<tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\" height=\"30\">예약일자(GMT)</th><td align=\"left\" height=\"30\">"+date+"</td></tr>";
        msgg += "<tr><td colspan=\"2\" height=\"20\"></td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"30\"></td></tr><tr><td align=\"center\"></td></tr><tr><td height=\"30\"></td></tr><tr><td>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr></tr><tr><td>";
        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>탑승객 정보</strong></p>";


        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
        for(int i = 0; i < paymentInfo.getRideLastName().length; i++){
            msgg += "<tbody><tr><td align=\"left\" style=\"padding:25px 15px;border-right:1px solid #ddd;\"><p style=\"margin:0;color:#555;\">"+
                    price.get(i).getType()+"</p><p style=\"margin:0;font-weight:bold;font-size:15px;color:#555;\">" +
                    rideLastName[i]+" "+rideFirstName[i]+"</p></td><td style=\"padding:15px 0 15px 15px;\"><p style=\"margin:0;\">" +
                    ridePassport[i] +"</p><p style=\"margin:0;padding-bottom:5px;font-weight:bold;font-size:15px;color:#d60815;\">9882480107273</p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr></tbody>";
        }
        msgg += "</table></td></tr><tr><td height=\"20\"></td></tr><tr><td>";


        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>예약 여정</strong></p>";
        msgg += "</td></tr><tr><td height=\"40\"></td></tr><tr><td valign=\"top\" style=\"text-align:left;\">";


        for(int i = 0; i < segDep.size(); i ++){
            msgg += "<p style=\"margin:0;font-size:16px;\">가는편 "+(i+1)+ " 번째 여정</p>";
            msgg += "</td></tr><tr><td height=\"10\"></td></tr><tr><td>";
            msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
            msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
            msgg += "<tbody><tr><td align=\"left\" style=\"padding:15px;border-right:1px solid #ddd;\"><p style=\"margin:0;\">출발</p></td><td style=\"padding:15px;\"><p style=\"margin:0;font-weight:bold;\">" +
                    segDep.get(i).getDepartureIata()+"</p><p style=\"margin:0;\">"+segDep.get(i).getDepartureAt().substring(0,10)+" "+segDep.get(i).getDepartureAt().substring(11,16)+"</p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr><tr><td align=\"left\" style=\"padding:15px;border-right:1px solid #ddd;\"><p style=\"margin:0;\">도착</p></td><td style=\"padding:15px;\"><p style=\"margin:0;font-weight:bold;\">" +
                    segDep.get(i).getArrivalIata()+"</p><p style=\"margin:0;\">"+segDep.get(i).getArrivalAt().substring(0,10)+" "+segDep.get(i).getArrivalAt().substring(11,16)+"</p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr></tbody></table>";
            msgg += "</td></tr><tr><td height=\"30\"></td></tr><tr><td valign=\"top\" style=\"text-align:left;\">";
        }


        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>결제 내역</strong></p>";
        msgg += "</td></tr><tr><td height=\"10\"></td></tr><tr><td style=\"background:#f0f0f0;padding:20px;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;\">";
        msgg += "<tbody><tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\">총 지불금액 (결제완료)</th><td align=\"right\"><p style=\"padding:0;margin:0;color:#d60815;\">" +
                "<strong>KRW <span style=\"font-size:23px;\">"+" "+ decimalFormat.format(airInfo.get(0).getTotalPrice())+"</span></strong></p></td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "본 예약은 항공권 구입이 완료된 건으로, 구매한 항공권에 대한 예약 변경 및 환불 시 운임규정에 따른 예약 변경 수수료 (재발행 수수료) 및 환불 위약금 또는 환불 수수료가 징수될 수 있습니다. 자세한 내용은 운임 규정을 확인하시기 바랍니다.";
        msgg += "</td></tr><tr><td height=\"60\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr></tbody></table></div></td></tr></tbody></table>";



        message.setText(msgg,"UTF-8","HTML");
        javaMailSender.send(message);
    }

    public void sendHotelEmail(String UUID, String firstName,String lastName,String email, List<ConHotelCartAll> hotelList,double totalPrice) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String date = dateFormat.format(currentDate);


        message.addRecipients(Message.RecipientType.TO,email);
        message.setSubject("[놀러감]"+email+"고객님 호텔 예약 전자 영수증입니다.");


        String msgg = "";

        msgg += "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;font-family:'AppeGothic','Malgun Gothic','맑은 고딕','돋음','Dotum','Apple SD Gothic Neo',Arial,Helvetica,sans-serif;word-break:keep-all;font-size:13px;line-height:1.5;color:#444;\">";
        msgg += "<tbody><tr><td align=\"center\">";
        msgg += "<div style=\"max-width:600px; margin:0 auto;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;border:1px solid #ddd;\">";
        msgg += "<tbody><tr><td height=\"38\" style=\"vertical-align:top;text-align:left;\"></td></tr>";
        msgg += "<tr><td style=\"padding:0 30px\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;\">";
        msgg += "<tbody><tr><td></td></tr>";
        msgg += "<tr><td height=\"40\"></td></tr><tr><td>";
        msgg += "<p style=\"margin:0;font-size:23px;line-height:33px;color:#444;font-weight:bold;\">숙소 예매가 완료되었습니다.</p>";
        msgg += "</td></tr><tr><td height=\"50\"></td></tr><tr><td>";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;border-top:2px solid #666;border-bottom:1px solid #ddd;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:30%\"><col style=\"width:auto\"></colgroup>";
        msgg += "<tbody><tr><td colspan=\"2\" height=\"20\"></td></tr>";
        msgg += "<tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\" height=\"30\">예약번호</th><td align=\"left\" height=\"30\"><span style=\"font-weight:bold;color:#ab7d55;\">"+UUID+"</span></td></tr>";
        msgg += "<tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\" height=\"30\">예약일자(GMT)</th><td align=\"left\" height=\"30\">"+date+"</td></tr>";
        msgg += "<tr><td colspan=\"2\" height=\"20\"></td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"30\"></td></tr><tr><td align=\"center\"></td></tr><tr><td height=\"30\"></td></tr><tr><td>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr></tr><tr><td>";
        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>예약자 정보</strong></p>";


        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
        msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
            msgg += "<tbody><tr><td align=\"left\" style=\"padding:25px 15px;border-right:1px solid #ddd;\"><p style=\"margin:0;color:#555;\"></p><p style=\"margin:0;font-weight:bold;font-size:15px;color:#555;\">" +
                    lastName+" "+firstName+"</p></td><td style=\"padding:15px 0 15px 15px;\"><p style=\"margin:0;\"></p><p style=\"margin:0;padding-bottom:5px;font-weight:bold;font-size:15px;color:#d60815;\">9882480107273</p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr></tbody>";
        msgg += "</table></td></tr><tr><td height=\"20\"></td></tr><tr><td>";


        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>예약 정보</strong></p>";
        msgg += "</td></tr><tr><td height=\"40\"></td></tr><tr><td valign=\"top\" style=\"text-align:left;\">";


        for(int i = 0; i < hotelList.size(); i ++){
            msgg += "<p style=\"margin:0;font-size:16px;\">예약 "+(i+1)+" ("+hotelList.get(i).getIataCodeObj().getCountryKo()+") "+hotelList.get(i).getHotelObj().getHotelName()+"</p>";
            msgg += "</td></tr><tr><td height=\"10\"></td></tr><tr><td>";
            msgg += "<div align='center'> <img src="+hotelList.get(i).getHotelObj().getSampleImage()+" style=width: 150px; height: 120px; object-fit: fill; alt=이미지></div>";
            msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border-top:2px solid #666;text-align:left;\">";
            msgg += "<colgroup><col style=\"width:35%\"><col style=\"width:auto\"></colgroup>";
            msgg += "<tbody><tr><td align=\"left\" style=\"padding:15px;border-right:1px solid #ddd;\"><p style=\"margin:0;\">체크인</p></td><td style=\"padding:15px;\"><p style=\"margin:0;font-weight:bold;\">" +
                    hotelList.get(i).getOfferObj().getCheckIn()+"</p><p style=\"margin:0;\"></p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr><tr><td align=\"left\" style=\"padding:15px;border-right:1px solid #ddd;\"><p style=\"margin:0;\">체크아웃</p></td><td style=\"padding:15px;\"><p style=\"margin:0;font-weight:bold;\">" +
                    hotelList.get(i).getOfferObj().getCheckOut()+"</p><p style=\"margin:0;\"></p></td></tr>";
            msgg += "<tr><td colspan=\"2\" height=\"1\" style=\"background-color:#ddd;\"></td></tr></tbody></table>";
            msgg += "</td></tr><tr><td height=\"30\"></td></tr><tr><td valign=\"top\" style=\"text-align:left;\">";
        }


        msgg += "<p style=\"margin:0;font-size:18px;\"><strong>결제 내역</strong></p>";
        msgg += "</td></tr><tr><td height=\"10\"></td></tr><tr><td style=\"background:#f0f0f0;padding:20px;\">";
        msgg += "<table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0;\">";
        msgg += "<tbody><tr><th align=\"left\" colspan=\"1\" rowspan=\"1\" scope=\"row\">총 지불금액 (결제완료)</th><td align=\"right\"><p style=\"padding:0;margin:0;color:#d60815;\">" +
                "<strong>KRW <span style=\"font-size:23px;\">"+" "+decimalFormat.format(totalPrice)+"</span></strong></p></td></tr></tbody></table>";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "본 예약은 결제가 완료된 건으로, 예매에 대한 예약 변경 및 환불 시 숙소규정 따른 예약 변경 수수료 (재발행 수수료) 및 환불 위약금 또는 환불 수수료가 징수될 수 있습니다. 자세한 내용은 운임 규정을 확인하시기 바랍니다.";
        msgg += "</td></tr><tr><td height=\"60\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr><tr><td height=\"20\"></td></tr><tr><td style=\"text-align:left;\">";
        msgg += "</td></tr></tbody></table></div></td></tr></tbody></table>";



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
