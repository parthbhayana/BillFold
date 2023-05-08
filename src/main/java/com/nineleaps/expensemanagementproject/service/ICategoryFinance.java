package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.entity.CategoryFinance;

public interface ICategoryFinance {

	void deleteCategoryFinanceById(Long catId);

	public CategoryFinance getCategoryFinanceById(Long catId);

	public List<CategoryFinance> getAllCategoryFinances();

	public CategoryFinance addCategoryFinances(CategoryFinance categoryfinance);

	public CategoryFinance updateCategoryFinance(CategoryFinance categoryfinance);

	public void hideCategory(Long categoryId);
}