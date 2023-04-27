package com.nineleaps.expensemanagementproject.service;

public interface EmailSenderService {
    void sendEmail(String reportingManagerEmail, String subject, String message);
}