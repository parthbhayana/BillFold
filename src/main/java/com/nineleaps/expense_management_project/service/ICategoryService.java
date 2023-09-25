package com.nineleaps.expense_management_project.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nineleaps.expense_management_project.dto.CategoryDTO;
import com.nineleaps.expense_management_project.entity.Category;

public interface ICategoryService {
	void deleteCategoryById(Long categoryId);

	Category getCategoryById(Long categoryId);
	List<Category> getAllCategories();
	Category addCategory(CategoryDTO category);

	void hideCategory(Long categoryId);
	HashMap<String, Double> getCategoryTotalAmount(LocalDate startDate, LocalDate endDate);
	Map<String, Double> getCategoryTotalAmountByYearAndCategory(Long categoryId);
	HashMap<String, Double> getCategoryTotalAmountByMonthAndCategory(Long categoryId, Long year);
	HashMap<String, Double> getYearlyReimbursementRatio(Long categoryId);
	HashMap<String, Double> getMonthlyReimbursementRatio(Long categoryId, Long year);
	HashMap<String, Object> getCategoryAnalyticsYearly(Long categoryId);
	HashMap<String, Object> getCategoryAnalyticsMonthly(Long categoryId, Long year);

	HashMap<String, Double> getTotalAmountByYearForAllCategories();

	HashMap<String, Double> getTotalAmountByMonthForAllCategories(Long year);

	HashMap<String, Double> getYearlyReimbursementRatioForAllCategories();

	HashMap<String, Double> getMonthlyReimbursementRatioForAllCategories(Long year);

	HashMap<String, Object> getYearlyCategoryAnalyticsForAllCategories();

	HashMap<String,Object> getMonthlyCategoryAnalyticsForAllCategories(Long year);

	Category updateCategory(Long categoryId, CategoryDTO categoryDTO);
}