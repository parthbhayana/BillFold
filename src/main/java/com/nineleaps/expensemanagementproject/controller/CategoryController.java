package com.nineleaps.expensemanagementproject.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.service.ICategory;

@RestController
public class CategoryController {
	@Autowired
	private ICategory categoryService;

	@PostMapping("/insertCategory")
	public Category saveExpense(@RequestBody Category categoryfinance) {
		return categoryService.addCategoryFinances(categoryfinance);
	}

	@GetMapping("/showAllCategories")
	public List<Category> getAllCategoryFinances() {
		return categoryService.getAllCategoryFinances();
	}

	@GetMapping("/findCategory/{categoryId}")
	public Category getCategoryFinanceById(@PathVariable("categoryId") Long categoryId) {
		return categoryService.getCategoryFinanceById(categoryId);
	}

	@PutMapping("/updateCategory/{categoryId}")
	public Category updateCategoryFinance(@PathVariable Long categoryId, @RequestBody Category newCategory) {
		Category category = categoryService.getCategoryFinanceById(categoryId);
		category.setCategoryDescription(newCategory.getCategoryDescription());
		return categoryService.updateCategoryFinance(category);
	}

	@PostMapping("/hideCategory/{categoryId}")
	public void hideCategory(@PathVariable Long categoryId) {
		categoryService.hideCategory(categoryId);
	}

	@GetMapping("/categoryTotalAmount")
	public HashMap<String, Float> getCategoryTotalAmount(
			@RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate)
	{
		return categoryService.getCategoryTotalAmount(startDate, endDate);
	}
}
