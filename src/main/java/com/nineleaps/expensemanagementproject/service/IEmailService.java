package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.entity.Expense;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IEmailService {


	void userRejectedNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException;

	void managerNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException;

	void userApprovedNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException,MessagingException;

	void financeReimbursedNotification(Long reportId);

	void financeRejectedNotification(Long reportId);

	public void userPartialApprovedExpensesNotification(Long reportId);

	void reminderMailToEmployee(List<Long> expenseIds);

    void reminderMailToManager(List<Long> reportIds);


	void financeNotificationToReimburse(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException;
}