package com.nineleaps.expensemanagementproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service
public class ReportsServiceImpl implements IReportsService {

	@Autowired
	private ReportsRepository reportsrepository;

	@Autowired
	private ExpenseRepository expRepo;
	
	@Autowired
	private IExpenseService expServices;

	@Override
	public List<Reports> getAllReports() {
		return reportsrepository.findAll();
	}

	@Override
	public void deleteReportById(Long reportId) {
		reportsrepository.deleteById(reportId);
	}

	@Override
	public Reports getReportById(Long reportId) {
		Optional<Reports> optionalReport = reportsrepository.findById(reportId);
		return optionalReport.orElseThrow(() -> new RuntimeException("Report not found"));
	}

	@Override
	public Reports addReport(Reports newReport) {
//		expServices.updateExpense(newReport, employeeid);
		return reportsrepository.save(newReport);

	}

	@Override
	public Reports addExpenseToReport(Long reportId, Long expenseid) {
		boolean reportedStatus = true;
		Reports report = getReportById(reportId);
		Expense expense = expServices.getExpenseById(expenseid);
		if (report != null) {
			expense.setIsReported(reportedStatus);
			expServices.updateExpense(reportId, expenseid);
			
		}
		reportsrepository.save(report);
		expRepo.save(expense);
		return null;

	}

	@Override
	public List<Reports> getReportByEmpId(Long employeeId) {
		// TODO Auto-generated method stub

		List<Expense> expense = expServices.getExpenseByEmployeeId(employeeId);
		List<Reports> report = new ArrayList<>();
		for (Expense expense2 : expense) {
			if (expense2.getReports() != null) {
				report.add(expense2.getReports());
			}
		}
		List<Reports> reportWithoutDuplicates = report.stream().distinct().collect(Collectors.toList());
		return reportWithoutDuplicates;
	}

	@Override
	public Reports updateReport(Reports report, Long reportId) {
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setReportTitle(report.getReportTitle());
			re.setReportDescription(report.getReportDescription());
		}
		reportsrepository.save(re);
		return null;
	}

	@Override
	public Reports addReportComments(Reports report, Long reportId) {
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setReportComments(report.getReportComments());
		}
		reportsrepository.save(re);
		return null;
	}

	@Override
	public Reports submitReport(Long reportId) {
		boolean submissionStatus = true;
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setIsSubmitted(submissionStatus);
		}
		reportsrepository.save(re);
		return null;
	}

	@Override
	public Reports approveReport(Long reportId) {
		boolean approvalStaus = true;
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setIsAprooved(approvalStaus);
		}
		reportsrepository.save(re);
		return null;
	}

	@Override
	public Reports rejectReport(Long reportId) {
		boolean approvalStaus = false;
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setIsAprooved(approvalStaus);
		}
		reportsrepository.save(re);
		return null;
	}

}
