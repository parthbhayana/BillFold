package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Reports;

public interface IReportsService {

	public List<Reports> getAllReports();

	public void deleteReport(Long reportId);

	public Reports getReportByReportId(Long reportId);

	public Reports addReport(Reports newReport, Long employeeid);
}
