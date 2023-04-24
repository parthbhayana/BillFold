package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;

public interface IExpenseService {

	public Expense addExpense(Expense expense, Long employeeid);

	public List<Expense> getAllExpenses();

	public Expense getExpenseById(Long expenseId);

	public Expense updateSupportingDocument(String supportingDoc, Long expenseId);

	public Expense updateExpense(Long reportId, Long employeeId);

	void deleteExpenseById(Long expenseId);

	public List<Expense> getExpenseByEmployeeId(Long employeeId);

	public Expense updateExpenses(Expense expense);
	
//	public Expense fetchEmpId(Long expenseId); 

	// Expense updateSupportingDocument(byte[] supportingDoc, Long expenseId);
}
