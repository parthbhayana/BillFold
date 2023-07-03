package com.nineleaps.expensemanagementproject.service;

import java.io.FileNotFoundException;
import java.util.List;

import javax.mail.MessagingException;

public interface IEmailService {

	void managerNotification(Long reportId) throws MessagingException, FileNotFoundException;

	void userApprovedNotification(Long reportId);

	void userRejectedNotification(Long reportId);

	void financeReimbursedNotification(Long reportId);

	void financeRejectedNotification(Long reportId);

	void reminderMailToEmployee(List<Long> expenseIds);

    void reminderMailToManager(List<Long> reportIds);
}