package com.nineleaps.expense_management_project.controller;

import java.util.List;

import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.service.IExpenseService;

@RestController
public class ExpenseController {
    @Autowired
    private IExpenseService expenseService;

    // Endpoint to add an expense for a specific employee and category
    @PostMapping("/insertExpenses/{employeeId}")
    public String saveExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long employeeId,
                              @RequestParam Long categoryId) throws IllegalAccessException {
        return expenseService.addExpense(expenseDTO, employeeId, categoryId);
    }

    // Endpoint to add a potential duplicate expense for a specific employee and category
    @PostMapping("/addPotentialDuplicateExpense/{employeeId}")
    public Expense addPotentialDuplicateExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long employeeId,
                                                @RequestParam Long categoryId) {
        return expenseService.addPotentialDuplicateExpense(expenseDTO, employeeId, categoryId);
    }

    // Endpoint to retrieve a list of all expenses
    @GetMapping("/showAllExpenses")
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    // Endpoint to find an expense by its ID
    @GetMapping("/findExpense/{expenseId}")
    public Expense getExpenseById(@PathVariable Long expenseId) {
        return expenseService.getExpenseById(expenseId);
    }

    // Endpoint to update an expense by its ID
    @PutMapping("/updateExpenses/{expenseId}")
    public Expense updateExpenses(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long expenseId) {
        return expenseService.updateExpenses(expenseDTO, expenseId);
    }

    // Endpoint to get expenses by employee ID
    @GetMapping("/getExpenseByEmployeeId/{employeeId}")
    public List<Expense> getExpenseByEmpId(@PathVariable Long employeeId) {
        return expenseService.getExpenseByEmployeeId(employeeId);
    }

    // Endpoint to get expenses by report ID
    @GetMapping("/getExpenseByReportId/{reportId}")
    public List<Expense> getExpenseByReportId(@PathVariable Long reportId) {
        return expenseService.getExpenseByReportId(reportId);
    }

    // Endpoint to remove a tagged expense by its ID
    @PostMapping("/removeTaggedExpense/{expenseId}")
    public Expense removeTaggedExpense(@PathVariable Long expenseId) {
        return expenseService.removeTaggedExpense(expenseId);
    }

    // Endpoint to get unreported expenses by employee ID
    @GetMapping("/getUnreportedExpensesByEmployeeId/{employeeId}")
    public List<Expense> getExpensesById(@PathVariable Long employeeId) {
        return expenseService.getExpensesByEmployeeId(employeeId);
    }

    // Endpoint to hide an expense by its ID
    @PostMapping("/hideExpense/{expenseId}")
    public void hideExpense(@PathVariable Long expenseId) {
        expenseService.hideExpense(expenseId);
    }
}
