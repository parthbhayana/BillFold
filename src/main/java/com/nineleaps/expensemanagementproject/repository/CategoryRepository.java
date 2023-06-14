package com.nineleaps.expensemanagementproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	

}
