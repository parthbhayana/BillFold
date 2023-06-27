package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service
public class ExpenseServiceImpl implements IExpenseService {
	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private IEmployeeService employeeService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private IReportsService reportServices;

	@Autowired
	private ReportsRepository reportsRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ICurrencyExchange CurrencyExchange;

	@Transactional
	@Override
	public Expense addExpense(Expense expense, Long employeeId, Long categoryId) {
		Employee employee = employeeService.getEmployeeDetailsById(employeeId);
		Category category = categoryRepository.findById(categoryId).get();
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		expense.setEmployee(employee);
		expense.setCategory(category);
		Category mergedCategory = entityManager.merge(category);
		expense.setCategory(mergedCategory);
		expense.setCategoryDescription(mergedCategory.getCategoryDescription());
		expense.setDate(today);
		expense.setTime(now);
		expenseRepository.save(expense);
		String curr = expense.getCurrency();
		double rate = CurrencyExchange.getExchangeRate(curr);
		double amountininr = expense.getAmount() * rate;
		expense.setAmountINR((float) amountininr);
		return expenseRepository.save(expense);
	}

	@Override
	public List<Expense> getAllExpenses() {
		LocalDate sixtyDaysAgo = LocalDate.now().minusDays(60);
		List<Expense> unreportedExpenses = expenseRepository.findByIsReportedAndDateBefore(false, sixtyDaysAgo);
		for(Expense expense:unreportedExpenses)
		{
			expense.setIsHidden(true);
			expenseRepository.save(expense);
		}
		return expenseRepository.findAll();
	}

	@Override
	public Expense getExpenseById(Long expenseId) {
		return expenseRepository.findById(expenseId).get();
	}

	@Override
	public Expense updateExpense(Long reportId, Long employeeId) {
		Expense expense = getExpenseById(employeeId);
		Reports report = reportServices.getReportById(reportId);
		String reportTitle = report.getReportTitle();
		boolean reportedStatus = true;
		if (expense != null) {
			expense.setReports(report);
			expense.setIsReported(reportedStatus);
			expense.setReportTitle(reportTitle);
		}
		return expenseRepository.save(expense);
	}

	@Override
	public void deleteExpenseById(Long expenseId) {
		expenseRepository.deleteById(expenseId);
	}

	@Override
	public List<Expense> getExpenseByEmployeeId(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId).get();
		return expenseRepository.findByEmployeeAndIsHidden(employee, false);
	}

	@Override
	public Expense updateSupportingDocument(String supportingDoc, Long expenseId) {
		return null;
	}

	@Override
	public Expense updateExpenses(Expense newExpense, Long expenseId) {
		Expense expense = getExpenseById(expenseId);
		if(expense.getIsHidden()!=true)
		{
			expense.setMerchantName(newExpense.getMerchantName());
			expense.setDate(newExpense.getDate());
			expense.setAmount(newExpense.getAmount());
			expense.setDescription(newExpense.getDescription());
			expense.setSupportingDocuments(newExpense.getSupportingDocuments());
			LocalDate today = LocalDate.now();
			LocalTime now = LocalTime.now();
			expense.setDate(today);
			expense.setTime(now);
			expense.setCurrency(newExpense.getCurrency());
			expenseRepository.save(expense);
			String curr = expense.getCurrency();
			double rate = CurrencyExchange.getExchangeRate(curr);
			double amountininr = expense.getAmount() * rate;
			expense.setAmountINR((float) amountininr);
			expenseRepository.save(expense);
		}
		if(expense.getIsHidden()==true) {
			throw new IllegalStateException("Expense " + expenseId + " does not exist!");
		}
		return expenseRepository.save(expense);
	}

	@Override
	public List<Expense> getExpensesByEmployeeId(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId).get();
		return expenseRepository.findByEmployeeAndIsReported(employee, false);
	}

	@Override
	public Expense removeTaggedExpense(Long expenseId) {
		Expense expense = expenseRepository.findById(expenseId).get();
		boolean removeExpense = false;
		if(expense.getIsHidden()!=true) {
			expense.setIsReported(removeExpense);
			expense.setReports(null);
			return expenseRepository.save(expense);
		}
		if(expense.getIsHidden()==true) {
			throw new IllegalStateException("Expense " + expenseId + " does not exist!");
		}
		return expenseRepository.save(expense);
	}

	@Override
	public List<Expense> getExpenseByReportId(Long reportId) {
		Reports report = reportsRepository.findById(reportId).get();
		return expenseRepository.findByReportsAndIsHidden(report,false);
	}

	@Override
	public void hideExpense(Long expId) {
		Boolean hidden = true;
		Expense expense = getExpenseById(expId);
		if (expense.getIsReported() != true) {
			expense.setIsHidden(hidden);
		}
		if (expense.getIsReported() == true) {
			throw new IllegalStateException(
					"Cannot delete expense " + expId + " as it is reported in Report: " + expense.getReportTitle());
		}
		expenseRepository.save(expense);
	}

}