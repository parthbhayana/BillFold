package com.nineleaps.expense_management_project.controller;

import java.util.List;
import java.util.NoSuchElementException;

import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.service.IExpenseService;

@RestController
@RequestMapping("/expense")
@ApiResponses(
        value = {
                @ApiResponse(code = 200, message = "SUCCESS"),
                @ApiResponse(code = 401, message = "UNAUTHORIZED"),
                @ApiResponse(code = 403, message = "ACCESS_FORBIDDEN"),
                @ApiResponse(code = 404, message = "NOT_FOUND"),
                @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
        })

public class ExpenseController {
    @Autowired
    private IExpenseService expenseService;

    @PostMapping("/insertExpenses/{employeeId}")
    public String saveExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long employeeId,
                              @RequestParam Long categoryId) throws IllegalAccessException {
        return expenseService.addExpense(expenseDTO, employeeId, categoryId);
    }

    @PostMapping("/addPotentialDuplicateExpense/{employeeId}")
    public Expense addPotentialDuplicateExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long employeeId,
                                                @RequestParam Long categoryId) {
        return expenseService.addPotentialDuplicateExpense(expenseDTO, employeeId, categoryId);
    }

    @GetMapping("/showAllExpenses")
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/findExpense/{expenseId}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable("expenseId") Long expenseId) {
        try {
            Expense expense = expenseService.getExpenseById(expenseId);
            return ResponseEntity.ok(expense);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateExpenses/{expenseId}")
    public ResponseEntity<Expense> updateExpenses(@RequestBody ExpenseDTO expenseDTO, @PathVariable("expenseId") Long expenseId) {
        try {
            Expense updatedExpense = expenseService.updateExpenses(expenseDTO, expenseId);
            return ResponseEntity.ok(updatedExpense);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/hideExpense/{expenseId}")
    public ResponseEntity<Void> hideExpense(@PathVariable("expenseId") Long expenseId) {
        try {
            expenseService.hideExpense(expenseId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getExpenseByEmployeeId/{employeeId}")
    @ApiOperation(value = "Deprecated Endpoint", tags = "api-v1")
    public List<Expense> getExpenseByEmpId(@PathVariable Long employeeId) {
        return expenseService.getExpenseByEmployeeId(employeeId);
    }

    @GetMapping("/getExpenseByEmpId/{employeeId}")
    public List<Expense> getExpenseByEmpId(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return expenseService.getExpenseByEmployeeId(employeeId, page, size);
    }



    @GetMapping("/getExpenseByReportId/{reportId}")
    public List<Expense> getExpenseByReportId(@PathVariable Long reportId) {
        return expenseService.getExpenseByReportId(reportId);
    }

    @PostMapping("/removeTaggedExpense/{expenseId}")
    public Expense removeTaggedExpense(@PathVariable Long expenseId) {
        return expenseService.removeTaggedExpense(expenseId);
    }

    @GetMapping("/getUnreportedExpensesByEmployeeId/{employeeId}")
    public List<Expense> getExpensesById(@PathVariable Long employeeId) {
        return expenseService.getExpensesByEmployeeId(employeeId);
    }

}