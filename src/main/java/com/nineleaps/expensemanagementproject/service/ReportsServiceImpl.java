package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service
public class ReportsServiceImpl implements IReportsService {

	@Autowired
	private ReportsRepository reportsrepository;

	@Autowired
	private IExpenseService expServices;

	@Override
	public List<Reports> getAllReports() {
		return reportsrepository.findAll();
	}

	@Override
	public void deleteReport(Long reportId) {
		reportsrepository.deleteById(reportId);

	}

	@Override
	public Reports getReportByReportId(Long reportId) {
		return reportsrepository.findById(reportId).get();
	}

	@Override
	public Reports addReport(Reports newReport, Long employeeid) {
		expServices.updateExpense(newReport, employeeid);
		return reportsrepository.save(newReport);

	}

}
