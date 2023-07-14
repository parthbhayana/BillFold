package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.nineleaps.expensemanagementproject.DTO.CategoryDTO;
import com.nineleaps.expensemanagementproject.entity.Category;

public interface ICategoryService {
	void deleteCategoryById(Long categoryId);

	Category getCategoryById(Long categoryId);
	List<Category> getAllCategories();
	Category addCategory(CategoryDTO category);

	void hideCategory(Long categoryId);
	HashMap<String, Float> getCategoryTotalAmount(LocalDate startDate, LocalDate endDate);
	HashMap<String, Float> getCategoryTotalAmountByYearAndCategory(Long categoryId);
	HashMap<String, Float> getCategoryTotalAmountByMonthAndCategory(Long categoryId, Long year);
    HashMap<String, Float> getYearlyReimbursementRatio(Long categoryId);
	HashMap<String, Float> getMonthlyReimbursementRatio(Long categoryId, Long year);
	HashMap<String, Object> getCategoryAnalyticsYearly(Long categoryId);
	HashMap<String, Object> getCategoryAnalyticsMonthly(Long categoryId, Long year);

	HashMap<String, Float> getTotalAmountByYearForAllCategories();

	HashMap<String, Float> getTotalAmountByMonthForAllCategories(Long year);

	HashMap<String, Float> getYearlyReimbursementRatioForAllCategories();

	HashMap<String, Float> getMonthlyReimbursementRatioForAllCategories(Long year);

	HashMap<String, Object> getYearlyCategoryAnalyticsForAllCategories();

	HashMap<String,Object> getMonthlyCategoryAnalyticsForAllCategories(Long year);

	Category updateCategory(Long categoryId, CategoryDTO categoryDTO);
}