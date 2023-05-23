package com.nineleaps.expensemanagementproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByReports(Reports reports);
	Optional<Expense> findReportByEmployee(Long fkEmpId);
	
	List<Expense> findByEmployee(Employee employee);
	List<Expense> findByEmployeeAndIsReported(Employee employee, boolean b);
//	List<Expense> findByManagerEmail(String managerEmail);
}