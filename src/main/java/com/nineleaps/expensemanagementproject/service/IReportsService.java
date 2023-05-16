package com.nineleaps.expensemanagementproject.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;

public interface IReportsService {

	public List<Reports> getAllReports();

	void deleteReportById(Long reportId);

	public Reports getReportById(Long reportId);

//	public Reports addReport(Reports newReport, Long employeeId);
	
	

	public List<Reports> getReportByEmpId(Long employeeId);

	public Reports updateReport(Reports report, Long employeeId);

	public Reports addExpenseToReport(Long reportId, Long employeeid);
	
	public Reports addExpenseToReport(Long reportId, List<Long> employeeids);

	//public Reports submitReport(Long reportId,String managerMail);

	public Reports approveReportByManager(Long reportId, String comments);

	public Reports rejectReportByManager(Long reportId, String comments);
	
	public Reports approveReportByFinance(Long reportId, String comments);

	public Reports rejectReportByFinance(Long reportId, String comments);
	
	public List<Reports> getAllSubmittedReports();

	public List<Reports> getReportsSubmittedToUser(String managerEmail);
	
	public List<Reports> getAllReportsApprovedByManager();

	public void hideReport(Long reportId);

	public float totalamount(Long reportId);


	public Reports addReport(Reports newReport, Long employeeId, List<Long> expenseids);

//	public Reports submitReport(Long reportId, String managerMail, HttpServletResponse response);

	public Reports submitReport(Long reportId, String managerMail);

	
	

	


	
}