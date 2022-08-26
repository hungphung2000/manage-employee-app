package com.example.manageemployeeapp.services.impl;

import com.example.manageemployeeapp.dto.UserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class AsyncMailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void sendMail(UserRequestDTO user) {
        String subject = "USER_INFORMATION";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("vinhyen2k@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText("Name: " + user.getName() + ",Email: " + user.getEmail() + ",Password: " + user.getPassword());

        javaMailSender.send(mailMessage);
    }


}
