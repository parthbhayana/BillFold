package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Category;

public interface ICategory {

	void deleteCategoryFinanceById(Long catId);

	public Category getCategoryFinanceById(Long catId);

	public List<Category> getAllCategoryFinances();

	public Category addCategoryFinances(Category categoryfinance);

	public Category updateCategoryFinance(Category categoryfinance);

	public void hideCategory(Long categoryId);
	
	public HashMap<String,Float> getCategoryTotalAmount(LocalDate startDate, LocalDate endDate);
	
}