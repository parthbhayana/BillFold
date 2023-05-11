package com.nineleaps.expensemanagementproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmailSenderService;

@RestController
public class EmailController {

    private final IEmailSenderService iEmailSenderService;

    public EmailController(IEmailSenderService iEmailSenderService) {
        this.iEmailSenderService = iEmailSenderService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody Employee emailMessage) { //, @RequestParam Employee employeeId
        this.iEmailSenderService.sendEmail(emailMessage.getReportingManagerEmail(), emailMessage.getSubject(), emailMessage.getMessage());//, emailMessage.getEmployeeId()
        return ResponseEntity.ok("Success");
    }
}
