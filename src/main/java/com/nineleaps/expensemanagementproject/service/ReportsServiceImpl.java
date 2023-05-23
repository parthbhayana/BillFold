package com.nineleaps.expensemanagementproject.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.ObjectNotFoundException;
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
	public Reports getReportById(Long reportId) {
		Optional<Reports> optionalReport = reportsrepository.findById(reportId);
		return optionalReport.orElseThrow(() -> new RuntimeException("Report not found"));
	}

	@Override
	public Reports addReport(Reports newReport, Long employeeId, List<Long> expenseids) {
		Employee emp = empServices.getEmployeeDetailsById(employeeId);
		String managerEmail = emp.getReportingManagerEmail();
		String employeeEmail = emp.getEmployeeEmail();
		newReport.setManagerEmail(managerEmail);
		newReport.setEmployeeMail(employeeEmail);
		LocalDate today = LocalDate.now();
		newReport.setDateCreated(today);
		reportsrepository.save(newReport);
		Long id = newReport.getReportId();
		List<Expense> expp = expRepo.findAllById(expenseids);
		float amt = 0;
		for (Expense expense2 : expp) {
			amt += expense2.getAmountINR();
		}
		long amtAsLong = Math.round(amt);
		newReport.setTotalAmount(amtAsLong);
		addExpenseToReport(id, expenseids);
		String reportTitle = newReport.getReportTitle();
		List<Expense> exp = expServices.getExpenseByReportId(id);
		for (Expense exp2 : exp) {
			if (exp != null) {
				exp2.setReportTitle(reportTitle);
				expRepo.save(exp2);
			}
		}
		return reportsrepository.save(newReport);
	}

	@Override
	public Reports updateReport(Reports report, Long reportId) {
		Reports re = getReportById(reportId);
		if (re != null && re.getIsHidden() != true) {
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
		if (report == null || report.getIsHidden() == true) {
			throw new NullPointerException("Report with ID " + reportId + " does not exist!");
		} else if (expense.getIsReported() == true) {
			throw new IllegalStateException("Expense with ID " + expenseid + " is already reported in another report!");
		} else if (report != null && expense.getIsReported() != true) {
			expense.setIsReported(reportedStatus);
			expRepo.save(expense);
			expServices.updateExpense(reportId, expenseid);
			float amt = totalamount(reportId);
			long amtAsLong = Math.round(amt);
			report.setTotalAmount(amtAsLong);
			reportsrepository.save(report);
			String reportTitle = report.getReportTitle();
			List<Expense> exp = expServices.getExpenseByReportId(reportId);
			for (Expense exp2 : exp) {
				if (exp2 != null) {
					exp2.setReportTitle(reportTitle);
					expRepo.save(exp2);
				}
			}
		}
		return report;
	}

	@Override
	public Reports addExpenseToReport(Long reportId, List<Long> expenseids) {
		boolean reportedStatus = true;
		Reports report = getReportById(reportId);
		if (report == null || report.getIsHidden() == true) {
			throw new NullPointerException("Report with ID " + reportId + " does not exist!");
		}
		for (Long expenseid : expenseids) {
			Expense expense = expServices.getExpenseById(expenseid);
			if (expense.getIsReported() == true) {
				throw new IllegalStateException(
						"Expense with ID " + expenseid + " is already reported in another report!");
			}
			if (report != null && expense.getIsReported() != true) {
				expense.setIsReported(reportedStatus);
				expRepo.save(expense);
				expServices.updateExpense(reportId, expenseid);
			}
		}
		String reportTitle = report.getReportTitle();
		List<Expense> exp = expServices.getExpenseByReportId(reportId);
		for (Expense exp2 : exp) {
			if (exp2 != null) {
				exp2.setReportTitle(reportTitle);
				expRepo.save(exp2);
			}
		}
		float amt = totalamount(reportId);
		long amtAsLong = Math.round(amt);
		report.setTotalAmount(amtAsLong);
		return reportsrepository.save(report);
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
	public List<Reports> getAllReportsApprovedByManager() {
		List<Reports> Reports = reportsrepository.findAll();
		List<Reports> ReportsApprovedByManager = new ArrayList<>();
		for (Reports reports2 : Reports) {
			if (reports2.getIsSubmitted() == true
					&& reports2.getManagerapprovalstatus() == ManagerApprovalStatus.APPROVED) {
				ReportsApprovedByManager.add(reports2);
			}
		}
		return ReportsApprovedByManager;
	}

	@Override
	public Reports submitReport(Long reportId, String managerMail) {
		boolean submissionStatus = true;
		ManagerApprovalStatus pending = ManagerApprovalStatus.PENDING;
		Reports re = getReportById(reportId);
		if (re != null) {
			re.setIsSubmitted(submissionStatus);
			re.setManagerapprovalstatus(pending);
			LocalDate today = LocalDate.now();
			re.setDateSubmitted(today);
			float amt = totalamount(reportId);
			long amtAsLong = Math.round(amt);
			re.setTotalAmount(amtAsLong);
			re.setManagerEmail(managerMail);
			reportsrepository.save(re);
			try {
				String decodedEmail = URLDecoder.decode(managerMail, "UTF-8");
				decodedEmail = decodedEmail.replace("=", "");
				re.setManagerEmail(decodedEmail);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Error decoding email: " + e.getMessage(), e);
			}
		}
		return reportsrepository.save(re);

	}

	@Override
	public Reports approveReportByManager(Long reportId, String comments) {
		ManagerApprovalStatus approvalStatus = ManagerApprovalStatus.APPROVED;
		FinanceApprovalStatus pending = FinanceApprovalStatus.PENDING;
		Reports re = getReportById(reportId);
		if (re.getIsSubmitted() == false) {
			throw new IllegalStateException("Report " + reportId + " is not Submitted!");
		}
		if (re == null || re.getIsHidden() == true) {
			throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
		}
		if (re != null && re.getIsHidden() == false && re.getIsSubmitted() == true) {
			re.setManagerapprovalstatus(approvalStatus);
			re.setManagerComments(comments);
			re.setFinanceapprovalstatus(pending);
		}

		return reportsrepository.save(re);
	}

	@Override
	public Reports rejectReportByManager(Long reportId, String comments) {
		ManagerApprovalStatus approvalStatus = ManagerApprovalStatus.REJECTED;
		Reports re = getReportById(reportId);
		if (re.getIsSubmitted() == false) {
			throw new IllegalStateException("Report " + reportId + " is not Submitted!");
		}
		if (re == null || re.getIsHidden() == true) {
			throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
		}
		if (re != null && re.getIsHidden() == false && re.getIsSubmitted() == true) {
			re.setManagerapprovalstatus(approvalStatus);
			re.setManagerComments(comments);
		}
		return reportsrepository.save(re);
	}

	@Override
	public Reports approveReportByFinance(Long reportId, String comments) {
		FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.REIMBURSED;
		Reports re = getReportById(reportId);
		if (re.getIsSubmitted() == false) {
			throw new IllegalStateException("Report " + reportId + " is not Submitted!");
		}
		if (re == null || re.getIsHidden() == true) {
			throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
		}
		if (re != null && re.getIsHidden() == false && re.getIsSubmitted() == true
				&& re.getManagerapprovalstatus() == ManagerApprovalStatus.APPROVED) {
			re.setFinanceapprovalstatus(approvalStatus);
			re.setFinanceComments(comments);
		}
		return reportsrepository.save(re);
	}

	@Override
	public Reports rejectReportByFinance(Long reportId, String comments) {
		FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.REJECT;
		Reports re = getReportById(reportId);
		if (re.getIsSubmitted() == false) {
			throw new IllegalStateException("Report " + reportId + " is not Submitted!");
		}
		if (re == null || re.getIsHidden() == true) {
			throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
		}
		if (re != null && re.getIsHidden() == false && re.getIsSubmitted() == true
				&& re.getManagerapprovalstatus() == ManagerApprovalStatus.APPROVED) {
			re.setFinanceapprovalstatus(approvalStatus);
			re.setFinanceComments(comments);
		}
		return reportsrepository.save(re);
	}

	@Override
	public float totalamount(Long reportId) {
		Reports report = reportsrepository.findById(reportId).get();
		List<Expense> expenses = expRepo.findByReports(report);

		float amt = 0;
		for (Expense expense2 : expenses) {
			amt += expense2.getAmountINR();
		}
		return amt;
	}

	@Override
	public void hideReport(Long reportId) {
		Boolean hidden = true;
		Reports report = getReportById(reportId);
		report.setIsHidden(hidden);
		reportsrepository.save(report);

	}

}