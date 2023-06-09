package com.nineleaps.expensemanagementproject.service;

import java.io.FileNotFoundException;

import javax.mail.MessagingException;

public interface IEmailService {

	public void managerNotification(Long reportId) throws MessagingException, FileNotFoundException;

	public void userApprovedNotification(Long reportId);

	public void userRejectedNotification(Long reportId);

	public void financeReimbursedNotification(Long reportId);

	public void financeRejectedNotification(Long reportId);

}