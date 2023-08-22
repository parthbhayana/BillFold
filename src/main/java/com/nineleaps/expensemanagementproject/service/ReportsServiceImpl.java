package com.nineleaps.expensemanagementproject.service;


import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.DTO.ReportsDTO;
import com.nineleaps.expensemanagementproject.entity.*;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationRequest;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service
public class ReportsServiceImpl implements IReportsService {

    @Autowired
    private ReportsRepository reportsRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IExpenseService expenseServices;

    @Autowired
    private IEmployeeService employeeServices;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private ICurrencyExchange currencyExchange;

    @Autowired
    private PushNotificationService pushNotificationService;
    private static final String CONSTANT1 = " does not exist!";
    private static final String CONSTANT2 = "Report with ID ";
    private static final String CONSTANT3 = "Expense with ID ";
    private static final String CONSTANT4 = "rejected";
    private static final String CONSTANT5 = "approved";
    private static final String CONSTANT6 = "Enter a valid request !";
    private static final String CONSTANT7 = "pending";
    private static final String CONSTANT8 = "Report ";
    private static final String CONSTANT9 = " is not Submitted!";


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Long getNextReportSerialNumber() {
        return null;
    }

    @Override
    public List<Reports> getAllReports() {
        return reportsRepository.findAll();
    }

