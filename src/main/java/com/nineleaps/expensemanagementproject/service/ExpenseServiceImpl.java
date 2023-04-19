 package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;


@Service
public class ExpenseServiceImpl implements IExpenseService {
	@Autowired
	private ExpenseRepository expRepository;


	@Autowired
	private IEmployeeService employeeSERVICES;


	@Override
	public Expense addExpense(Expense expense, Long employeeid) {

		Employee empDetails = employeeSERVICES.getEmployeeDetailsById(employeeid);
		expense.setEmployee(empDetails);
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
	public Expense updateExpense(Reports report, Long employeeId) {

		Expense exp = getExpenseById(employeeId);
		if (exp != null) {
			exp.setReports(report);
		}
		expRepository.save(exp);
		return null;
	}
	
//	@Override
//	public Expense updateSupportingDocument( String supportingDoc, Long expenseId) {
//
//		Expense exp = getExpenseById(expenseId);
//		if (exp != null) {
//			exp.setSupportingDocument(supportingDoc);
//		}
//		expRepository.save(exp);
//		return null;
//	}

	@Override
	public void deleteExpenseById(Long expenseId) {
		expRepository.deleteById(expenseId);

	}

	@Override
	public Expense getExpenseByEmployeeId(Long fk_empid) {

		return expRepository.findById(fk_empid).get();
	}

	@Override
	public Expense updateSupportingDocument(String supportingDoc, Long expenseId) {
		// TODO Auto-generated method stub
		return null;
	}

}