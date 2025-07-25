package com.project.notelication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("NoteLication - 이메일 인증 코드");
        message.setText("귀하의 인증 코드는 다음과 같습니다: " + code + "\n\n이 코드를 사용하여 회원가입을 완료해주세요.");
        mailSender.send(message);
    }
}
