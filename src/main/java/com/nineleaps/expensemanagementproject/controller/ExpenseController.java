package com.nineleaps.expensemanagementproject.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.service.IExpenseService;

@RestController
public class ExpenseController {
	@Autowired
	private IExpenseService expService;

	@PostMapping("/insertexpenses/{empid}")
	public Expense saveExpense(@RequestBody Expense expense, @PathVariable Long empid, @RequestParam Long catid) {
	@PostMapping("/insertexpenses/{empid}")
	public Expense saveExpense(@RequestBody Expense expense, @PathVariable Long empid, @RequestParam Long catid) {
		return expService.addExpense(expense, empid, catid);
	}

	@GetMapping("/showallexpenses")
	public List<Expense> getAllExpenses() {
		return expService.getAllExpenses();
	}

	@GetMapping("/findexpense/{expId}")
	public Expense getExpenseById(@PathVariable("expId") Long expenseId) {
		return expService.getExpenseById(expenseId);
	}

	@PutMapping("/updateexpenses/{expId}")
	public Expense updateExpenses(@RequestBody Expense newExpense, @PathVariable("expId") Long expenseId) {
		return expService.updateExpenses(newExpense, expenseId);
		return expService.updateExpenses(newExpense, expenseId);
	}

//	@DeleteMapping("/deleteexpense/{expId}")
//	public void deleteCustomerById(@PathVariable("expId") Long expenseId) {
//		expService.deleteExpenseById(expenseId);
//	}
//	@DeleteMapping("/deleteexpense/{expId}")
//	public void deleteCustomerById(@PathVariable("expId") Long expenseId) {
//		expService.deleteExpenseById(expenseId);
//	}

	@GetMapping("/getexpensebyemployeeid/{empid}")
	@GetMapping("/getexpensebyemployeeid/{empid}")
	public List<Expense> getExpenseByEmpId(@PathVariable("empid") Long employeeId) {
		return expService.getExpenseByEmployeeId(employeeId);
	}

	@GetMapping("/getexpensebyreportid/{reportid}")
	public List<Expense> getExpenseByReportId(@PathVariable("reportid") Long reportId) {
		return expService.getExpenseByReportId(reportId);
	}

	@PostMapping("/removetaggedexpense/{expId}")
	public Expense removeTaggedExpense(@PathVariable("expId") Long expenseId) {
		return expService.removeTaggedExpense(expenseId);
	}

	@GetMapping("/getunreportedexpensesbyempid/{empid}")
	public List<Expense> getExpensesById(@PathVariable("empid") Long employeeId) {
		return expService.getExpensesByEmployeeId(employeeId);
	}

	@PostMapping("/hideexpense/{expId}")
	public void hideExpense(@PathVariable Long expId) {
		expService.hideExpense(expId);
	}


	@GetMapping("/getexpensebyreportid/{reportid}")
	public List<Expense> getExpenseByReportId(@PathVariable("reportid") Long reportId) {
		return expService.getExpenseByReportId(reportId);
	}

	@PostMapping("/removetaggedexpense/{expId}")
	public Expense removeTaggedExpense(@PathVariable("expId") Long expenseId) {
		return expService.removeTaggedExpense(expenseId);
	}

	@GetMapping("/getunreportedexpensesbyempid/{empid}")
	public List<Expense> getExpensesById(@PathVariable("empid") Long employeeId) {
		return expService.getExpensesByEmployeeId(employeeId);
	}

	@PostMapping("/hideexpense/{expId}")
	public void hideExpense(@PathVariable Long expId) {
		expService.hideExpense(expId);
	}

}
