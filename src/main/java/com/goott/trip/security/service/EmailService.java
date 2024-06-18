package com.goott.trip.security.service;

import com.goott.trip.hamster.model.Testproduct;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
        msgg+= "<h1>  </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>비밀번호찾기 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= code + "</strong><div><br/> ";
        msgg+= "</div>";
        session.setAttribute("sCode", code);
        message.setText(msgg,"UTF-8","HTML");
        javaMailSender.send(message);

        return msgg;
    }

    public String sendAirplaneEmail(String email, HttpSession session,String UUID,Testproduct cont) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO,email);
        message.setSubject("[tripHamsic]"+email+"고객님 전자 영수증입니다.");

        String msgg = "";
        msgg+= "<p>안녕하세요. tripHamsic입니다.</p>";
        msgg+= "<p>"+email+"고객님께서 결제하신 주문의 전자 영수증입니다.</p>";
        msgg+= "<br>";
        msgg+= "<br>";
        msgg += "<table border='1' width='700' cellspacing='0'>";
        msgg += "<tr>";
        msgg += "<th>출발지</th>";
        msgg += "<th>목적지</th>";
        msgg += "<th>비행시간</th>";
        msgg += "<th>항공사</th>";
        msgg += "<th>결제금액</th>";
        msgg += "</tr>";

        msgg += "<tr>";
        msgg += "<td>"+cont.getAirplaneDepart()+"</td>";
        msgg += "<td>"+cont.getAirplaneArrive()+"</td>";
        msgg += "<td>"+cont.getAirplaneDepartTime().toString().substring(5,16) + " ~ " + cont.getAirplaneArriveTime().toString().substring(5,16)+"</td>";
        msgg += "<td>"+cont.getAirplaneName()+"</td>";
        msgg += "<td>"+cont.getAirplanePrice()+"</td>";
        msgg += "</tr>";
        msgg += "</table>";

        message.setText(msgg,"UTF-8","HTML");
        javaMailSender.send(message);

        return msgg;
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
