package com.nineleaps.expensemanagementproject.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nineleaps.expensemanagementproject.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByReports(Reports reports);
	
	List<Expense> findByReportsAndIsHidden(Reports reports, Boolean b);

	List<Expense> findByReportIdAndManagerApprovalStatusAndIsReportedAndIsHidden(Long reportId, ManagerApprovalStatusExpense managerStatus, Boolean a, Boolean b);

	Optional<Expense> findReportByEmployee(Long fkEmpId);

	List<Expense> findByEmployee(Employee employee);
	
	List<Expense> findByEmployeeAndIsHidden(Employee employee, Boolean b);

	List<Expense> findByEmployeeAndIsReported(Employee employee, boolean b);

	Expense getExpenseByexpenseId(Long expenseID);

	List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
	
	List<Expense> findByDateBetweenAndIsReported(LocalDate startDate, LocalDate endDate, Boolean bool);

    List<Expense> findByIsReportedAndDateBefore(boolean b, LocalDate sixtyDaysAgo);
}