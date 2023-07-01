package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Category;

public interface ICategoryService {

	void deleteCategoryById(Long categoryId);

	public Category getCategoryById(Long categoryId);

	public List<Category> getAllCategories();

	public Category addCategory(Category category);

	public Category updateCategory(Category category);

	public void hideCategory(Long categoryId);

	public HashMap<String, Float> getCategoryTotalAmount(LocalDate startDate, LocalDate endDate);

}