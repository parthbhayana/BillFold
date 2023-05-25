package com.nineleaps.expensemanagementproject.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.web.bind.annotation.DeleteMapping;
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

	@PostMapping("/insertcategory")
	public Category saveExpense(@RequestBody Category categoryfinance) {
		return categoryService.addCategoryFinances(categoryfinance);
	}

	@GetMapping("/showallcategories")
	public List<Category> getAllCategoryFinances() {
		return categoryService.getAllCategoryFinances();

	}

	@GetMapping("/findcategory/{catid}")
	public Category getCategoryFinanceById(@PathVariable("catid") Long catId) {
		return categoryService.getCategoryFinanceById(catId);
	}

	@PutMapping("/updatecategory/{catid}")
	public Category updateCategoryFinance(@PathVariable("catid") Long catId, @RequestBody Category categoryfinance) {
		Category catfin = categoryService.getCategoryFinanceById(catId);
		catfin.setCatDescription(categoryfinance.getCatDescription());

		return categoryService.updateCategoryFinance(catfin);
	}

	@PostMapping("/hidecategory/{categoryId}")
	public void hideCategory(@PathVariable Long categoryId) {
		categoryService.hideCategory(categoryId);
	}

	@GetMapping("/category-total-amount")
	public HashMap<String, Float> getCategoryTotalAmount(
			@RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

		return categoryService.getCategoryTotalAmount(startDate, endDate);
	}

}