package com.nineleaps.expensemanagementproject.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.service.IExpenseService;

@RestController
public class ExpenseController {
	@Autowired
	private IExpenseService expService;

	@PostMapping("/insert_expenses/{employee_id}")
	public Expense saveExpense(@RequestBody Expense expense, @PathVariable Long empid, @RequestParam Long catid) {
		return expService.addExpense(expense, empid, catid);
	}

	@GetMapping("/show_all_expenses")
	public List<Expense> getAllExpenses() {
		return expService.getAllExpenses();
	}

	@GetMapping("/find_expense/{expense_id}")
	public Expense getExpenseById(@PathVariable Long expenseId) {
		return expService.getExpenseById(expenseId);
	}

	@PutMapping("/update_expenses/{expense_id}")
	public Expense updateExpenses(@RequestBody Expense newExpense, @PathVariable Long expenseId) {
		return expService.updateExpenses(newExpense, expenseId);
	}

	@GetMapping("/get_expense_by_employee_id/{employee_id}")
	public List<Expense> getExpenseByEmpId(@PathVariable Long employeeId) {
		return expService.getExpenseByEmployeeId(employeeId);
	}

	@GetMapping("/get_expense_by_report_id/{report_id}")
	public List<Expense> getExpenseByReportId(@PathVariable Long reportId) {
		return expService.getExpenseByReportId(reportId);
	}

	@PostMapping("/remove_tagged_expense/{expense_id}")
	public Expense removeTaggedExpense(@PathVariable Long expenseId) {
		return expService.removeTaggedExpense(expenseId);
	}

	@GetMapping("/get_unreported_expenses_by_emp_id/{employee_id}")
	public List<Expense> getExpensesById(@PathVariable Long employeeId) {
		return expService.getExpensesByEmployeeId(employeeId);
	}

	@PostMapping("/hide_expense/{expense_id}")
	public void hideExpense(@PathVariable Long expId) {
		expService.hideExpense(expId);
	}

}