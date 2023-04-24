//package com.nineleaps.expensemanagementproject.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import com.nineleaps.expensemanagementproject.entity.CategoryFinance;
//import com.nineleaps.expensemanagementproject.entity.Expense;
//import com.nineleaps.expensemanagementproject.service.ICategoryFinance;
//
//@RestController
//public class CategoryFinanceController {
//	@Autowired
//	private ICategoryFinance catFinService;
//
//	@PostMapping("/insertcategory")
//	public CategoryFinance saveExpense(@RequestBody CategoryFinance categoryfinance) {
//		return catFinService.addCategoryFinances(categoryfinance);
//	}
//
//	@GetMapping("/showallcategories")
//	public List<CategoryFinance> getAllCategoryFinances() {
//		return catFinService.getAllCategoryFinances();
//
//	}
//
//	@GetMapping("/findexpense/{expid}")
//	public CategoryFinance getCategoryFinanceById(@PathVariable("catId") Long catId) {
//		return catFinService.getCategoryFinanceById(catId);
//	}
//
//	@PutMapping("/updatecategory/{catid}")
//	public CategoryFinance updateCategoryFinance(@RequestBody CategoryFinance categoryfinance,
//			@PathVariable("catId") Long catId) {
//
//		return catFinService.updateCategoryFinance(categoryfinance, catId);
//	}
//
//	@DeleteMapping("/deletecategory/{catid}")
//	public void deleteCategoryFinanceById(@PathVariable("catId") Long catId) {
//		catFinService.deleteCategoryFinanceById(catId);
//
//	}
//}