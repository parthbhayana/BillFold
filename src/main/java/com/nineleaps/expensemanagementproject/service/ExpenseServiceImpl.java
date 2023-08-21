package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.nineleaps.expensemanagementproject.DTO.ExpenseDTO;
import com.nineleaps.expensemanagementproject.entity.*;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationRequest;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

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

    @Autowired
    private ICurrencyExchange currencyExchange;
    @Autowired
    private IEmailService emailService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Transactional
    @Override
    public String addExpense(ExpenseDTO expenseDTO, Long employeeId, Long categoryId) throws IllegalAccessException {
        Employee employee = employeeService.getEmployeeById(employeeId);
        Expense expense = new Expense();
        expense.setEmployee(employee);
        LocalDateTime now = LocalDateTime.now();
        expense.setDateCreated(now);
        String curr = expenseDTO.getCurrency();
        String date = expenseDTO.getDate().toString();
        double rate = currencyExchange.getExchangeRate(curr, date);
        double amountInInr = expenseDTO.getAmount() * rate;
        expense.setAmountINR((float) amountInInr);
        Category category = categoryRepository.getCategoryByCategoryId(categoryId);
        String categoryDescription = category.getCategoryDescription();

        //Check for Potential duplicate
        List<Expense> potentialDuplicateExpense =
                expenseRepository.findByEmployeeAndAmountAndDateAndCategoryAndMerchantName(employee, expenseDTO.getAmount(), expenseDTO.getDate(), category, expenseDTO.getMerchantName());
        System.out.println("Potential Duplicate List = " + potentialDuplicateExpense);

        if (potentialDuplicateExpense.isEmpty()) {
            expense.setDescription(expenseDTO.getDescription());
            expense.setAmount(expenseDTO.getAmount());
            expense.setCurrency(expenseDTO.getCurrency());
            expense.setMerchantName(expenseDTO.getMerchantName());
            expense.setSupportingDocuments(expenseDTO.getSupportingDocuments());
            expense.setDate(expenseDTO.getDate());
            expense.setCategory(category);
            expense.setCategoryDescription(categoryDescription);
            expenseRepository.save(expense);
            return "Success!";
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
        String curr = expenseDTO.getCurrency();
        String date = expenseDTO.getDate().toString();
        double rate = currencyExchange.getExchangeRate(curr, date);
        double amountInInr = expenseDTO.getAmount() * rate;
        expense.setAmountINR((float) amountInInr);
        Category category = categoryRepository.getCategoryByCategoryId(categoryId);
        String categoryDescription = category.getCategoryDescription();
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setCurrency(expenseDTO.getCurrency());
        expense.setMerchantName(expenseDTO.getMerchantName());
        expense.setSupportingDocuments(expenseDTO.getSupportingDocuments());
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
        return expenseRepository.findById(expenseId).get();
    }

    @Override
    public Expense updateExpense(Long reportId, Long employeeId) {
        Expense expense = getExpenseById(employeeId);
        Reports report = reportServices.getReportById(reportId);
        String reportTitle = report.getReportTitle();
        boolean reportedStatus = true;
        if (expense != null) {
            expense.setReports(report);
            expense.setIsReported(reportedStatus);
            expense.setReportTitle(reportTitle);
            expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
        }
        return expenseRepository.save(expense);
    }

    @Override
    public void deleteExpenseById(Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    @Override
    public List<Expense> getExpenseByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return expenseRepository.findByEmployeeAndIsHidden(employee, false);
    }

    @Override
    public Expense updateSupportingDocument(String supportingDoc, Long expenseId) {
        return null;
    }

    @Override
    public Expense updateExpenses(ExpenseDTO expenseDTO, Long expenseId) {
        Expense expense = getExpenseById(expenseId);
        if ((!expense.getIsHidden() && !expense.getIsReported()) || ((expense.getIsReported()) &&
                (expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.REJECTED))) {
            expense.setMerchantName(expenseDTO.getMerchantName());
            expense.setDate(expenseDTO.getDate());
            expense.setAmount(expenseDTO.getAmount());
            expense.setDescription(expenseDTO.getDescription());
            expense.setSupportingDocuments(expenseDTO.getSupportingDocuments());
            LocalDateTime now = LocalDateTime.now();
            expense.setDateCreated(now);
            expense.setCurrency(expenseDTO.getCurrency());
            String curr = expense.getCurrency();
            String date = expense.getDate().toString();
            double rate = currencyExchange.getExchangeRate(curr, date);
            double amountInInr = expense.getAmount() * rate;
            expense.setAmountINR((float) amountInInr);
            expenseRepository.save(expense);
        }
        if (expense.getIsHidden()) {
            throw new IllegalStateException("Expense " + expenseId + " does not exist!");
        }
        if (expense.getIsReported() &&
                ((expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.PENDING) ||
                        (expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.APPROVED) ||
                        (expense.getManagerApprovalStatusExpense() ==
                                ManagerApprovalStatusExpense.PARTIALLY_APPROVED))) {
            throw new IllegalStateException(
                    "Can not edit Expense with ExpenseId:" + expenseId + " as it is already reported!");
        }
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return expenseRepository.findByEmployeeAndIsReported(employee, false);
    }

    @Override
    public Expense removeTaggedExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).get();
        boolean removeExpense = false;
        if (!expense.getIsHidden()) {
            expense.setIsReported(removeExpense);
            expense.setReports(null);
            return expenseRepository.save(expense);
        }
        throw new IllegalStateException("Expense " + expenseId + " does not exist!");
    }


    @Override
    public List<Expense> getExpenseByReportId(Long reportId) {
        Reports report = reportsRepository.findById(reportId).get();
        return expenseRepository.findByReportsAndIsHidden(report, false);
    }


    @Override
    public void hideExpense(Long expId) {
        Boolean hidden = true;
        Expense expense = getExpenseById(expId);
        if (!expense.getIsReported()) {
            expense.setIsHidden(hidden);
        }
        if (expense.getIsReported()) {
            throw new IllegalStateException(
                    "Cannot delete expense " + expId + " as it is already reported in Report: " +
                            expense.getReportTitle());
        }
        expenseRepository.save(expense);
    }

    @Scheduled(cron = "0 0 15 * * *")
    public void sendExpenseReminder() {
        LocalDate currentDate = LocalDate.now();

        List<Expense> expenseList = expenseRepository.findByIsReportedAndIsHidden(false, false);
        List<Long> expenseIds = new ArrayList<>();
        for (Expense expense : expenseList) {
            LocalDate submissionDate = expense.getDate();
            LocalDate expirationDate = submissionDate.plusDays(60);
            if (currentDate.isAfter(expirationDate.minusDays(5)) && currentDate.isBefore(expirationDate)) {
                expenseIds.add(expense.getExpenseId());
            }
        }
        emailService.reminderMailToEmployee(expenseIds);
        //Push Notification Functionality
        for (Long expense : expenseIds) {
            Expense exp = getExpenseById(expense);
            Long employeeId = exp.getExpenseId();
            Employee employee = employeeService.getEmployeeById(employeeId);
            PushNotificationRequest notificationRequest = new PushNotificationRequest();
            notificationRequest.setTitle("[REMINDER]: Report your pending expenses.");
            notificationRequest.setMessage("Unreported expenses will be deleted.");
            notificationRequest.setToken(employee.getToken());
            System.out.println("TOKEN-" + employee.getToken());

            pushNotificationService.sendPushNotificationToToken(notificationRequest);
        }
    }

    @Override
    public List<Expense> getRejectedExpensesByReportId(Long reportId) {
        List<Expense> rejectedExpenses = new ArrayList<>();
        List<Expense> expenses = getExpenseByReportId(reportId);

        for (Expense expense : expenses) {
            if (expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.REJECTED) {
                rejectedExpenses.add(expense);
            }
        }
        return rejectedExpenses;
    }


}