    @Override
    public Reports getReportById(Long reportId) {
        Optional<Reports> optionalReport = reportsRepository.findById(reportId);
        return optionalReport.orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public Reports addReport(ReportsDTO reportsDTO, Long employeeId, List<Long> expenseids) {
        Employee employee = employeeServices.getEmployeeById(employeeId);
        String employeeEmail = employee.getEmployeeEmail();
        String managerEmail = employee.getManagerEmail();
        String employeeName = (employee.getFirstName() + " " + employee.getLastName());
        String officialEmployeeId = employee.getOfficialEmployeeId();
        Reports newReport = new Reports();
        newReport.setReportTitle(reportsDTO.getReportTitle());
        newReport.setReportDescription(reportsDTO.getReportDescription());
        newReport.setEmployeeMail(employeeEmail);
        newReport.setManagerEmail(managerEmail);
        newReport.setEmployeeName(employeeName);
        newReport.setOfficialEmployeeId(officialEmployeeId);
        newReport.setDateCreated(LocalDate.now());
        newReport.setEmployeeId(employeeId);
        String currency = null;
        if (!expenseids.isEmpty()) {
            Long firstExpenseId = expenseids.get(0);
            Expense ep = expenseRepository.getExpenseByexpenseId(firstExpenseId);
            currency = ep.getCurrency();
        }
        newReport.setCurrency(currency);
        reportsRepository.save(newReport);
        Long id = newReport.getReportId();
        List<Expense> expp = expenseRepository.findAllById(expenseids);
        //Setting Amounts
        float amt = 0;
        for (Expense expense2 : expp) {
            amt += expense2.getAmountINR();
        }
        newReport.setTotalAmountINR(amt);
        float amtCurrency = 0;
        for (Expense expense2 : expp) {
            amtCurrency += expense2.getAmount();
        }
        newReport.setTotalAmountCurrency(amtCurrency);
        addExpenseToReport(id, expenseids);
        String reportTitle = newReport.getReportTitle();
        List<Expense> exp = expenseServices.getExpenseByReportId(id);
        for (Expense exp2 : exp) {
            if (exp != null) {
                exp2.setReportTitle(reportTitle);
                expenseRepository.save(exp2);
            }
        }
        return reportsRepository.save(newReport);
    }


    @Override
    public List<Reports> editReport(Long reportId, String reportTitle, String reportDescription,
                                    List<Long> addExpenseIds, List<Long> removeExpenseIds) {
        Long empId = null;
        Reports report = getReportById(reportId);
        if (report.getIsSubmitted()) {
            throw new IllegalStateException(
                    "Can not edit Report with ReportId:" + reportId + " as it is already submitted!");
        } else if (report.getIsHidden()) {
            throw new NullPointerException(CONSTANT2 + reportId + CONSTANT1);
        } else if (!report.getIsHidden() ||
                report.getIsSubmitted() && report.getManagerapprovalstatus() == ManagerApprovalStatus.ACTION_REQUIRED) {
            report.setReportTitle(reportTitle);
            report.setReportDescription(reportDescription);
            reportsRepository.save(report);
            // Updating Report Title in existing expenses
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            for (Expense exp : expenseList) {
                if (exp != null) {
                    exp.setReportTitle(reportTitle);
                    expenseRepository.save(exp);
                    // Fetching employeeId
                    Expense expense = expenseList.get(0);
                    Employee employee = expense.getEmployee();
                    empId = employee.getEmployeeId();
                }
            }
            // Adding Expenses
            for (Long expenseid : addExpenseIds) {
                Expense expense = expenseServices.getExpenseById(expenseid);
                if (expense.getIsReported()) {
                    throw new IllegalStateException(CONSTANT3 + expenseid + " is already reported in another report!");
                }
                if (report != null && !expense.getIsReported()) {
                    expenseServices.updateExpense(reportId, expenseid);
                }
            }
            // Removing Expenses
            if (removeExpenseIds != null && !removeExpenseIds.isEmpty()) {
                boolean reportedStatus = false;
                for (Long expenseid : removeExpenseIds) {
                    Expense expense = expenseServices.getExpenseById(expenseid);
                    if (report != null && expense.getIsReported()) {
                        expense.setIsReported(reportedStatus);
                        expense.setReports(null);
                        expense.setReportTitle(null);
                        expenseRepository.save(expense);
                    }
                }
            }
        }
        Reports re = getReportById(reportId);
        re.setTotalAmountINR(totalAmountINR(reportId));
        re.setTotalAmountCurrency(totalAmountCurrency(reportId));
        re.setTotalApprovedAmountINR(totalApprovedAmountINR(reportId));
        re.setTotalApprovedAmountCurrency(totalApprovedAmountCurrency(reportId));
        return getReportByEmpId(empId, "drafts");
    }

    @Override
    public Reports addExpenseToReport(Long reportId, List<Long> expenseids) {
        Reports report = getReportById(reportId);
        if (report.getIsHidden()) {
            throw new NullPointerException(CONSTANT2 + reportId + CONSTANT1);
        }
        for (Long expenseid : expenseids) {
            Expense expense = expenseServices.getExpenseById(expenseid);
            if (expense.getIsReported()) {
                throw new IllegalStateException(CONSTANT3 + expenseid + " is already reported in another report!");
            }
            if (!expense.getIsReported()) {
                expenseServices.updateExpense(reportId, expenseid);
            }
        }
        Reports re = getReportById(reportId);
        re.setTotalAmountINR(totalAmountINR(reportId));
        re.setTotalAmountCurrency(totalAmountCurrency(reportId));
        return reportsRepository.save(re);
    }

    @Override
    public List<Reports> getReportByEmpId(Long employeeId, String request) {
        switch (request) {
            case "drafts":
                return reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(employeeId, false, false);
            case "submitted":
                return reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.PENDING, true, false);
            case CONSTANT4:
                return reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsHidden(employeeId,
                        ManagerApprovalStatus.REJECTED, false);
            case CONSTANT5:
                List<Reports> approvedList =
                        reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.APPROVED, true, false);
                List<Reports> partiallyApprovedList =
                        reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.PARTIALLY_APPROVED, true, false);
                List<Reports> mergedList = new ArrayList<>();
                mergedList.addAll(approvedList);
                mergedList.addAll(partiallyApprovedList);
                return mergedList;
            default:
                throw new IllegalArgumentException(CONSTANT6 + request);
        }
    }

    @Override
    public List<Reports> getReportsSubmittedToUser(String managerEmail, String request) {
        switch (request) {
            case CONSTANT5:
                List<Reports> approvedList =
                        reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.APPROVED, true, false);
                List<Reports> partiallyApprovedList =
                        reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.PARTIALLY_APPROVED, true, false);
                List<Reports> mergedList = new ArrayList<>();
                mergedList.addAll(approvedList);
                mergedList.addAll(partiallyApprovedList);
                return mergedList;
            case CONSTANT4:
                return reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsHidden(managerEmail,
                        ManagerApprovalStatus.REJECTED, false);
            case CONSTANT7:
                return reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.PENDING, true, false);
            default:
                throw new IllegalArgumentException(CONSTANT6);
        }
    }

    @Override
    public List<Reports> getAllSubmittedReports() {
        List<Reports> reports = reportsRepository.findAll();
        List<Reports> submittedReports = new ArrayList<>();
        for (Reports reports2 : reports) {
            if (reports2.getIsSubmitted()) {
                submittedReports.add(reports2);
            }
        }
        return submittedReports;
    }

    @Override
    public List<Reports> getAllReportsApprovedByManager(String request) {
        switch (request) {
            case CONSTANT5:
                List<Reports> approvedList1 =
                        reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.APPROVED, true, false);
                List<Reports> partiallyApprovedList1 =
                        reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.APPROVED, true, false);
                List<Reports> mergedList1 = new ArrayList<>();
                mergedList1.addAll(approvedList1);
                mergedList1.addAll(partiallyApprovedList1);
                return mergedList1;
            case CONSTANT4:
                List<Reports> approvedList2 =
                        reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REJECTED, true, false);
                List<Reports> partiallyApprovedList2 =
                        reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.REJECTED, true, false);
                List<Reports> mergedList2 = new ArrayList<>();
                mergedList2.addAll(approvedList2);
                mergedList2.addAll(partiallyApprovedList2);
                return mergedList2;


            case CONSTANT7:
                List<Reports> approvedList3 =
                        reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.PENDING, true, false);
                List<Reports> partiallyApprovedList3 =
                        reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.PENDING, true, false);
                List<Reports> mergedList3 = new ArrayList<>();
                mergedList3.addAll(approvedList3);
                mergedList3.addAll(partiallyApprovedList3);
                return mergedList3;
            case "reimbursed":
                List<Reports> approvedList4 =
                        reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REIMBURSED, true, false);
                List<Reports> partiallyApprovedList4 =
                        reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.REIMBURSED, true, false);
                List<Reports> mergedList4 = new ArrayList<>();
                mergedList4.addAll(approvedList4);
                mergedList4.addAll(partiallyApprovedList4);
                return mergedList4;


            default:
                throw new IllegalArgumentException(CONSTANT6);
        }
    }

    @Override
    public void submitReport(Long reportId, HttpServletResponse response) throws MessagingException, IOException {
        boolean submissionStatus = true;
        Reports re = getReportById(reportId);
        Long employeeId = re.getEmployeeId();

        if (re == null || re.getIsHidden()) {
            throw new NullPointerException(CONSTANT2 + reportId + CONSTANT1);
        }

        // Check if the report is already submitted and not rejected
        if (re.getIsSubmitted() && re.getManagerapprovalstatus() != ManagerApprovalStatus.REJECTED) {
            throw new IllegalStateException(CONSTANT2 + reportId + " is already submitted!");
        }

        // Check if the report is not submitted or was previously rejected by the manager
        if (!re.getIsSubmitted() || re.getManagerapprovalstatus() == ManagerApprovalStatus.REJECTED) {
            List<Expense> rejectedExpenses = expenseServices.getRejectedExpensesByReportId(reportId);
            for (Expense expense : rejectedExpenses) {
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
                expenseRepository.save(expense);
            }
            re.setIsSubmitted(submissionStatus);
            re.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
            re.setDateSubmitted(LocalDate.now());
            re.setTotalAmountINR(totalAmountINR(reportId));
            re.setTotalAmountCurrency(totalAmountCurrency(reportId));

            // Fetch the manager's email and set it in the report
            Employee employee = employeeServices.getEmployeeById(employeeId);
            String managerEmail = employee.getManagerEmail();
            if (managerEmail == null) {
                throw new NullPointerException("Manager Email not found for Employee ID: " + employee.getEmployeeId());
            }
            re.setManagerEmail(managerEmail);

            reportsRepository.save(re);

            // Send email notification to the manager
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            ArrayList<Long> expenseIds = new ArrayList<>();
            for (Expense expense : expenseList) {
                expenseIds.add(expense.getExpenseId());
            }
            emailService.managerNotification(reportId, expenseIds, response);
        }

        // Push Notification Functionality
        String managerEmail = re.getManagerEmail();
        Employee manager = employeeServices.getEmployeeByEmail(managerEmail);

        if (manager != null && manager.getToken() != null) {
            Employee employee = employeeServices.getEmployeeById(employeeId);
            PushNotificationRequest notificationRequest = new PushNotificationRequest();
            notificationRequest.setTitle(employee.getFirstName() + " " + employee.getLastName());
            notificationRequest.setMessage("Submitted you an expense report.");
            notificationRequest.setToken(manager.getToken());
            System.out.println("TOKEN-" + manager.getToken());

            pushNotificationService.sendPushNotificationToToken(notificationRequest);
        }
    }




    @Override
    public void reimburseReportByFinance(ArrayList<Long> reportIds, String comments) {
        FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.APPROVED;
        for (Long reportId : reportIds) {
            Reports re = getReportById(reportId);
            Long employeeId = re.getEmployeeId();
            if (re == null || re.getIsHidden()) {
                throw new ObjectNotFoundException(reportId, CONSTANT8 + reportId + CONSTANT1);
            }
            if (!re.getIsSubmitted()) {
                throw new IllegalStateException(CONSTANT8 + reportId + " is not submitted!");
            }

            if (re.getManagerapprovalstatus() != ManagerApprovalStatus.APPROVED &&
                    re.getManagerapprovalstatus() != ManagerApprovalStatus.PARTIALLY_APPROVED) {
                throw new IllegalStateException(CONSTANT8 + reportId + " is not approved by the manager!");
            }
            re.setFinanceApprovalStatus(approvalStatus);
            re.setFinanceComments(comments);
            re.setFinanceActionDate(LocalDate.now());
            reportsRepository.save(re);
            emailService.userReimbursedNotification(reportId);
            //Update Expenses Status
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            for (Expense exp : expenseList) {
                exp.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
                expenseRepository.save(exp);
            }
            //Push Notification Functionality

            Employee employee = employeeServices.getEmployeeById(employeeId);

            PushNotificationRequest notificationRequest = new PushNotificationRequest();
            notificationRequest.setTitle("[PUSHED TO REIMBURSEMENT]: " + re.getReportTitle());
            notificationRequest.setMessage("Your expense report is pushed to reimbursement.");
            notificationRequest.setToken(employee.getToken());
            System.out.println("TOKEN-" + employee.getToken());

            pushNotificationService.sendPushNotificationToToken(notificationRequest);
        }
    }

    @Override
    public void rejectReportByFinance(Long reportId, String comments) {
        FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.REJECTED;
        Reports re = getReportById(reportId);
        Long employeeId = re.getEmployeeId();
        if (!re.getIsSubmitted()) {
            throw new IllegalStateException(CONSTANT8 + reportId + CONSTANT9);
        }
        if (re.getIsHidden()) {
            throw new ObjectNotFoundException(reportId, CONSTANT8 + reportId + CONSTANT1);
        }
        if (!re.getIsHidden() && re.getIsSubmitted() &&
                re.getManagerapprovalstatus() == ManagerApprovalStatus.APPROVED) {
            re.setFinanceApprovalStatus(approvalStatus);
            re.setFinanceComments(comments);
            re.setFinanceActionDate(LocalDate.now());
            reportsRepository.save(re);
            emailService.userRejectedByFinanceNotification(reportId);
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            for (Expense exp : expenseList) {
                exp.setFinanceApprovalStatus(FinanceApprovalStatus.REJECTED);
                expenseRepository.save(exp);
            }
        }
        //Push Notification Functionality

        Employee employee = employeeServices.getEmployeeById(employeeId);

        PushNotificationRequest notificationRequest = new PushNotificationRequest();
        notificationRequest.setTitle("[REJECTED]: " + re.getReportTitle());
        notificationRequest.setMessage("Accounts admin rejected your expense report.");
        notificationRequest.setToken(employee.getToken());
        System.out.println("TOKEN-" + employee.getToken());

        pushNotificationService.sendPushNotificationToToken(notificationRequest);
    }

    @Override
    public float totalAmountINR(Long reportId) {
        Reports report = reportsRepository.findById(reportId).get();
        List<Expense> expenses = expenseRepository.findByReports(report);

        float amtINR = 0;
        for (Expense expense2 : expenses) {
            amtINR += expense2.getAmountINR();
        }
        return amtINR;
    }

    @Override
    public float totalAmountCurrency(Long reportId) {
        Reports report = reportsRepository.findById(reportId).get();
        List<Expense> expenses = expenseRepository.findByReports(report);

        float amtCurrency = 0;
        for (Expense expense2 : expenses) {
            amtCurrency += expense2.getAmount();
        }
        return amtCurrency;
    }

    @Override
    public float totalApprovedAmountCurrency(Long reportId) {
        Reports report = getReportById(reportId);
        List<Expense> expenseList = expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(report, true, false);
        float totalApprovedAmount = 0;
        for (Expense expense2 : expenseList) {
            Float amountApproved = expense2.getAmountApproved();
            if (amountApproved != null) {
                totalApprovedAmount += amountApproved;
            }

        }
        return totalApprovedAmount;

    }


    @Override
    public float totalApprovedAmountINR(Long reportId) {
        Reports report = getReportById(reportId);
        List<Expense> expenseList = expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(report, true, false);
        float totalApprovedAmount = 0;
        for (Expense expense2 : expenseList) {
            Double amountApprovedINR = expense2.getAmountApprovedINR();
            if (amountApprovedINR != null) {
                totalApprovedAmount += amountApprovedINR.floatValue();
            }


        }
        return totalApprovedAmount;
    }


    @Override
    public void hideReport(Long reportId) {
        Boolean hidden = true;
        Boolean isReported = false;
        Reports report = getReportById(reportId);
        List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
        for (Expense exp : expenseList) {
            exp.setIsReported(isReported);
            exp.setReports(null);
            exp.setReportTitle(null);
            expenseRepository.save(exp);
        }
        report.setIsHidden(hidden);
        reportsRepository.save(report);
    }

    @Override
    public List<Reports> getReportsInDateRange(LocalDate startDate, LocalDate endDate, String request) {

        switch (request) {
            case "approved":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceapprovalstatus(startDate, endDate,
                        FinanceApprovalStatus.APPROVED);
            case "pending":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceapprovalstatus(startDate, endDate,
                        FinanceApprovalStatus.PENDING);
            case "reimbursed":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceapprovalstatus(startDate, endDate,
                        FinanceApprovalStatus.REIMBURSED);
            case "rejected":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceapprovalstatus(startDate, endDate,
                        FinanceApprovalStatus.REJECTED);
            default:
                throw new IllegalArgumentException(CONSTANT6);

        }

    }


    @Override
    public List<Reports> getReportsSubmittedToUserInDateRange(String managerEmail, LocalDate startDate,
                                                              LocalDate endDate, String request) {
        switch (request) {
            case CONSTANT5:
                return reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, startDate, endDate, ManagerApprovalStatus.APPROVED, true, false);
            case CONSTANT4:
                return reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, startDate, endDate, ManagerApprovalStatus.REJECTED, true, false);
            case CONSTANT7:
                return reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, startDate, endDate, ManagerApprovalStatus.PENDING, true, false);

            default:
                throw new IllegalArgumentException(CONSTANT6);
        }
    }

    @Override
    public String getAmountOfReportsInDateRange(LocalDate startDate, LocalDate endDate) {
        List<Reports> reports = reportsRepository.findByDateSubmittedBetween(startDate, endDate);
        float total = 0;
        for (Reports report2 : reports) {
            total += report2.getTotalAmountINR();
        }
        return (total + " INR");
    }

    @Override
    public void updateExpenseStatus(Long reportId, List<Long> approveExpenseIds, List<Long> rejectExpenseIds,
                                    Map<Long, Float> partiallyApprovedMap, String reviewTime, String comments,
                                    HttpServletResponse response) throws MessagingException, IOException {
        Reports report = getReportById(reportId);
        if (Boolean.TRUE.equals(report.getIsHidden())) {
            throw new IllegalStateException(CONSTANT2 + reportId + CONSTANT1);
        } else if (report == null) {
            throw new NullPointerException(CONSTANT2 + reportId + " does not contain any expenses!");
        } else if (!report.getIsSubmitted()) {
            throw new IllegalStateException(CONSTANT2 + reportId + " is not submitted!");
        }
        LocalDate managerActionDate = LocalDate.now();

        for (Long expenseId : approveExpenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);
            if (expense.getIsHidden()) {
                throw new IllegalStateException(CONSTANT3 + expenseId + CONSTANT1);
            }
            if (expense.getIsReported() &&
                    expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.PENDING) {
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
                expense.setAmountApproved(Float.valueOf(expense.getAmount()));
                //Setting Approved Amount INR
                String curr = expense.getCurrency();
                String date = expense.getDate().toString();
                double rate = currencyExchange.getExchangeRate(curr, date);
                double approvedAmountInInr = expense.getAmountApproved() * rate;
                expense.setAmountApprovedINR(approvedAmountInInr);
                expenseRepository.save(expense);
            }
        }
        for (Long expenseId : rejectExpenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);
            if (expense.getIsHidden()) {
                throw new IllegalStateException(CONSTANT3 + expenseId + CONSTANT1);
            }
            if (expense.getIsReported() &&
                    expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.PENDING) {
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.REJECTED);
                expense.setAmountApproved(0F);
                expenseRepository.save(expense);
            }
        }
        for (Map.Entry<Long, Float> entry : partiallyApprovedMap.entrySet()) {
            Long expId = entry.getKey();
            Float amt = entry.getValue();
            Expense expense = expenseServices.getExpenseById(expId);
            expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PARTIALLY_APPROVED);
            expense.setAmountApproved(amt);
            expenseRepository.save(expense);

            String curr = expense.getCurrency();
            String date = expense.getDate().toString();
            double rate = currencyExchange.getExchangeRate(curr, date);
            double approvedAmountInInr = expense.getAmountApproved() * rate;
            expense.setAmountApprovedINR(approvedAmountInInr);
            expenseRepository.save(expense);
        }
        report.setManagerReviewTime(reviewTime);
        report.setManagerActionDate(managerActionDate);
        report.setManagerComments(comments);

        //If all the expenses are approved then report status will be "APPROVED"
        if (rejectExpenseIds.isEmpty() && partiallyApprovedMap.isEmpty()) {
            report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
            report.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
            //Email Notification
            emailService.userApprovedNotification(reportId, approveExpenseIds,response);
            emailService.financeNotification(reportId, approveExpenseIds, response);

            //Push Notification to Employee
            Long employeeId = report.getEmployeeId();
            Employee employee = employeeServices.getEmployeeById(employeeId);

            PushNotificationRequest notificationRequest = new PushNotificationRequest();
            notificationRequest.setTitle("[APPROVED]: " + report.getReportTitle());
            notificationRequest.setMessage(employee.getManagerName() + " approved your expense report.");
            notificationRequest.setToken(employee.getToken());
            System.out.println("TOKEN-" + employee.getToken());
            pushNotificationService.sendPushNotificationToToken(notificationRequest);

        }
        //If any of the expenses are rejected then report status will be "REJECTED"
        else if (!rejectExpenseIds.isEmpty() && partiallyApprovedMap.isEmpty()) {
            report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);
            report.setIsSubmitted(false);
            //Email Notification
            emailService.userRejectedNotification(reportId, rejectExpenseIds,response);
            //Push Notification to Employee
            Long employeeId = report.getEmployeeId();
            Employee employee = employeeServices.getEmployeeById(employeeId);

            PushNotificationRequest notificationRequest = new PushNotificationRequest();
            notificationRequest.setTitle("[REJECTED]: " + report.getReportTitle());
            notificationRequest.setMessage(employee.getManagerName() + " rejected your expense report.");
            notificationRequest.setToken(employee.getToken());
            System.out.println("TOKEN-" + employee.getToken());
            pushNotificationService.sendPushNotificationToToken(notificationRequest);
        }

        //If any of the expenses are partially-approved then report status will be "PARTIALLY_APPROVED"
        else if (rejectExpenseIds.isEmpty() && !partiallyApprovedMap.isEmpty()) {
            report.setManagerApprovalStatus(ManagerApprovalStatus.PARTIALLY_APPROVED);
            report.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
            //Email Notification
            emailService.userPartialApprovedExpensesNotification(reportId);
            //Push Notification to Employee
            Long employeeId = report.getEmployeeId();
            Employee employee = employeeServices.getEmployeeById(employeeId);

            PushNotificationRequest notificationRequest = new PushNotificationRequest();
            notificationRequest.setTitle("[PARTIALLY APPROVED]: " + report.getReportTitle());
            notificationRequest.setMessage(employee.getManagerName() + " partially approved your expense report.");
            notificationRequest.setToken(employee.getToken());
            System.out.println("TOKEN-" + employee.getToken());
            pushNotificationService.sendPushNotificationToToken(notificationRequest);

        }
        report.setTotalApprovedAmountCurrency(totalApprovedAmountCurrency(reportId));
        report.setTotalApprovedAmountINR(totalApprovedAmountINR(reportId));
        reportsRepository.save(report);
    }


    @Override
    public void notifyHR(Long reportId) throws MessagingException {
        Reports report = getReportById(reportId);
        Long employeeId = report.getEmployeeId();
        Employee employee = employeeServices.getEmployeeById(employeeId);
        String hrEmail = employee.getHrEmail();
        String hrName = employee.getHrName();
        emailService.notifyHr(reportId, hrEmail, hrName);
    }


    @Scheduled(cron = "0 0 12 22,24 * ?")
    public void sendReportNotApprovedByManagerReminder() {
        LocalDate currentDate = LocalDate.now();

        List<Reports> reportsList = reportsRepository.findBymanagerapprovalstatus(ManagerApprovalStatus.PENDING);
        List<Long> reportIds = new ArrayList<>();
        for (Reports report : reportsList) {

            if (currentDate.getDayOfMonth() == 22 || currentDate.getDayOfMonth() == 24) {
                reportIds.add(report.getReportId());
            }
        }

        emailService.reminderMailToManager(reportIds);

        // Push Notification Functionality
        for (Long report : reportIds) {
            Reports re = getReportById(report);
            String managerEmail = re.getManagerEmail();
            Employee manager = employeeServices.getEmployeeByEmail(managerEmail);
            PushNotificationRequest notificationRequest = new PushNotificationRequest();
            notificationRequest.setTitle("[REMINDER]: Take Action on pending reports.");
            notificationRequest.setToken(manager.getToken());
            System.out.println("TOKEN-" + manager.getToken());

            pushNotificationService.sendPushNotificationToToken(notificationRequest);
        }
    }
}

