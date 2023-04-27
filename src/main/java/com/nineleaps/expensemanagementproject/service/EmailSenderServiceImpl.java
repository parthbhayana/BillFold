package com.nineleaps.expensemanagementproject.service;

import org.springframework.stereotype.Service;


import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
@Service
public class EmailSenderServiceImpl implements EmailSenderService {
	@Autowired
	EmployeeRepository emprepository;
    private final JavaMailSender mailSender;

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail( String to, String subject, String message) {

    	
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("karthikdemoemail@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        this.mailSender.send(simpleMailMessage);
    }
}