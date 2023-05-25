package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.repository.CategoryFinanceRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;

@Service
public class CategoryServiceImpl implements ICategory {

	@Autowired
	CategoryFinanceRepository catfinrepository;

	@Autowired
	ExpenseRepository expRepo;

	@Override
	public void deleteCategoryFinanceById(Long catId) {
		catfinrepository.deleteById(catId);
	}

	@Override
	public Category updateCategoryFinance(Category categoryfinance) {
		return catfinrepository.save(categoryfinance);
	}

	@Override
	public Category getCategoryFinanceById(Long catId) {
		return catfinrepository.findById(catId).get();
	}

	@Override
	public List<Category> getAllCategoryFinances() {
		List<Category> category = catfinrepository.findAll();
		List<Category> nondeletedcategories = new ArrayList<>();
		for (Category cat2 : category) {
			if (cat2.getIsHidden() != true) {
				nondeletedcategories.add(cat2);
			}
		}
		return nondeletedcategories;
	}

	@Override
	public Category addCategoryFinances(Category categoryfinance) {
		return catfinrepository.save(categoryfinance);
	}

	@Override
	public void hideCategory(Long categoryId) {
		Boolean hidden = true;
		Category category = getCategoryFinanceById(categoryId);
		category.setIsHidden(hidden);
		catfinrepository.save(category);
	}

	@Override
	public HashMap<String,Float> getCategoryTotalAmount(LocalDate startDate, LocalDate endDate) {
//		List<Category> categoryList = catfinrepository.findAll();
		List<Expense> expenseList = expRepo.findByDateBetween(startDate, endDate);
		HashMap<String, Float> catAmount = new HashMap<>();
		for (Expense expense : expenseList) {
			Category category = expense.getCategoryfinance();
			String categoryName = category.getCatDescription();
			Float amt = expense.getAmountINR();
			catAmount.put(categoryName, amt);
		}
		return catAmount;
	}

}