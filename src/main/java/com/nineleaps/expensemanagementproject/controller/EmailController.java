package com.nineleaps.expensemanagementproject.controller;

import java.io.FileNotFoundException;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expensemanagementproject.service.IEmailService;

@RestController
public class EmailController {

	@Autowired
	private IEmailService emailService;

	@PostMapping("/send_email/{employee_id}")
	public void sendEmail(@RequestParam Long reportId) throws FileNotFoundException, MessagingException {
		emailService.managerNotification(reportId);
	}
}