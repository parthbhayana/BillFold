package com.nineleaps.expense_management_project.service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IEmailService {

	void managerNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException;

	void managerNotification(Long reportId, List<Long> expenseIds, String managerEmail, HttpServletResponse response) throws IOException,
			MessagingException;



	void userApprovedNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException;

	void userReimbursedNotification(Long reportId);

	void userRejectedByFinanceNotification(Long reportId);

	 void userPartialApprovedExpensesNotification(Long reportId);

	void reminderMailToEmployee(List<Long> expenseIds);

	void reminderMailToManager(List<Long> reportIds);

	void financeNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException;


	void notifyHr(Long reportId, String hrEmail, String hrName) throws MessagingException;

	void notifyLnD(Long reportId, String lndEmail, String lndName) throws MessagingException;

}