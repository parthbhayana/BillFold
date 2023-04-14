package com.nineleaps.expensemanagementproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	List<Expense> findByReports(Reports reports);

}