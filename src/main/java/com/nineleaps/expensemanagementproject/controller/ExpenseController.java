package com.nineleaps.expensemanagementproject.controller;

import java.util.ArrayList;
import java.util.List;

import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
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

	@PostMapping("/insertExpenses/{employeeId}")
	public String saveExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long employeeId,
			@RequestParam Long categoryId) throws IllegalAccessException {
		return expenseService.addExpense(expenseDTO, employeeId, categoryId);
	}

	@PostMapping("/addPotentialDuplicateExpense/{employeeId}")
	public Expense addPotentialDuplicateExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long employeeId,
			@RequestParam Long categoryId) {
		return expenseService.addPotentialDuplicateExpense(expenseDTO, employeeId, categoryId);
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
	public Expense updateExpenses(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long expenseId) {
		return expenseService.updateExpenses(expenseDTO, expenseId);
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

	@GetMapping("/expenseCount/{reportId}")
	public Long expenseCount(@RequestParam Long reportId){
		List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
		List<Long> expenseIds = new ArrayList<>();
		for (Expense exp : expenseList) {
			expenseIds.add(exp.getExpenseId());
		}
		return (long) expenseIds.size();
	}

}