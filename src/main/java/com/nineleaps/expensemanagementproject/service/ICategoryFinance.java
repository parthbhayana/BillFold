package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.entity.CategoryFinance;
import com.nineleaps.expensemanagementproject.entity.Expense;

public interface ICategoryFinance {

	void deleteCategoryFinanceById(Long catId);

	CategoryFinance getCategoryFinanceById(Long catId);

	List<CategoryFinance> getAllCategoryFinances();

	CategoryFinance addCategoryFinances(CategoryFinance categoryfinance);

	CategoryFinance updateCategoryFinance(CategoryFinance categoryfinance);

}
