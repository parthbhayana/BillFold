package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ExpenseRepository expenseRepository;

	@Override
	public void deleteCategoryById(Long categoryId) {
		categoryRepository.deleteById(categoryId);
	}

	@Override
	public Category updateCategory(Category category)
	{
		return categoryRepository.save(category);
	}

	@Override
	public Category getCategoryById(Long categoryId) {
		return categoryRepository.findById(categoryId).get();
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> category = categoryRepository.findAll();
		List<Category> nondeletedcategories = new ArrayList<>();
		for (Category cat2 : category) {
			if (cat2.getIsHidden() != true) {
				nondeletedcategories.add(cat2);
			}
		}
		return nondeletedcategories;
	}

	@Override
	public Category addCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public void hideCategory(Long categoryId) {
		Boolean hidden = true;
		Category category = getCategoryById(categoryId);
		category.setIsHidden(hidden);
		categoryRepository.save(category);
	}

	@Override
	public HashMap<String, Float> getCategoryTotalAmount(LocalDate startDate, LocalDate endDate) {
		List<Expense> expenseList = expenseRepository.findByDateBetweenAndIsReported(startDate, endDate, true);
		HashMap<String, Float> categoryAmountMap = new HashMap<>();

		for (Expense expense : expenseList) {
			Category category = expense.getCategory();
			String categoryName = category.getCategoryDescription();
			Float amt = expense.getAmountINR();
			if (categoryAmountMap.containsKey(categoryName)) {
				Float previousAmt = categoryAmountMap.get(categoryName);
				categoryAmountMap.put(categoryName, previousAmt + amt);
			} else {
				categoryAmountMap.put(categoryName, amt);
			}
		}
		return categoryAmountMap;
	}
}