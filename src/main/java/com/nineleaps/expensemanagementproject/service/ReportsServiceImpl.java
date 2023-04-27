package com.nineleaps.expensemanagementproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.FinanceApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.ManagerApprovalStatus;
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

	@Autowired
	private IEmployeeService empServices;

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
	public Reports addReport(Reports newReport, Long employeeId) {
		Employee emp = empServices.getEmployeeDetailsById(employeeId);
		String managerEmail = emp.getReportingManagerEmail();
		newReport.setManagerEmail(managerEmail);
//		expServices.updateExpense(newReport, employeeid);
		return reportsrepository.save(newReport);
	}

	@Override
	public Reports updateReport(Reports report, Long reportId) {
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setReportTitle(report.getReportTitle());
			re.setReportDescription(report.getReportDescription());
		}
		return reportsrepository.save(re);
	}

	@Override
	public Reports addExpenseToReport(Long reportId, Long expenseid) {
		boolean reportedStatus = true;
		Reports report = getReportById(reportId);
		Expense expense = expServices.getExpenseById(expenseid);
		if (report != null) {
			expense.setIsReported(reportedStatus);
			expRepo.save(expense);
			expServices.updateExpense(reportId, expenseid);
			report.setTotalAmount(totalamount(reportId));
			reportsrepository.save(report);
		}
		return report;
	}

	@Override
	public List<Reports> getReportByEmpId(Long employeeId) {
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
	public List<Reports> getReportsSubmittedToUser(String managerEmail) {
		List<Reports> reports = reportsrepository.findByManagerEmail(managerEmail);
		List<Reports> reportfinal = new ArrayList<>();
		for (Reports report3 : reports) {
			if (report3 != null && report3.getIsSubmitted() == true) {
				reportfinal.add(report3);
			}
		}
		List<Reports> reportsSubmittedToUserWODuplicates = reportfinal.stream().distinct().collect(Collectors.toList());
		return reportsSubmittedToUserWODuplicates;
	}

	@Override
	public List<Reports> getAllSubmittedReports() {
		List<Reports> Reports = reportsrepository.findAll();
		List<Reports> submitttedReports = new ArrayList<>();
		for (Reports reports2 : Reports) {
			if (reports2.getIsSubmitted() == true) {
				submitttedReports.add(reports2);
			}
		}
		return submitttedReports;
	}

	@Override
	public Reports addReportComments(Reports report, Long reportId) {
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setManagerComments(report.getManagerComments());
		}
		return reportsrepository.save(re);
	}

	@Override
	public Reports submitReport(Long reportId) {
		boolean submissionStatus = true;
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setIsSubmitted(submissionStatus);
		}

		return reportsrepository.save(re);
	}

	@Override
	public Reports approveReportByManager(Long reportId) {
		ManagerApprovalStatus approvalStaus = ManagerApprovalStatus.approve;
		Reports re = getReportById(reportId);
		if (re != null && re.getIsSubmitted() == true) {
			re.setManagerapprovalstatus(approvalStaus);
		}

		return reportsrepository.save(re);
	}

	@Override
	public Reports rejectReportByManager(Long reportId) {
		ManagerApprovalStatus approvalStaus = ManagerApprovalStatus.reject;
		Reports re = getReportById(reportId);
		if (re != null && re.getIsSubmitted() == true) {
			re.setManagerapprovalstatus(approvalStaus);
		}
		return reportsrepository.save(re);
	}

	@Override
	public Reports approveReportByFinance(Long reportId) {
		FinanceApprovalStatus approvalStaus = FinanceApprovalStatus.approve;
		Reports re = getReportById(reportId);
		if (re.getManagerapprovalstatus() == ManagerApprovalStatus.approve) {
			if (re != null && re.getIsSubmitted() == true) {
				re.setFinanceapprovalstatus(approvalStaus);
			}
		}
		return reportsrepository.save(re);
	}

	@Override
	public Reports rejectReportByFinance(Long reportId) {
		FinanceApprovalStatus approvalStaus = FinanceApprovalStatus.reject;
		Reports re = getReportById(reportId);
//		if(re.getIsSubmitted()==false)
//		{
//			return "Error! Report Not Submitted Yet!"
//		}
		if (re.getManagerapprovalstatus() == ManagerApprovalStatus.reject) {
			if (re != null && re.getIsSubmitted() == true) {
				re.setFinanceapprovalstatus(approvalStaus);
			}
		}
		return reportsrepository.save(re);
	}

	public float totalamount(Long reportId) {
		Reports report = reportsrepository.findById(reportId).get();
		List<Expense> expenses = expRepo.findByReports(report);
		float amt = 0;
		for (Expense expense2 : expenses) {
			amt += expense2.getAmount();
		}
		return amt;
	}

}
