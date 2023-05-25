package com.nineleaps.expensemanagementproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expensemanagementproject.service.IEmailSenderService;

@RestController
public class EmailController {

	@Autowired
	private IEmailSenderService emailService;

	@PostMapping("/sendemail/{empId}")
	public void sendEmail(@RequestParam Long empId) {
		emailService.sendEmail(empId);
	}
}
