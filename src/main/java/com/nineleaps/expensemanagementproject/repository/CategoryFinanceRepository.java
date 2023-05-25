package com.nineleaps.expensemanagementproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Category;

public interface CategoryFinanceRepository extends JpaRepository<Category, Long> {
	

}
