package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.CategoryFinanceRepository;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service
public class ExpenseServiceImpl implements IExpenseService {
	@Autowired
	private ExpenseRepository expRepository;

	@Autowired
	private EmployeeRepository empRepository;

	@Autowired
	private IEmployeeService employeeSERVICES;

	@Autowired
	private CategoryFinanceRepository catrepository;

	@Autowired
	private IReportsService reportServices;

	@Autowired
	private ReportsRepository reportsRepo;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ICurrencyExchange CurrencyExchange;

	@Transactional
	@Override
	public Expense addExpense(Expense expense, Long employeeid, Long catId) {
		Employee empDetails = employeeSERVICES.getEmployeeDetailsById(employeeid);
		Category catfin = catrepository.findById(catId).get();
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		expense.setEmployee(empDetails);
		expense.setCategoryfinance(catfin);
		Category mergedCategoryFinance = entityManager.merge(catfin);
		expense.setCategoryfinance(mergedCategoryFinance);
		expense.setCatDescription(mergedCategoryFinance.getCatDescription());
		expense.setDate(today);
		expense.setTime(now);
		expRepository.save(expense);
		String curr = expense.getCurrency();
		double rate = CurrencyExchange.getExchangeRate(curr);
		double amountininr = expense.getAmount() * rate;
		expense.setAmountINR((float) amountininr);
		return expRepository.save(expense);
	}

	@Override
	public List<Expense> getAllExpenses() {
		return expRepository.findAll();
	}

	@Override
	public Expense getExpenseById(Long expenseId) {
		return expRepository.findById(expenseId).get();
	}

//	@Override
//	public Expense updateExpense(Long reportId, Long employeeId) {
//		Expense exp = getExpenseById(employeeId);
//		Reports report = reportServices.getReportById(reportId);
//		if (exp != null) {
//			exp.setReports(report);
//		}
//		return expRepository.save(exp);
//	}
	
	@Override
	public Expense updateExpense(Long reportId, Long employeeId) {
		Expense exp = getExpenseById(employeeId);
		Reports report = reportServices.getReportById(reportId);
		String reportTitle = report.getReportTitle();
		boolean reportedStatus = true;
		if (exp != null) {
			exp.setReports(report);
			exp.setIsReported(reportedStatus);
			exp.setReportTitle(reportTitle);
		}
		return expRepository.save(exp);
	}

//	@Override
//	public Expense updateSupportingDocument( String supportingDoc, Long expenseId) {
//
//		Expense exp = getExpenseById(expenseId);
//		if (exp != null) {
//			exp.setSupportingDocument(supportingDoc);
//		}
//		expRepository.save(exp);
//		return null;
//	}

	@Override
	public void deleteExpenseById(Long expenseId) {
		expRepository.deleteById(expenseId);
	}

	@Override
	public List<Expense> getExpenseByEmployeeId(Long employeeId) {
		Employee employee = empRepository.findById(employeeId).get();
		return expRepository.findByEmployee(employee);
	}

	@Override
	public Expense updateSupportingDocument(String supportingDoc, Long expenseId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expense updateExpenses(Expense newExpense, Long expenseId) {
		Expense expense = getExpenseById(expenseId);
		expense.setMerchantName(newExpense.getMerchantName());
		expense.setDate(newExpense.getDate());
		expense.setAmount(newExpense.getAmount());
		expense.setDescription(newExpense.getDescription());
		// expense.setCategory(newExpense.getCategory());
		expense.setSupportingDocuments(newExpense.getSupportingDocuments());
		return expRepository.save(expense);
	}

	@Override
	public List<Expense> getExpensesByEmployeeId(Long employeeId) {
		Employee employee = empRepository.findById(employeeId).get();
		return expRepository.findByEmployeeAndIsReported(employee, false);
	}

	@Override
	public Expense removeTaggedExpense(Long expenseId) {
		Expense exp = expRepository.findById(expenseId).get();
		boolean removeExpense = false;
		exp.setIsReported(removeExpense);
		exp.setReports(null);
		return expRepository.save(exp);
	}

	@Override
	public List<Expense> getExpenseByReportId(Long reportId) {
		Reports report = reportsRepo.findById(reportId).get();
		return expRepository.findByReports(report);
	}

	@Override
	public void hideExpense(Long expId) {
		Boolean hidden = true;
		Expense exp = getExpenseById(expId);
		exp.setIsHidden(hidden);
		expRepository.save(exp);
	}
}