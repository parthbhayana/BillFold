package com.nineleaps.expensemanagementproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense,Long>{

}