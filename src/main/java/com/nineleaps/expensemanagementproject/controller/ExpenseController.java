package com.nineleaps.expensemanagementproject.controller;

/**
          ExpenseController

              Manages endpoints related to expenses, including CRUD operations and filtering by employee and report IDs.

              ## Endpoints

              ### POST /insertExpenses/{employeeId}

              - **Description:** Adds an expense for a specified employee and category.
              - **Request Body:** ExpenseDTO object containing expense details.
              - **Path Parameters:**
                  - employeeId (Long) - ID of the employee.
              - **Query Parameters:**
                  - categoryId (Long) - ID of the category.
              - **Returns:** Message indicating the success or failure of expense addition.

              ### POST /addPotentialDuplicateExpense/{employeeId}

              - **Description:** Adds a potentially duplicate expense for a specified employee and category.
              - **Request Body:** ExpenseDTO object containing expense details.
              - **Path Parameters:**
                  - employeeId (Long) - ID of the employee.
              - **Query Parameters:**
                  - categoryId (Long) - ID of the category.
              - **Returns:** The added expense as an Expense object.

              ### GET /showAllExpenses
              - **Description:** Retrieves details of all expenses.
              - **Returns:** A list of Expense objects containing expense details.

              ### GET /findExpense/{expenseId}
              - **Description:** Retrieves details of an expense by ID.
              - **Path Parameters:**
                  - expenseId (Long) - ID of the expense.
              - **Returns:** The Expense object corresponding to the provided ID.

              ### PUT /updateExpenses/{expenseId}
              - **Description:** Updates details of a specific expense.
              - **Request Body:** ExpenseDTO object containing updated expense details.
              - **Path Parameters:**
                  - expenseId (Long) - ID of the expense.
              - **Returns:** The updated Expense object.

              ### GET /getExpenseByEmployeeId/{employeeId}
              - **Description:** Retrieves expenses based on the employee ID.
              - **Path Parameters:**
                  - employeeId (Long) - ID of the employee.
              - **Returns:** A list of Expense objects corresponding to the provided employee ID.

              ### GET /getExpenseByReportId/{reportId}
              - **Description:** Retrieves expenses based on the report ID.
              - **Path Parameters:**
                  - reportId (Long) - ID of the report.
              - **Returns:** A list of Expense objects corresponding to the provided report ID.

              ### POST /removeTaggedExpense/{expenseId}
              - **Description:** Removes a tagged expense.
              - **Path Parameters:**
                  - expenseId (Long) - ID of the expense.
              - **Returns:** The removed Expense object.

              ### GET /getUnreportedExpensesByEmployeeId/{employeeId}
              - **Description:** Retrieves unreported expenses based on the employee ID.
              - **Path Parameters:**
                  - employeeId (Long) - ID of the employee.
              - **Returns:** A list of Expense objects corresponding to the provided employee ID.

              ### POST /deleteExpense/{expenseId}
              - **Description:** Hides an expense by ID.
              - **Path Parameters:**
                  - expenseId (Long) - ID of the expense.

 */
import java.util.List;
import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.service.IExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/expense")
public class ExpenseController {
    @Autowired
    private IExpenseService expenseService;
    @PostMapping("/insertExpenses/{employeeId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public String saveExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long employeeId,
                              @RequestParam Long categoryId) throws IllegalAccessException {
        log.info("Received request to add expense for employee ID: {}", employeeId);
        log.debug("Expense details: {}", expenseDTO);

        String result;
        try {
            result = expenseService.addExpense(expenseDTO, employeeId, categoryId);
            log.info("Expense added successfully for employee ID: {}", employeeId);
        } catch (IllegalAccessException e) {
            log.error("Failed to add expense for employee ID: {}. Reason: {}", employeeId, e.getMessage());
            throw e;
        }
        return result;
    }

    @PostMapping("/addPotentialDuplicateExpense/{employeeId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public Expense addPotentialDuplicateExpense(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long employeeId,
                                                @RequestParam Long categoryId) {
        log.info("Received request to potentially add duplicate expense for employee ID: {}", employeeId);
        log.debug("Expense details: {}", expenseDTO);

        return expenseService.addPotentialDuplicateExpense(expenseDTO, employeeId, categoryId);
    }

    @GetMapping("/showAllExpenses")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public List<Expense> getAllExpenses() {
        log.info("Fetching all expenses...");
        return expenseService.getAllExpenses();
    }

    @GetMapping("/findExpense/{expenseId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public Expense getExpenseById(@PathVariable Long expenseId) {
        log.info("Fetching expense with ID: {}", expenseId);
        return expenseService.getExpenseById(expenseId);
    }

    @PutMapping("/updateExpenses/{expenseId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public Expense updateExpenses(@RequestBody ExpenseDTO expenseDTO, @PathVariable Long expenseId) {
        log.info("Updating expense with ID: {}", expenseId);
        return expenseService.updateExpenses(expenseDTO, expenseId);
    }

    @GetMapping("/getExpenseByEmployeeId/{employeeId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public List<Expense> getExpenseByEmpId(@PathVariable Long employeeId) {
        log.info("Retrieving expenses for employee ID: {}", employeeId);
        return expenseService.getExpenseByEmployeeId(employeeId);
    }

    @GetMapping("/getExpenseByReportId/{reportId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public List<Expense> getExpenseByReportId(@PathVariable Long reportId) {
        log.info("Retrieving expenses for report ID: {}", reportId);
        return expenseService.getExpenseByReportId(reportId);
    }

    @PostMapping("/removeTaggedExpense/{expenseId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public Expense removeTaggedExpense(@PathVariable Long expenseId) {
        log.info("Removing tagged expense with ID: {}", expenseId);
        return expenseService.removeTaggedExpense(expenseId);
    }

    @GetMapping("/getUnreportedExpensesByEmployeeId/{employeeId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public List<Expense> getExpensesById(@PathVariable Long employeeId) {
        log.info("Retrieving unreported expenses for employee ID: {}", employeeId);
        return expenseService.getExpensesByEmployeeId(employeeId);
    }

    @PostMapping("/deleteExpense/{expenseId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','FINANCE_ADMIN')")
    public void hideExpense(@PathVariable Long expenseId) {
        log.info("Hiding expense with ID: {}", expenseId);
        expenseService.hideExpense(expenseId);
    }
}
