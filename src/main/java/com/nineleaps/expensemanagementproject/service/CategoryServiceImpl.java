package com.nineleaps.expensemanagementproject.service;


import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import com.nineleaps.expensemanagementproject.DTO.CategoryDTO;
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

	private static final String CONSTANT1 = "_ratio";
	private static final String CONSTANT2 = "_count";

  

    @Override
	public void deleteCategoryById(Long categoryId) {
		categoryRepository.deleteById(categoryId);
	}

	@Override
	public Category updateCategory(Long categoryID,CategoryDTO categoryDTO) {
		Category category = getCategoryById(categoryID);
		category.setCategoryDescription(categoryDTO.getCategoryDescription());
		return categoryRepository.save(category);
	}

	@Override
	public Category getCategoryById(Long categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		return optionalCategory.orElse(null);
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> category = categoryRepository.findAll();
		List<Category> nondeletedcategories = new ArrayList<>();
		for (Category cat2 : category) {
			if (!Boolean.TRUE.equals(cat2.getIsHidden())) {
				nondeletedcategories.add(cat2);
			}
		}
		return nondeletedcategories;
	}

	@Override
	public Category addCategory(CategoryDTO categoryDTO) {
		String categoryDescription = categoryDTO.getCategoryDescription();

		if (categoryRepository.existsByCategoryDescription(categoryDescription)) {
			throw new IllegalArgumentException("Category with description '" + categoryDescription + "' already exists.");
		}

		Category category = new Category();
		category.setCategoryDescription(categoryDescription);
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

	@Override
	public HashMap<String, Float> getCategoryTotalAmountByYearAndCategory(Long categoryId) {

			Category category = getCategoryById(categoryId);
			Map<String, Float> categoryAmountMap = new HashMap<>();

			if (category != null) {
				List<Expense> expenseList = expenseRepository.findByCategoryAndIsReported(category, true);

				for (Expense expense : expenseList) {
					LocalDate expenseDate = expense.getDate();
					String year = String.valueOf(expenseDate.getYear());
					Float amount = expense.getAmountINR();

					if (categoryAmountMap.containsKey(year)) {
						Float previousAmount = categoryAmountMap.get(year);
						categoryAmountMap.put(year, previousAmount + amount);
					} else {
						categoryAmountMap.put(year, amount);
					}
				}
			}

			return (HashMap<String, Float>) categoryAmountMap;
		}





	@Override
	public HashMap<String, Float> getCategoryTotalAmountByMonthAndCategory(Long categoryId, Long year) {
		Category category = getCategoryById(categoryId);
		HashMap<String, Float> categoryAmountMap = new HashMap<>();

		if (category != null) {
			List<Expense> expenseList = expenseRepository.findByCategoryAndIsReported(category, true);

			for (Expense expense : expenseList) {
				LocalDate expenseDate = expense.getDate();
				if (expenseDate.getYear() == year) {
					String month = expenseDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
					Float amount = expense.getAmountINR();

					if (categoryAmountMap.containsKey(month)) {
						Float previousAmount = categoryAmountMap.get(month);
						categoryAmountMap.put(month, previousAmount + amount);
					} else {
						categoryAmountMap.put(month, amount);
					}
				}
			}
		}

		return categoryAmountMap;
	}

	@Override
	public HashMap<String, Float> getYearlyReimbursementRatio(Long categoryId) {
		Category category = getCategoryById(categoryId);
		HashMap<String, Float> yearlyReimbursementRatioMap = new HashMap<>();

		if (category != null) {
			List<Expense> expenseList = expenseRepository.findByCategory(category);

			for (Expense expense : expenseList) {
				if (Boolean.TRUE.equals(expense.getIsReported())) {
					String year = String.valueOf(expense.getDate().getYear());

					if (yearlyReimbursementRatioMap.containsKey(year)) {
						float totalReimbursedAmount = yearlyReimbursementRatioMap.get(year) + expense.getAmountINR();
						int totalNumberOfExpenses = (int) (yearlyReimbursementRatioMap.get(year + CONSTANT2) + 1);
						float reimbursementRatio = totalReimbursedAmount / totalNumberOfExpenses;

						yearlyReimbursementRatioMap.put(year, totalReimbursedAmount);
						yearlyReimbursementRatioMap.put(year + CONSTANT2, (float) totalNumberOfExpenses);
						yearlyReimbursementRatioMap.put(year + CONSTANT1, reimbursementRatio);
					} else {
						yearlyReimbursementRatioMap.put(year, expense.getAmountINR());
						yearlyReimbursementRatioMap.put(year + CONSTANT2, 1F);
						yearlyReimbursementRatioMap.put(year + CONSTANT1, expense.getAmountINR());
					}
				}
			}
		}

		return yearlyReimbursementRatioMap;
	}
	@Override
	public HashMap<String, Float> getMonthlyReimbursementRatio(Long categoryId, Long year) {
		Category category = getCategoryById(categoryId);
		HashMap<String, Float> yearlyReimbursementRatioMap = new HashMap<>();

		if (category != null) {
			List<Expense> expenseList = expenseRepository.findByCategory(category);

			for (Expense expense : expenseList) {
				if (Boolean.TRUE.equals(expense.getIsReported()) && expense.getDate().getYear() == year) {
					String month = expense.getDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
					String monthKey = year + "_" + month;

					if (yearlyReimbursementRatioMap.containsKey(monthKey)) {
						float totalReimbursedAmount = yearlyReimbursementRatioMap.get(monthKey) + expense.getAmountINR();
						int totalNumberOfExpenses = (int) (yearlyReimbursementRatioMap.get(monthKey + CONSTANT2) + 1);
						float reimbursementRatio = totalReimbursedAmount / totalNumberOfExpenses;

						yearlyReimbursementRatioMap.put(monthKey, totalReimbursedAmount);
						yearlyReimbursementRatioMap.put(monthKey + CONSTANT2, (float) totalNumberOfExpenses);
						yearlyReimbursementRatioMap.put(monthKey + CONSTANT1, reimbursementRatio);
					} else {
						yearlyReimbursementRatioMap.put(monthKey, expense.getAmountINR());
						yearlyReimbursementRatioMap.put(monthKey + CONSTANT2, 1F);
						yearlyReimbursementRatioMap.put(monthKey + CONSTANT1, expense.getAmountINR());
					}
				}
			}
		}

		return yearlyReimbursementRatioMap;
	}
	@Override
	public HashMap<String, Object> getCategoryAnalyticsYearly(Long categoryId) {
		HashMap<String, Object> analyticsData = new HashMap<>();

		Category category = getCategoryById(categoryId);
		if (category != null) {
			HashMap<String, Float> categoryTotalAmountByYear = getCategoryTotalAmountByYearAndCategory(categoryId);
			HashMap<String, Float> yearlyReimbursementRatio = getYearlyReimbursementRatio(categoryId);

			analyticsData.put("categoryTotalAmountByYear", categoryTotalAmountByYear);
			analyticsData.put("yearlyReimbursementRatio", yearlyReimbursementRatio);
		}

		return analyticsData;
	}

	@Override
	public HashMap<String, Object> getCategoryAnalyticsMonthly(Long categoryId, Long year) {
		HashMap<String, Object> analyticsData = new HashMap<>();

		Category category = getCategoryById(categoryId);
		if (category != null) {
			HashMap<String, Float> categoryTotalAmountByMonth = getCategoryTotalAmountByMonthAndCategory(categoryId, year);
			HashMap<String, Float> monthlyReimbursementRatio = getMonthlyReimbursementRatio(categoryId, year);

			analyticsData.put("categoryTotalAmountByMonth", categoryTotalAmountByMonth);
			analyticsData.put("monthlyReimbursementRatio", monthlyReimbursementRatio);
		}

		return analyticsData;
	}


	@Override
	public HashMap<String, Float> getTotalAmountByYearForAllCategories() {
		List<Expense> expenses = expenseRepository.findByIsReportedAndIsHidden(true,false);
		HashMap<String, Float> result = new HashMap<>();

		for (Expense expense : expenses) {
			LocalDate expenseDate = expense.getDate();
			String year = String.valueOf(expenseDate.getYear());
			Float amount = expense.getAmountINR();

			if (result.containsKey(year)) {
				Float previousAmount = result.get(year);
				result.put(year, previousAmount + amount);
			} else {
				result.put(year, amount);
			}
		}

		return result;
	}

	@Override
	public HashMap<String, Float> getTotalAmountByMonthForAllCategories(Long year) {
		List<Expense> expenses = expenseRepository.findByIsReportedAndIsHidden(true,false);
		HashMap<String, Float> result = new HashMap<>();

		for (Expense expense : expenses) {
			LocalDate expenseDate = expense.getDate();
			if (expenseDate.getYear() == year) {
				String monthYear = expenseDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
				Float amount = expense.getAmountINR();

				if (result.containsKey(monthYear)) {
					Float previousAmount = result.get(monthYear);
					result.put(monthYear, previousAmount + amount);
				} else {
					result.put(monthYear, amount);
				}
			}
		}

		return result;
	}
@Override
	public HashMap<String, Float> getYearlyReimbursementRatioForAllCategories() {
		List<Expense> expenses = expenseRepository.findByIsReportedAndIsHidden(true,false);
		HashMap<String, Float> yearlyReimbursementRatioMap = new HashMap<>();

		for (Expense expense : expenses) {
			String year = String.valueOf(expense.getDate().getYear());

			if (yearlyReimbursementRatioMap.containsKey(year)) {
				float totalReimbursedAmount = yearlyReimbursementRatioMap.get(year) + expense.getAmountINR();
				int totalNumberOfExpenses = (int) (yearlyReimbursementRatioMap.get(year + CONSTANT2) + 1);
				float reimbursementRatio = totalReimbursedAmount / totalNumberOfExpenses;

				yearlyReimbursementRatioMap.put(year, totalReimbursedAmount);
				yearlyReimbursementRatioMap.put(year + CONSTANT2, (float) totalNumberOfExpenses);
				yearlyReimbursementRatioMap.put(year + CONSTANT1, reimbursementRatio);
			} else {
				yearlyReimbursementRatioMap.put(year, expense.getAmountINR());
				yearlyReimbursementRatioMap.put(year + CONSTANT2, 1F);
				yearlyReimbursementRatioMap.put(year + CONSTANT1, expense.getAmountINR());
			}
		}
		for (String yearKey : yearlyReimbursementRatioMap.keySet()) {
			if (!yearKey.contains("_")) {
				String year = yearKey;
				float totalReimbursedAmount = yearlyReimbursementRatioMap.get(year);
				Float totalNumberOfExpenses = (yearlyReimbursementRatioMap.get(year + CONSTANT2));
				float reimbursementRatio = totalReimbursedAmount / totalNumberOfExpenses;
				yearlyReimbursementRatioMap.put(year + CONSTANT1, reimbursementRatio);
			}
		}

		return yearlyReimbursementRatioMap;
	}
@Override
	public HashMap<String, Float> getMonthlyReimbursementRatioForAllCategories(Long year) {
		List<Expense> expenses = expenseRepository.findByIsReportedAndIsHidden(true,false);
		HashMap<String, Float> monthlyReimbursementRatioMap = new HashMap<>();

		for (Expense expense : expenses) {
			int expenseYear = expense.getDate().getYear();

			if (expenseYear == year) {
				String month =  expense.getDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
				String monthKey = year + "_" + month;
				if (monthlyReimbursementRatioMap.containsKey(monthKey)) {
					float totalReimbursedAmount = monthlyReimbursementRatioMap.get(monthKey) + expense.getAmountINR();
					Float totalNumberOfExpenses = (monthlyReimbursementRatioMap.get(monthKey + CONSTANT2) + 1);
					float reimbursementRatio = totalReimbursedAmount / totalNumberOfExpenses;

					monthlyReimbursementRatioMap.put(monthKey, totalReimbursedAmount);
					monthlyReimbursementRatioMap.put(monthKey + CONSTANT2, (float) totalNumberOfExpenses);
					monthlyReimbursementRatioMap.put(monthKey + CONSTANT1, reimbursementRatio);
				} else {
					monthlyReimbursementRatioMap.put(monthKey, expense.getAmountINR());
					monthlyReimbursementRatioMap.put(monthKey + CONSTANT2, 1F);
					monthlyReimbursementRatioMap.put(monthKey + CONSTANT1, expense.getAmountINR());
				}
			}
		}

		return monthlyReimbursementRatioMap;
	}
	@Override
	public HashMap<String, Object> getYearlyCategoryAnalyticsForAllCategories() {
		HashMap<String, Object> analyticsData = new HashMap<>();

		HashMap<String, Float> totalAmountByYear = getTotalAmountByYearForAllCategories();
		HashMap<String, Float> yearlyReimbursementRatio = getYearlyReimbursementRatioForAllCategories();

		analyticsData.put("categoryTotalAmountByYear", totalAmountByYear);
		analyticsData.put("yearlyReimbursementRatio", yearlyReimbursementRatio);

		return analyticsData;
	}

	@Override
	public HashMap<String,Object> getMonthlyCategoryAnalyticsForAllCategories(Long year)
	{
		HashMap<String, Object> analyticsData = new HashMap<>();
			HashMap<String, Float> categoryTotalAmountByMonth = getTotalAmountByMonthForAllCategories(year);
			HashMap<String, Float> monthlyReimbursementRatio = getMonthlyReimbursementRatioForAllCategories(year);

			analyticsData.put("categoryTotalAmountByMonth", categoryTotalAmountByMonth);
			analyticsData.put("monthlyReimbursementRatio", monthlyReimbursementRatio);
		return analyticsData;
	}

}
