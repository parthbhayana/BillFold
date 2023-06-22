package com.nineleaps.expensemanagementproject.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.entity.Reports;

public interface IReportsService {

	public List<Reports> getAllReports();

	public Reports getReportById(Long reportId);

	public Reports addReport(Reports newReport, Long employeeId, List<Long> expenseids);

	public List<Reports> editReport(Long reportId, String reportTitle, String reportDescription,
			List<Long> addExpenseIds, List<Long> removeExpenseIds);

	public List<Reports> getReportByEmpId(Long employeeId, String request);

	public Reports addExpenseToReport(Long reportId, List<Long> employeeids);


    public void submitReport(Long reportId, String managerMail,HttpServletResponse response) throws MessagingException,FileNotFoundException, IOException;
	public void approveReportByManager(Long reportId, String comments);

	public void rejectReportByManager(Long reportId, String comments);

	public void approveReportByFinance(Long reportId, String comments);

	public void rejectReportByFinance(Long reportId, String comments);

	public List<Reports> getAllSubmittedReports();

	public List<Reports> getReportsSubmittedToUser(String managerEmail, String request);

	public List<Reports> getAllReportsApprovedByManager(String request);

	public void hideReport(Long reportId);

	public float totalamountINR(Long reportId);

	public float totalamountCurrency(Long reportId);

	public List<Reports> getReportsInDateRange(LocalDate startDate, LocalDate endDate);

	public String getAmountOfReportsInDateRange(LocalDate startDate, LocalDate endDate);

	public List<Reports> getReportsSubmittedToUserInDateRange(String managerEmail, LocalDate startDate,
			LocalDate endDate, String request);
}