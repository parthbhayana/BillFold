package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Reports;

public interface IReportsService {

	public List<Reports> getAllReports();

	void deleteReportById(Long reportId);

	public Reports getReportById(Long reportId);

	public Reports addReport(Reports newReport);

	public List<Reports> getReportByEmpId(Long employeeId);

	public Reports updateReport(Reports report, Long employeeId);

	Reports addExpenseToReport(Long reportId, Long employeeid);

	public Reports addReportComments(Reports report, Long reportId);

	public Reports submitReport(Long reportId);

	public Reports approveReport(Long reportId);

	public Reports rejectReport(Long reportId);

}
