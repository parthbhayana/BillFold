package com.nineleaps.expensemanagementproject.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.service.IExpenseService;


@RestController
public class ExpenseController {
	@Autowired
	private IExpenseService expService;
	
	@PostMapping("/insertexpenses")
	public Expense saveExpense(@RequestBody Expense expense) {
		return expService.saveCustomer(expense);
	}
	
	@GetMapping("/showallexpenses")
	public List<Expense> getAllExpenses(){
		return expService.getAllExpenses();
		
	}
	
	@GetMapping("/findexpense/{expId}")
	public Expense getExpenseById(@PathVariable("expId") Long expenseId)
	{
		return expService.getExpenseById(expenseId);
	}
	@PutMapping("/updateexpense/{expId}")
	public Expense updateCustomer(@RequestBody Expense newExpense,@PathVariable("expId") Long expenseId) {
		Expense expense =  expService.getExpenseById(expenseId);
				
		expense.setMerchantName(newExpense.getMerchantName());
		expense.setDate(newExpense.getDate());
		expense.setAmount(newExpense.getAmount());
		expense.setDescription(newExpense.getDescription());
		expense.setCategory(newExpense.getCategory());
		return expService.updateExpense(expense);
	}
	
	@DeleteMapping("/deleteexpense/{expId}")
	public void deleteCustomerById(@PathVariable("expId") Long expenseId){
		 expService.deleteExpenseById(expenseId);
		 
	}
	

}