package com.example.BapZip.service.MailService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.web.dto.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailServiceImpl implements MailService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public MailDTO.CertificationNumber sendCertificationNumberEmail(String mail){
        Random random = new Random();
        Integer randomNumber = random.nextInt(9000) + 1000;
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("[ 밥zip ] - 본인 인증 ");
            message.setTo(mail);
            message.setText(" 인증 번호 : "+randomNumber.toString());
            javaMailSender.send(message);
            return MailDTO.CertificationNumber.builder().number(randomNumber).build();
        }
        catch (Exception e){
            throw new GeneralException(ErrorStatus.EMAIL_ERROR);
        }

    }
}
