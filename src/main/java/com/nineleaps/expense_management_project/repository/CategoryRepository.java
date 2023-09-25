package com.nineleaps.expense_management_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expense_management_project.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category getCategoryByCategoryId(Long categoryId);

	boolean existsByCategoryDescription(String categoryDescription);

}