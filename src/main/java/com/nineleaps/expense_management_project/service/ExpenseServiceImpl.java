package com.nineleaps.expense_management_project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import com.nineleaps.expense_management_project.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;

@Service
public class ExpenseServiceImpl implements IExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IReportsService reportServices;

    @Autowired
    private ReportsRepository reportsRepository;


    @Transactional
    @Override
    public String addExpense(ExpenseDTO expenseDTO, Long employeeId, Long categoryId)  {
        Employee employee = employeeService.getEmployeeById(employeeId);
        Expense expense = new Expense();
        expense.setEmployee(employee);
        LocalDateTime now = LocalDateTime.now();
        expense.setDateCreated(now);
        @SuppressWarnings("unused")
        String date = expenseDTO.getDate().toString();
        Category category = categoryRepository.getCategoryByCategoryId(categoryId);
        String categoryDescription = category.getCategoryDescription();

        // Check for Potential duplicate
        List<Expense> potentialDuplicateExpense = expenseRepository
                .findByEmployeeAndAmountAndDateAndCategoryAndMerchantNameAndIsHidden(employee, expenseDTO.getAmount(),
                        expenseDTO.getDate(), category, expenseDTO.getMerchantName(), false);


        if (potentialDuplicateExpense.isEmpty()) {
            expense.setDescription(expenseDTO.getDescription());
            expense.setAmount(expenseDTO.getAmount());
            expense.setMerchantName(expenseDTO.getMerchantName());
            expense.setFile(expenseDTO.getFile());
            expense.setFileName(expenseDTO.getFileName());
            expense.setDate(expenseDTO.getDate());
            expense.setCategory(category);
            expense.setCategoryDescription(categoryDescription);
            expenseRepository.save(expense);
            return "Expense Added!";
        } else {
            return "Expense might be a potential duplicate!";
        }
    }

    @Transactional
    @Override
    public Expense addPotentialDuplicateExpense(ExpenseDTO expenseDTO, Long employeeId, Long categoryId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        Expense expense = new Expense();
        expense.setEmployee(employee);
        LocalDateTime now = LocalDateTime.now();
        expense.setDateCreated(now);
        @SuppressWarnings("unused")
        String date = expenseDTO.getDate().toString();
        Category category = categoryRepository.getCategoryByCategoryId(categoryId);
        String categoryDescription = category.getCategoryDescription();
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setMerchantName(expenseDTO.getMerchantName());
        expense.setFile(expenseDTO.getFile());
        expense.setFileName(expenseDTO.getFileName());
        expense.setDate(expenseDTO.getDate());
        expense.setCategory(category);
        expense.setCategoryDescription(categoryDescription);
        expense.setPotentialDuplicate(true);
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        LocalDate sixtyDaysAgo = LocalDate.now().minusDays(60);
        List<Expense> unreportedExpenses = expenseRepository.findByIsReportedAndDateBefore(false, sixtyDaysAgo);
        for (Expense expense : unreportedExpenses) {
            expense.setIsHidden(true);
            expenseRepository.save(expense);
        }
        return expenseRepository.findAll();
    }

    @Override
    public Expense getExpenseById(Long expenseId) {
        Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);

        if (expenseOptional.isPresent()) {
            return expenseOptional.get();
        } else {

            throw new EntityNotFoundException("Expense with ID " + expenseId + " not found");
        }
    }
    @Override
    public Expense updateExpense(Long reportId, Long employeeId) {
        Expense expense = getExpenseById(employeeId);
        Reports report = reportServices.getReportById(reportId);

        if (expense != null) {
            String reportTitle = report.getReportTitle();
            boolean reportedStatus = true;

            expense.setReports(report);
            expense.setIsReported(reportedStatus);
            expense.setReportTitle(reportTitle);
            expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);

            return expenseRepository.save(expense);
        } else {

            throw new IllegalStateException("Expense with ID " + employeeId + " not found.");
        }
    }

    @Override
    public void deleteExpenseById(Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    @Override
    public List<Expense> getExpenseByEmployeeId(Long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            Sort sort = Sort.by(Sort.Direction.DESC, "dateCreated");
            return expenseRepository.findByEmployeeAndIsHidden(employee, false, sort);
        } else {

            return Collections.emptyList();
        }
    }



    @Override
    public Expense updateExpenses(ExpenseDTO expenseDTO, Long expenseId) {
        Expense expense = getExpenseById(expenseId);

        boolean isNotHidden = !expense.getIsHidden();
        boolean isNotReportedOrRejected = !expense.getIsReported() || expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.REJECTED;

        if ((isNotHidden && isNotReportedOrRejected) || (expense.getIsReported() && isNotApprovedOrPartiallyApproved(expense))) {
            expense.setMerchantName(expenseDTO.getMerchantName());
            expense.setDate(expenseDTO.getDate());
            expense.setAmount(expenseDTO.getAmount());
            expense.setDescription(expenseDTO.getDescription());
            expense.setFile(expenseDTO.getFile());
            LocalDateTime now = LocalDateTime.now();
            expense.setDateCreated(now);
            expenseRepository.save(expense);
        } else if (Boolean.TRUE.equals(expense.getIsHidden())) {
            throw new IllegalStateException("Expense " + expenseId + " does not exist!");
        }

        return expenseRepository.save(expense);
    }


    private boolean isApprovedOrPartiallyApproved(Expense expense) {
        ManagerApprovalStatusExpense status = expense.getManagerApprovalStatusExpense();
        return status == ManagerApprovalStatusExpense.PENDING ||
                status == ManagerApprovalStatusExpense.APPROVED ||
                status == ManagerApprovalStatusExpense.PARTIALLY_APPROVED;
    }

    private boolean isNotApprovedOrPartiallyApproved(Expense expense) {
        return !isApprovedOrPartiallyApproved(expense);
    }

    @Override
    public List<Expense> getExpensesByEmployeeId(Long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            return expenseRepository.findByEmployeeAndIsReported(employee, false);
        } else {

            return Collections.emptyList();
        }
    }






    @Override
    public Expense removeTaggedExpense(Long expenseId) {
        Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);
        if (expenseOptional.isPresent()) {
            Expense expense = expenseOptional.get();
            if (Boolean.FALSE.equals(expense.getIsHidden())) {
                boolean removeExpense = false;
                expense.setIsReported(removeExpense);
                expense.setReports(null);
                return expenseRepository.save(expense);
            } else {
                throw new IllegalStateException("Expense " + expenseId + " is hidden and cannot be removed.");
            }
        } else {
            throw new IllegalStateException("Expense " + expenseId + " does not exist.");
        }
    }


    @Override
    public List<Expense> getExpenseByReportId(Long reportId) {
        Optional<Reports> reportOptional = reportsRepository.findById(reportId);

        if (reportOptional.isPresent()) {
            Reports report = reportOptional.get();
            return expenseRepository.findByReportsAndIsHidden(report, false);
        } else {

            return Collections.emptyList();
        }
    }

    @Override
    public void hideExpense(Long expId) {
        boolean hidden = true;
        Expense expense = getExpenseById(expId);

        if (Boolean.FALSE.equals(expense.getIsReported())) {
            expense.setIsHidden(hidden);
        } else {
            throw new IllegalStateException("Cannot delete expense " + expId + " as it is already reported in Report: "
                    + expense.getReportTitle());
        }
        expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getRejectedExpensesByReportId(Long reportId) {
        return getExpenseByReportId(reportId).stream()
                .filter(expense -> expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.REJECTED)
                .collect(Collectors.toList());
    }
}