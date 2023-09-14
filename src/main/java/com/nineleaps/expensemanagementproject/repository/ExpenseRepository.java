package com.nineleaps.expensemanagementproject.repository;

import java.time.LocalDate;
import java.util.List;

import com.nineleaps.expensemanagementproject.entity.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nineleaps.expensemanagementproject.entity.Category;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByReports(Reports reports);

	List<Expense> findByReportsAndIsHidden(Reports reports, Boolean b);

	List<Expense> findExpenseByReportsAndIsReportedAndIsHidden(Reports report, Boolean a, Boolean b);

	List<Expense> findByEmployeeAndIsHidden(Employee employee, boolean isHidden, Sort sort);


	List<Expense> findByEmployeeAndIsReported(Employee employee, boolean b);

	Expense getExpenseByexpenseId(Long expenseID);

	List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

	List<Expense> findByDateBetweenAndIsReported(LocalDate startDate, LocalDate endDate, Boolean bool);

	List<Expense> findByIsReportedAndDateBefore(boolean b, LocalDate sixtyDaysAgo);

	List<Expense> findByCategoryAndIsReported(Category category, boolean b);

	List<Expense> findByCategory(Category category);

	List<Expense> findByIsReportedAndIsHidden(boolean b, boolean b1);

	List<Expense> findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(Employee employee, Double amount,
																					  LocalDate date, Category category,
																					  String merchantName, boolean a);

}