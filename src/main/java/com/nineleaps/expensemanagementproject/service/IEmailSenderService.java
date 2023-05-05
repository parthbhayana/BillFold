package com.nineleaps.expensemanagementproject.service;

public interface IEmailSenderService {

	void sendEmail(String reportingManagerEmail, String subject, String message);

}