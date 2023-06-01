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

	@Autowired
	private IEmailService emailService;

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
		String currency = null;
		if (!expenseids.isEmpty()) {
			Long firstExpenseId = expenseids.get(0);
			Expense ep = expRepo.getExpenseByexpenseId(firstExpenseId);
			currency = ep.getCurrency();
		}
		newReport.setCurrency(currency);
		reportsrepository.save(newReport);
		Long id = newReport.getReportId();
		List<Expense> expp = expRepo.findAllById(expenseids);
		float amt = 0;
		for (Expense expense2 : expp) {
			amt += expense2.getAmountINR();
		}
		newReport.setTotalAmountINR(amt);
		float amtCurrency = 0;
		for (Expense expense2 : expp) {
			amtCurrency += expense2.getAmount();
		}
		newReport.setTotalAmountCurrency(amtCurrency);
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
			List<Expense> expenseList = expServices.getExpenseByReportId(reportId);
			for (Expense exp : expenseList) {
				if (exp != null) {
					exp.setReportTitle(report.getReportTitle());
				}
			}
		}
		return reportsrepository.save(re);
	}

	@Override
	public List<Reports> editReport(Long reportId, String reportTitle, String reportDescription,
			List<Long> addExpenseIds, List<Long> removeExpenseIds) {
		Long empId = null;
		Reports report = getReportById(reportId);
		if (report == null || report.getIsHidden() == true) {
			throw new NullPointerException("Report with ID " + reportId + " does not exist!");
		}
		if (report != null && report.getIsHidden() != true) {
			report.setReportTitle(reportTitle);
			report.setReportDescription(reportDescription);
			reportsrepository.save(report);
			// Updating Report Title in existing expenses
			List<Expense> expenseList = expServices.getExpenseByReportId(reportId);
			for (Expense exp : expenseList) {
				if (exp != null) {
					exp.setReportTitle(reportTitle);
					expRepo.save(exp);
					// Fetching employeeId
					Expense expense = expenseList.get(0);
					Employee employee = expense.getEmployee();
					empId = employee.getEmployeeId();
				}
			}
			// Adding Expenses
			Reports re = getReportById(reportId);
			for (Long expenseid : addExpenseIds) {
				Expense expense = expServices.getExpenseById(expenseid);
				if (expense.getIsReported() == true) {
					throw new IllegalStateException(
							"Expense with ID " + expenseid + " is already reported in another report!");
				}
				if (report != null && expense.getIsReported() != true) {
					expServices.updateExpense(reportId, expenseid);
				}
			}
			reportsrepository.save(re);
			// Removing Expenses
			boolean reportedStatus = false;
			for (Long expenseid : removeExpenseIds) {
				Expense expense = expServices.getExpenseById(expenseid);
				if (report != null && expense.getIsReported() == true) {
					expense.setIsReported(reportedStatus);
					expense.setReports(null);
					expense.setReportTitle(null);
					expRepo.save(expense);
				}
			}

		}
		// Setting total amounts
		List<Expense> expp = expServices.getExpenseByReportId(reportId);
		Reports newReport = getReportById(reportId);
		float amt = 0;
		for (Expense expense2 : expp) {
			amt += expense2.getAmountINR();
		}
		newReport.setTotalAmountINR(amt);
		float amtCurrency = 0;
		for (Expense expense2 : expp) {
			amtCurrency += expense2.getAmount();
		}
		newReport.setTotalAmountCurrency(amtCurrency);
		Reports re = getReportById(reportId);
		re.setTotalAmountINR(totalamountINR(reportId));
		re.setTotalAmountCurrency(totalamountCurrency(reportId));
		// Fetching Employee ID
//		List<Expense> expenseList = expServices.getExpenseByReportId(reportId);
//		Expense expense = expenseList.get(0);
//		Employee employee = expense.getEmployee();
//		Long employeeId = employee.getEmployeeId();
		return getReportByEmpId(empId);
	}

	@Override
	public Reports removeExpenseFromReport(Long reportId, List<Long> expenseIds) {
		boolean reportedStatus = false;
		Reports report = getReportById(reportId);
		if (report == null || report.getIsHidden() == true) {
			throw new NullPointerException("Report with ID " + reportId + " does not exist!");
		}
		for (Long expenseid : expenseIds) {
			Expense expense = expServices.getExpenseById(expenseid);
			if (report != null && expense.getIsReported() == true) {
				expense.setIsReported(reportedStatus);
				expense.setReports(null);
				expense.setReportTitle(null);
				expRepo.save(expense);
			}
		}
		return null;
	}

	@Override
	public Reports addExpenseToReport(Long reportId, List<Long> expenseids) {
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
				expServices.updateExpense(reportId, expenseid);
			}
		}
		Reports re = getReportById(reportId);
		re.setTotalAmountINR(totalamountINR(reportId));
		re.setTotalAmountCurrency(totalamountCurrency(reportId));
		return reportsrepository.save(re);
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
		if (re == null || re.getIsHidden() == true) {
			throw new NullPointerException("Report with ID " + reportId + " does not exist!");
		}
		if (re != null && re.getIsSubmitted() == submissionStatus) {
			throw new IllegalStateException("Report with ID " + reportId + " is already submitted!");
		}
		if (re != null && re.getIsSubmitted() != submissionStatus) {
			re.setIsSubmitted(submissionStatus);
			re.setManagerapprovalstatus(pending);
			LocalDate today = LocalDate.now();
			re.setDateSubmitted(today);
			re.setTotalAmountINR(totalamountINR(reportId));
			re.setTotalAmountCurrency(totalamountCurrency(reportId));
			re.setManagerEmail(managerMail);
			reportsrepository.save(re);
			// Email Functionality Integration
			emailService.managerNotification(reportId);
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
	public void approveReportByManager(Long reportId, String comments) {
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
			LocalDate today = LocalDate.now();
			re.setManagerActionDate(today);
			reportsrepository.save(re);
			emailService.userApprovedNotification(reportId);
		}
	}

	@Override
	public void rejectReportByManager(Long reportId, String comments) {
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
			LocalDate today = LocalDate.now();
			re.setManagerActionDate(today);
			reportsrepository.save(re);
			emailService.userRejectedNotification(reportId);
		}
	}

	@Override
	public void approveReportByFinance(Long reportId, String comments) {
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
			LocalDate today = LocalDate.now();
			re.setFinanceActionDate(today);
			reportsrepository.save(re);
			emailService.financeReimbursedNotification(reportId);
		}
	}

	@Override
	public void rejectReportByFinance(Long reportId, String comments) {
		FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.REJECTED;
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
			LocalDate today = LocalDate.now();
			re.setFinanceActionDate(today);
			reportsrepository.save(re);
			emailService.financeRejectedNotification(reportId);
		}
	}

	@Override
	public float totalamountINR(Long reportId) {
		Reports report = reportsrepository.findById(reportId).get();
		List<Expense> expenses = expRepo.findByReports(report);

		float amtINR = 0;
		for (Expense expense2 : expenses) {
			amtINR += expense2.getAmountINR();
		}
		return amtINR;
	}

	@Override
	public float totalamountCurrency(Long reportId) {
		Reports report = reportsrepository.findById(reportId).get();
		List<Expense> expenses = expRepo.findByReports(report);

		float amtCurrency = 0;
		for (Expense expense2 : expenses) {
			amtCurrency += expense2.getAmount();
		}
		return amtCurrency;
	}

	@Override
	public void hideReport(Long reportId) {
		Boolean hidden = true;
		Boolean isReported = false;
		Reports report = getReportById(reportId);
		List<Expense> expenseList = expServices.getExpenseByReportId(reportId);
		for (Expense exp : expenseList) {
			exp.setIsReported(isReported);
			exp.setReports(null);
			exp.setReportTitle(null);
			expRepo.save(exp);
		}
		report.setIsHidden(hidden);
		reportsrepository.save(report);

	}

	@Override
	public List<Reports> getReportsInDateRange(LocalDate startDate, LocalDate endDate) {
		List<Reports> reports = reportsrepository.findByDateSubmittedBetween(startDate, endDate);
		return reports;
	}

	@Override
	public List<Reports> getReportsSubmittedToUserInDateRange(String managerEmail, LocalDate startDate,
			LocalDate endDate) {
		List<Reports> reports = reportsrepository.findByManagerEmailAndDateSubmittedBetween(managerEmail, startDate,
				endDate);
		return reports;
	}

	@Override
	public String getAmountOfReportsInDateRange(LocalDate startDate, LocalDate endDate) {
		List<Reports> reports = reportsrepository.findByDateSubmittedBetween(startDate, endDate);
		float total = 0;
		for (Reports report2 : reports) {
			total += report2.getTotalAmountINR();
		}
		return (total + " INR");
	}
}