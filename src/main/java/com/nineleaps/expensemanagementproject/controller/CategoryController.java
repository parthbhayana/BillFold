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
import com.nineleaps.expensemanagementproject.service.ICategoryService;

@RestController
public class CategoryController {
	@Autowired
	private ICategoryService categoryService;

	@PostMapping("/insertCategory")
	public Category addCategory(@RequestBody Category category) {
		return categoryService.addCategory(category);
	}

	@GetMapping("/showAllCategories")
	public List<Category> getAllCategory() {
		return categoryService.getAllCategories();
	}

	@GetMapping("/findCategory/{categoryId}")
	public Category getCategoryById(@PathVariable("categoryId") Long categoryId) {
		return categoryService.getCategoryById(categoryId);
	}

	@PutMapping("/updateCategory/{categoryId}")
	public Category updateCategory(@PathVariable Long categoryId, @RequestBody Category newCategory) {
		Category category = categoryService.getCategoryById(categoryId);
		category.setCategoryDescription(newCategory.getCategoryDescription());
		return categoryService.updateCategory(category);
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
