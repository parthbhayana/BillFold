package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.CategoryFinance;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.CategoryFinanceRepository;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements IExpenseService {
	@Autowired
	private ExpenseRepository expRepository;

	@Autowired
	private EmployeeRepository empRepository;
	@Autowired
	private IEmployeeService employeeSERVICES;
	
	@Autowired
	private CategoryFinanceRepository catrepository;

	@Autowired
	private IReportsService reportServices;
	
	@Autowired
	private EntityManager entityManager;
	
    @Transactional
	@Override
	public Expense addExpense(Expense expense, Long employeeid,Long catId) {

    	 Employee empDetails = employeeSERVICES.getEmployeeDetailsById(employeeid);
    	    CategoryFinance catfin = catrepository.findById(catId).get();
    	    expense.setEmployee(empDetails);
    	    expense.setCategoryfinance(catfin);
    	    CategoryFinance mergedCategoryFinance = entityManager.merge(catfin);
    	    expense.setCategoryfinance(mergedCategoryFinance);
    	    expense.setCatDescription(mergedCategoryFinance.getCatDescription()); // set the category description
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
	public Expense updateExpense(Long reportId, Long employeeId) {

		Expense exp = getExpenseById(employeeId);
		Reports report = reportServices.getReportById(reportId);
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
	public List<Expense> getExpenseByEmployeeId(Long employeeId) {
		Employee employee = empRepository.findById(employeeId).get();
		return expRepository.findByEmployee(employee);
	}

	@Override
	public Expense updateSupportingDocument(String supportingDoc, Long expenseId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expense updateExpenses(Expense expense) {
		return expRepository.save(expense);
	}

	

}