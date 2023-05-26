package com.nineleaps.expensemanagementproject.service;

public interface IEmailService {

	public void managerNotification(Long reportId);

	void userApprovedNotification(Long reportId);

	void userRejectedNotification(Long reportId);

	void financeReimbursedNotification(Long reportId);

	void financeRejectedNotification(Long reportId);

}