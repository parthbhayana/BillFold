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

	@PostMapping("/insert_category")
	public Category saveExpense(@RequestBody Category categoryfinance) {
		return categoryService.addCategoryFinances(categoryfinance);
	}

	@GetMapping("/show_all_categories")
	public List<Category> getAllCategoryFinances() {
		return categoryService.getAllCategoryFinances();
	}

	@GetMapping("/find_category/{category_id}")
	public Category getCategoryFinanceById(@PathVariable("category_id") Long category_id) {
		return categoryService.getCategoryFinanceById(category_id);
	}

	@PutMapping("/update_category/{category_id}")
	public Category updateCategoryFinance(@PathVariable("category_id") Long category_id, @RequestBody Category categoryfinance) {
		Category catfin = categoryService.getCategoryFinanceById(category_id);
		catfin.setCategoryDescription(categoryfinance.getCategoryDescription());
		return categoryService.updateCategoryFinance(catfin);
	}

	@PostMapping("/hide_category/{category_id}")
	public void hideCategory(@PathVariable Long categoryId) {
		categoryService.hideCategory(categoryId);
	}

	@GetMapping("/category_total_amount")
	public HashMap<String, Float> getCategoryTotalAmount(
			@RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date,
			@RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end_date)
	{
		return categoryService.getCategoryTotalAmount(start_date, end_date);
	}
}
