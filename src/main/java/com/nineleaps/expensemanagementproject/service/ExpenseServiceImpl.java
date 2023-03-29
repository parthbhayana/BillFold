package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements IExpenseService {
	@Autowired
	private ExpenseRepository expRepository;

	@Override
	public Expense saveCustomer(Expense expense) {
		
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
	
	
	@Override
	public Expense updateExpense(Expense expense) {
		
		return expRepository.save(expense);
	}

	@Override
	public void deleteExpenseById(Long expenseId) {
		expRepository.deleteById(expenseId);
		
	}

}