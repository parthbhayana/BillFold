package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Reports;

public interface IReportsService {

	public List<Reports> getAllReports();

	public Reports getReportById(Long reportId);

	public Reports addReport(Reports newReport, Long employeeId, List<Long> expenseids);

	public List<Reports> getReportByEmpId(Long employeeId);

	public Reports updateReport(Reports report, Long employeeId);

	public Reports addExpenseToReport(Long reportId, List<Long> employeeids);

	public Reports submitReport(Long reportId, String managerMail);

	public void approveReportByManager(Long reportId, String comments);

	public void rejectReportByManager(Long reportId, String comments);

	public void approveReportByFinance(Long reportId, String comments);

	public void rejectReportByFinance(Long reportId, String comments);

	public List<Reports> getAllSubmittedReports();

	public List<Reports> getReportsSubmittedToUser(String managerEmail);

	public List<Reports> getAllReportsApprovedByManager();

	public void hideReport(Long reportId);

	public float totalamountINR(Long reportId);

	public float totalamountCurrency(Long reportId);

	public List<Reports> getReportsInDateRange(LocalDate startDate, LocalDate endDate);

	public String getAmountOfReportsInDateRange(LocalDate startDate, LocalDate endDate);

	public Reports removeExpenseFromReport(Long reportId, List<Long> expenseIds);

	public List<Reports> editReport(Long reportId, String reportTitle, String reportDescription, List<Long> expenseids);

	public List<Reports> getReportsSubmittedToUserInDateRange(String managerEmail, LocalDate startDate, LocalDate endDate);
}