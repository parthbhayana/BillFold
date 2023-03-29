package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Expense;

public interface IExpenseService {

	public Expense saveCustomer(Expense expense);

	public List<Expense> getAllExpenses();

	public Expense getExpenseById(Long expenseId);

	public Expense updateExpense(Expense expense);

	 void deleteExpenseById(Long expenseId);
}

	
