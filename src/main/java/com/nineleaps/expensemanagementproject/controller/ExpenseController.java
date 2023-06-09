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
	public Expense saveExpense(@RequestBody Expense expense, @PathVariable Long employee_id, @RequestParam Long category_id) {
		return expService.addExpense(expense, employee_id, category_id);
	}

	@GetMapping("/show_all_expenses")
	public List<Expense> getAllExpenses() {
		return expService.getAllExpenses();
	}

	@GetMapping("/find_expense/{expense_id}")
	public Expense getExpenseById(@PathVariable Long expense_id) {
		return expService.getExpenseById(expense_id);
	}

	@PutMapping("/update_expenses/{expense_id}")
	public Expense updateExpenses(@RequestBody Expense newExpense, @PathVariable Long expense_id) {
		return expService.updateExpenses(newExpense, expense_id);
	}

	@GetMapping("/get_expense_by_employee_id/{employee_id}")
	public List<Expense> getExpenseByEmpId(@PathVariable Long employee_id) {
		return expService.getExpenseByEmployeeId(employee_id);
	}

	@GetMapping("/get_expense_by_report_id/{report_id}")
	public List<Expense> getExpenseByReportId(@PathVariable Long report_id) {
		return expService.getExpenseByReportId(report_id);
	}

	@PostMapping("/remove_tagged_expense/{expense_id}")
	public Expense removeTaggedExpense(@PathVariable Long expense_id) {
		return expService.removeTaggedExpense(expense_id);
	}

	@GetMapping("/get_unreported_expenses_by_emp_id/{employee_id}")
	public List<Expense> getExpensesById(@PathVariable Long employee_id) {
		return expService.getExpensesByEmployeeId(employee_id);
	}

	@PostMapping("/hide_expense/{expense_id}")
	public void hideExpense(@PathVariable Long expense_id) {
		expService.hideExpense(expense_id);
	}

}