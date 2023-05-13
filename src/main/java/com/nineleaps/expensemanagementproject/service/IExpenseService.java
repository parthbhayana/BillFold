package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Expense;

public interface IExpenseService {

	//public Expense addExpense(Expense expense, Long employeeid);

	public List<Expense> getAllExpenses();

	public Expense getExpenseById(Long expenseId);

	public Expense updateSupportingDocument(String supportingDoc, Long expenseId);

	public Expense updateExpense(Long reportId, Long employeeId);

	void deleteExpenseById(Long expenseId);

	public List<Expense> getExpenseByEmployeeId(Long employeeId);
	
	public List<Expense> getExpenseByReportId(Long employeeId);

	public Expense updateExpenses(Expense expense, Long expenseId);

	
	
	public Expense removeTaggedExpense(Long expenseId);
	
	public List<Expense> getExpensesByEmployeeId(Long employeeId);
	
	public void hideExpense(Long expId);

	

	

	
	Expense addExpense(Expense expense, Long employeeid, Long catId);
}