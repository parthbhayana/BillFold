package com.nineleaps.expense_management_project.service;

import java.util.List;

import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import com.nineleaps.expense_management_project.entity.Expense;

public interface IExpenseService {

    public List<Expense> getAllExpenses();

    public Expense getExpenseById(Long expenseId);


    public Expense updateExpense(Long reportId, Long employeeId);

    public void deleteExpenseById(Long expenseId);

    public List<Expense> getExpenseByEmployeeId(Long employeeId);

    public List<Expense> getExpenseByReportId(Long employeeId);

    public Expense updateExpenses(ExpenseDTO expense, Long expenseId);

    public String addExpense(ExpenseDTO expense, Long employeeid, Long catId) throws IllegalAccessException;

    public Expense addPotentialDuplicateExpense(ExpenseDTO expense, Long employeeid, Long catId);

    public Expense removeTaggedExpense(Long expenseId);


    public List<Expense> getExpensesByEmployeeId(Long employeeId);

    public void hideExpense(Long expId);

    List<Expense> getRejectedExpensesByReportId(Long reportId);
}