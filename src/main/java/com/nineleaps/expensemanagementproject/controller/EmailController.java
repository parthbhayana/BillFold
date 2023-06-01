package com.nineleaps.expensemanagementproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expensemanagementproject.service.IEmailService;

@RestController
public class EmailController {

	@Autowired
	private IEmailService emailService;

	@PostMapping("/sendemail/{empId}")
	public void sendEmail(@RequestParam Long reportId) {
		emailService.managerNotification(reportId);
	}
}
