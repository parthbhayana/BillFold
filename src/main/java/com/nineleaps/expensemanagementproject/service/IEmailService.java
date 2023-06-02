package com.nineleaps.expensemanagementproject.service;

import java.io.FileNotFoundException;

import javax.mail.MessagingException;

public interface IEmailService {

	public void managerNotification(Long reportId) throws MessagingException, FileNotFoundException;

	void userApprovedNotification(Long reportId);

	void userRejectedNotification(Long reportId);

	void financeReimbursedNotification(Long reportId);

	void financeRejectedNotification(Long reportId);

}