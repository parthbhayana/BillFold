package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
import com.nineleaps.expensemanagementproject.entity.Expense;

public interface IExpenseService {

    List<Expense> getAllExpenses();

     Expense getExpenseById(Long expenseId);

     Expense updateSupportingDocument(String supportingDoc, Long expenseId);

     Expense updateExpense(Long reportId, Long employeeId);

     void deleteExpenseById(Long expenseId);

     List<Expense> getExpenseByEmployeeId(Long employeeId);

     List<Expense> getExpenseByReportId(Long employeeId);

     Expense updateExpenses(ExpenseDTO expense, Long expenseId);

     String addExpense(ExpenseDTO expense, Long employeeid, Long catId) throws IllegalAccessException;

     Expense addPotentialDuplicateExpense(ExpenseDTO expense, Long employeeid, Long catId);

     Expense removeTaggedExpense(Long expenseId);


     List<Expense> getExpensesByEmployeeId(Long employeeId);

     void hideExpense(Long expId);

    List<Expense> getRejectedExpensesByReportId(Long reportId);
}