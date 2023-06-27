package com.nineleaps.expensemanagementproject.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByReports(Reports reports);
	
	List<Expense> findByReportsAndIsHidden(Reports reports, Boolean b);

	Optional<Expense> findReportByEmployee(Long fkEmpId);

	List<Expense> findByEmployee(Employee employee);
	
	List<Expense> findByEmployeeAndIsHidden(Employee employee, Boolean b);

	List<Expense> findByEmployeeAndIsReported(Employee employee, boolean b);

	Expense getExpenseByexpenseId(Long expenseID);

	List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
	
	List<Expense> findByDateBetweenAndIsReported(LocalDate startDate, LocalDate endDate, Boolean bool);

    List<Expense> findByIsReportedAndDateBefore(boolean b, LocalDate sixtyDaysAgo);
}