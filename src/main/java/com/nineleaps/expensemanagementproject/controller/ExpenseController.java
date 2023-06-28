package com.nineleaps.expensemanagementproject.controller;

import java.util.List;

import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
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
	private IExpenseService expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

	@PostMapping("/insertExpenses/{employeeId}")
	public Expense saveExpense(@RequestBody Expense expense, @PathVariable Long employeeId, @RequestParam Long categoryId) {
		return expenseService.addExpense(expense, employeeId, categoryId);
	}

	@GetMapping("/showAllExpenses")
	public List<Expense> getAllExpenses() {
		return expenseService.getAllExpenses();
	}

	@GetMapping("/findExpense/{expenseId}")
	public Expense getExpenseById(@PathVariable Long expenseId) {
		return expenseService.getExpenseById(expenseId);
	}

	@PutMapping("/updateExpenses/{expenseId}")
	public Expense updateExpenses(@RequestBody Expense newExpense, @PathVariable Long expenseId) {
		return expenseService.updateExpenses(newExpense, expenseId);
	}

	@GetMapping("/getExpenseByEmployeeId/{employeeId}")
	public List<Expense> getExpenseByEmpId(@PathVariable Long employeeId) {
		return expenseService.getExpenseByEmployeeId(employeeId);
	}

	@GetMapping("/getExpenseByReportId/{reportId}")
	public List<Expense> getExpenseByReportId(@PathVariable Long reportId) {
		return expenseService.getExpenseByReportId(reportId);
	}

	@PostMapping("/removeTaggedExpense/{expenseId}")
	public Expense removeTaggedExpense(@PathVariable Long expenseId) {
		return expenseService.removeTaggedExpense(expenseId);
	}

	@GetMapping("/getUnreportedExpensesByEmployeeId/{employeeId}")
	public List<Expense> getExpensesById(@PathVariable Long employeeId) {
		return expenseService.getExpensesByEmployeeId(employeeId);
	}

	@PostMapping("/hideExpense/{expenseId}")
	public void hideExpense(@PathVariable Long expenseId) {
		expenseService.hideExpense(expenseId);
	}

	@PostMapping("/setPartialApprovedAmount")
	public void setPartialApprovedAmount(@RequestParam Long expenseId, @RequestParam float approvedAmount){
		Expense expense = expenseService.getExpenseById(expenseId);
		expense.setAmountApproved(approvedAmount);
		expenseRepository.save(expense);
	}
}