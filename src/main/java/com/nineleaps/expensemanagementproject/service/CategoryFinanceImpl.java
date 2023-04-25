package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.CategoryFinance;
import com.nineleaps.expensemanagementproject.repository.CategoryFinanceRepository;

@Service
public class CategoryFinanceImpl implements ICategoryFinance {

	@Autowired
	CategoryFinanceRepository catfinrepository;

	@Override
	public void deleteCategoryFinanceById(Long catId) {
		catfinrepository.deleteById(catId);

	}

	@Override
	public CategoryFinance updateCategoryFinance(CategoryFinance categoryfinance) {

		return catfinrepository.save(categoryfinance);
	}

	@Override
	public CategoryFinance getCategoryFinanceById(Long catId) {

		return catfinrepository.findById(catId).get();
	}

	@Override
	public List<CategoryFinance> getAllCategoryFinances() {

		return catfinrepository.findAll();
	}

	@Override
	public CategoryFinance addCategoryFinances(CategoryFinance categoryfinance) {
		return catfinrepository.save(categoryfinance);

	}

}
