package com.nineleaps.expensemanagementproject.service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import com.nineleaps.expensemanagementproject.entity.*;
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
    private IPdfGeneratorService pdfGeneratorService;

    @Autowired
    private ICurrencyExchange CurrencyExchange;


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
    public Reports addReport(Reports newReport, Long employeeId, List<Long> expenseids) {
        Employee employee = employeeServices.getEmployeeById(employeeId);
        String employeeEmail = employee.getEmployeeEmail();
        String managerEmail = employee.getManagerEmail();
        String employeeName = (employee.getFirstName() + " " + employee.getLastName());
        String officialEmployeeId = employee.getOfficialEmployeeId();
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
//        float approvedAmountCurrency = 0;
//        for (Expense expense2 : expp) {
//            approvedAmountCurrency += expense2.getAmountApproved();
//        }
//        System.out.println("@@@@@@@@@@@@@@" + approvedAmountCurrency + "!!");
//        newReport.setTotalApprovedAmountCurrency(approvedAmountCurrency);
//        float approvedAmountINR = 0;
//        for (Expense expense2 : expp) {
//            approvedAmountINR += expense2.getAmountApprovedINR();
//        }
//        System.out.println("@@@@@@@@@@@@@@" + approvedAmountINR + "!!");
//        newReport.setTotalApprovedAmountINR(approvedAmountINR);

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
            throw new IllegalStateException("Can not edit Report with ReportId:" + reportId + " as it is already submitted!");
        }
        if (report == null || report.getIsHidden()) {
            throw new NullPointerException("Report with ID " + reportId + " does not exist!");
        }
        if ((report != null && !report.getIsHidden()) || (report.getIsSubmitted() && report.getManagerapprovalstatus() == ManagerApprovalStatus.ACTION_REQUIRED)) {
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
                    throw new IllegalStateException(
                            "Expense with ID " + expenseid + " is already reported in another report!");
                }
                if (report != null && !expense.getIsReported()) {
                    expenseServices.updateExpense(reportId, expenseid);
                }
            }
            // Removing Expenses
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
        if (report == null || report.getIsHidden()) {
            throw new NullPointerException("Report with ID " + reportId + " does not exist!");
        }
        for (Long expenseid : expenseids) {
            Expense expense = expenseServices.getExpenseById(expenseid);
            if (expense.getIsReported()) {
                throw new IllegalStateException(
                        "Expense with ID " + expenseid + " is already reported in another report!");
            }
            if (report != null && !expense.getIsReported()) {
                expenseServices.updateExpense(reportId, expenseid);
            }
        }
        Reports re = getReportById(reportId);
        re.setTotalAmountINR(totalAmountINR(reportId));
        re.setTotalAmountCurrency(totalAmountCurrency(reportId));
        re.setTotalApprovedAmountINR(totalApprovedAmountINR(reportId));
        re.setTotalApprovedAmountCurrency(totalApprovedAmountCurrency(reportId));
        return reportsRepository.save(re);
    }

    @Override
    public List<Reports> getReportByEmpId(Long employeeId, String request) {
        switch (request) {
            case "drafts":
                return reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(employeeId, false, false);
            case "submitted":
                return reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId,
                        ManagerApprovalStatus.PENDING, true, false);
            case "rejected":
                return reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId,
                        ManagerApprovalStatus.REJECTED, true, false);
            case "approved":
                List<Reports> approvedList = reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId,
                        ManagerApprovalStatus.APPROVED, true, false);
                List<Reports> partiallyApprovedList = reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId,
                        ManagerApprovalStatus.PARTIALLY_APPROVED, true, false);
                List<Reports> mergedList = new ArrayList<>();
                mergedList.addAll(approvedList);
                mergedList.addAll(partiallyApprovedList);
                return mergedList;
            default:
                throw new IllegalArgumentException("Enter a valid request !" + request);
        }
    }

    @Override
    public List<Reports> getReportsSubmittedToUser(String managerEmail, String request) {
        switch (request) {
            case "approved":
                List<Reports> approvedList = reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail,
                        ManagerApprovalStatus.APPROVED, true, false);
                List<Reports> partiallyApprovedList = reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail,
                        ManagerApprovalStatus.PARTIALLY_APPROVED, true, false);
                List<Reports> mergedList = new ArrayList<>();
                mergedList.addAll(approvedList);
                mergedList.addAll(partiallyApprovedList);
                return mergedList;
            case "rejected":
                return reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail,
                        ManagerApprovalStatus.REJECTED, true, false);
            case "pending":
                return reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail,
                        ManagerApprovalStatus.PENDING, true, false);
            default:
                throw new IllegalArgumentException("Enter a valid request !");
        }
    }

    @Override
    public List<Reports> getAllSubmittedReports() {
        List<Reports> Reports = reportsRepository.findAll();
        List<Reports> submitttedReports = new ArrayList<>();
        for (Reports reports2 : Reports) {
            if (reports2.getIsSubmitted()) {
                submitttedReports.add(reports2);
            }
        }
        return submitttedReports;
    }

    @Override
    public List<Reports> getAllReportsApprovedByManager(String request) {
        switch (request) {
            case "approved":
                return reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                        ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REIMBURSED, true, false);
            case "rejected":
                return reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                        ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REJECTED, true, false);
            case "pending":
                List<Reports> approvedList = reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                        ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.PENDING, true, false);
                List<Reports> partiallyApprovedList = reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
                        ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.PENDING, true, false);
                List<Reports> mergedList = new ArrayList<>();
                mergedList.addAll(approvedList);
                mergedList.addAll(partiallyApprovedList);
                return mergedList;
            default:
                throw new IllegalArgumentException("Enter a valid request !");
        }
    }

    @Override
    public void submitReport(Long reportId, HttpServletResponse response)
            throws MessagingException, FileNotFoundException, IOException {
        boolean submissionStatus = true;
        Reports re = getReportById(reportId);
        if (re == null || re.getIsHidden()) {
            throw new NullPointerException("Report with ID " + reportId + " does not exist!");
        }
        if (re != null && re.getIsSubmitted() == submissionStatus && re.getManagerapprovalstatus() != ManagerApprovalStatus.REJECTED) {
            throw new IllegalStateException("Report with ID " + reportId + " is already submitted!");
        }
        if ((re != null && re.getIsSubmitted() != submissionStatus) || (re != null && re.getIsSubmitted() == true && re.getManagerapprovalstatus() == ManagerApprovalStatus.REJECTED)) {
            re.setIsSubmitted(submissionStatus);
            re.setManagerapprovalstatus(ManagerApprovalStatus.PENDING);
            re.setDateSubmitted(LocalDate.now());
            re.setTotalAmountINR(totalAmountINR(reportId));
            re.setTotalAmountCurrency(totalAmountCurrency(reportId));
//            re.setTotalApprovedAmountINR(totalApprovedAmountINR(reportId));
//            re.setTotalApprovedAmountCurrency(totalApprovedAmountCurrency(reportId));
            // Fetching employee ID
            Long employeeId = re.getEmployeeId();
            Employee employee = employeeServices.getEmployeeById(employeeId);
            re.setManagerEmail(employee.getManagerEmail());
            reportsRepository.save(re);
            if (employee.getManagerEmail() == null) {
                throw new NullPointerException("Manager Email not found for Employee ID: " + employee.getEmployeeId());
            }
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            ArrayList<Long> expenseIds = new ArrayList<>();
            for (Expense expense : expenseList) {
                expenseIds.add(expense.getExpenseId());
            }
            emailService.managerNotification(reportId, expenseIds, response);
        }

    }

    @Override
    public void approveReportByManager(Long reportId, String comments, HttpServletResponse response) throws MessagingException, IOException {
        Reports re = getReportById(reportId);
        if (!re.getIsSubmitted()) {
            throw new IllegalStateException("Report " + reportId + " is not Submitted!");
        }
        if (re == null || re.getIsHidden()) {
            throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
        }
        if (re != null && !re.getIsHidden() && re.getIsSubmitted()) {
            re.setManagerapprovalstatus(ManagerApprovalStatus.APPROVED);
            re.setManagerComments(comments);
            re.setFinanceapprovalstatus(FinanceApprovalStatus.PENDING);
            re.setManagerActionDate(LocalDate.now());
            reportsRepository.save(re);
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            List<Long> expenseIds = new ArrayList<>();
            for (Expense expense : expenseList) {
                expenseIds.add(expense.getExpenseId());
            }
            emailService.userApprovedNotification(reportId, expenseIds, response);
            emailService.financeNotificationToReimburse(reportId, expenseIds, response);
        }
        //Update Expenses Status
        List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
        for (Expense exp : expenseList) {
            exp.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
            expenseRepository.save(exp);
        }

    }

    @Override
    public void rejectReportByManager(Long reportId, String comments, HttpServletResponse response) throws MessagingException, IOException {
        ManagerApprovalStatus approvalStatus = ManagerApprovalStatus.REJECTED;
        Reports re = getReportById(reportId);
        if (!re.getIsSubmitted()) {
            throw new IllegalStateException("Report " + reportId + " is not Submitted!");
        }
        if (re == null || re.getIsHidden()) {
            throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
        }
        if (re != null && !re.getIsHidden() && re.getIsSubmitted()) {
            re.setManagerapprovalstatus(approvalStatus);
            re.setManagerComments(comments);
            re.setManagerActionDate(LocalDate.now());
            reportsRepository.save(re);
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            List<Long> expenseIds = new ArrayList<>();
            for (Expense expense : expenseList) {
                expenseIds.add(expense.getExpenseId());
            }
            emailService.userRejectedNotification(reportId, expenseIds, response);
        }
        //Update Expenses Status
        List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
        for (Expense exp : expenseList) {
            exp.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.REJECTED);
            expenseRepository.save(exp);
        }

    }

    @Override
    public void reimburseReportByFinance(ArrayList<Long> reportIds, String comments) {
        FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.REIMBURSED;
        for (Long reportId : reportIds) {
            Reports re = getReportById(reportId);
            if (re == null || re.getIsHidden()) {
                throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
            }
            if (!re.getIsSubmitted()) {
                throw new IllegalStateException("Report " + reportId + " is not submitted!");
            }

            if (re.getManagerapprovalstatus() != ManagerApprovalStatus.APPROVED) {
                throw new IllegalStateException("Report " + reportId + " is not approved by the manager!");
            }
            re.setFinanceapprovalstatus(approvalStatus);
            re.setFinanceComments(comments);
            re.setFinanceActionDate(LocalDate.now());
            reportsRepository.save(re);
            emailService.financeReimbursedNotification(reportId);
            //Update Expenses Status
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            for (Expense exp : expenseList) {
                exp.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
                expenseRepository.save(exp);
            }
        }
    }

    @Override
    public void rejectReportByFinance(Long reportId, String comments) {
        FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.REJECTED;
        Reports re = getReportById(reportId);
        if (!re.getIsSubmitted()) {
            throw new IllegalStateException("Report " + reportId + " is not Submitted!");
        }
        if (re == null || re.getIsHidden()) {
            throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
        }
        if (re != null && !re.getIsHidden() && re.getIsSubmitted()
                && re.getManagerapprovalstatus() == ManagerApprovalStatus.APPROVED) {
            re.setFinanceapprovalstatus(approvalStatus);
            re.setFinanceComments(comments);
            re.setFinanceActionDate(LocalDate.now());
            reportsRepository.save(re);
            emailService.financeRejectedNotification(reportId);
            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            for (Expense exp : expenseList) {
                exp.setFinanceApprovalStatus(FinanceApprovalStatus.REJECTED);
                expenseRepository.save(exp);
            }
        }
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
//        Reports report = getReportById(reportId);
//        List<Expense> expenseList = expenseRepository
//                .findExpenseByReportsAndIsReportedAndIsHidden(report, true, false);
//        float totalApprovedAmount = 0;
//        for (Expense expense2 : expenseList) {
//            totalApprovedAmount += expense2.getAmountApproved();
//        }
//        return totalApprovedAmount;
       return 0;
    }


    @Override
    public float totalApprovedAmountINR(Long reportId) {
//        Reports report = getReportById(reportId);
//        List<Expense> expenseList = expenseRepository
//                .findExpenseByReportsAndIsReportedAndIsHidden(report, true, false);
//        float totalApprovedAmount = 0;
//        for (Expense expense2 : expenseList) {
//            totalApprovedAmount += expense2.getAmountApprovedINR();
//        }
//        return totalApprovedAmount;
        return 0;
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
    public List<Reports> getReportsInDateRange(LocalDate startDate, LocalDate endDate) {
        return reportsRepository.findByDateSubmittedBetween(startDate, endDate);
    }

    @Override
    public List<Reports> getReportsSubmittedToUserInDateRange(String managerEmail, LocalDate startDate,
                                                              LocalDate endDate, String request) {
        switch (request) {
            case "approved":
                return reportsRepository
                        .findByManagerEmailAndDateSubmittedBetweenAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                                managerEmail, startDate, endDate, ManagerApprovalStatus.APPROVED, true, false);
            case "rejected":
                return reportsRepository
                        .findByManagerEmailAndDateSubmittedBetweenAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                                managerEmail, startDate, endDate, ManagerApprovalStatus.REJECTED, true, false);
            case "pending":
                return reportsRepository
                        .findByManagerEmailAndDateSubmittedBetweenAndManagerapprovalstatusAndIsSubmittedAndIsHidden(
                                managerEmail, startDate, endDate, ManagerApprovalStatus.PENDING, true, false);
            default:
                throw new IllegalArgumentException("Enter a valid request !");
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
    public void updateExpenseStatus(Long reportId, List<Long> approveExpenseIds, List<Long> rejectExpenseIds, Map<Long, Float> partiallyApprovedMap, String reviewTime) {
        Reports report = getReportById(reportId);
        if (report.getIsHidden()) {
            throw new IllegalStateException("Report with ID " + reportId + " does not exist!");
        }
        if (report == null) {
            throw new NullPointerException("Report with ID " + reportId + " does not contain any expenses!");
        }
        if (!report.getIsSubmitted()) {
            throw new IllegalStateException("Report with ID " + reportId + " is not submitted!");
        }
        for (Long expenseId : approveExpenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);
            if (expense.getIsHidden()) {
                throw new IllegalStateException(
                        "Expense with ID " + expenseId + " does not exist!");
            }
            if (expense.getIsReported() && expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.PENDING) {
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
                expense.setAmountApproved(Float.valueOf(expense.getAmount()));
                //Setting Approved Amount INR
                String curr = expense.getCurrency();
                String date = expense.getDate().toString();
                double rate = CurrencyExchange.getExchangeRate(curr, date);
                System.out.println("Exchange Rate = " + rate);
                double approvedAmountInInr = expense.getAmountApproved() * rate;
                expense.setAmountApprovedINR(approvedAmountInInr);
                expenseRepository.save(expense);
            }
        }
        for (Long expenseId : rejectExpenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);
            if (expense.getIsHidden()) {
                throw new IllegalStateException(
                        "Expense with ID " + expenseId + " does not exist!");
            }
            if (expense.getIsReported() && expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.PENDING) {
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
            //Setting Approved Amount INR
            String curr = expense.getCurrency();
            String date = expense.getDate().toString();
            double rate = CurrencyExchange.getExchangeRate(curr, date);
            System.out.println("Exchange Rate = " + rate);
            double approvedAmountInInr = expense.getAmountApproved() * rate;
            expense.setAmountApprovedINR(approvedAmountInInr);
            expenseRepository.save(expense);
        }
        report.setManagerReviewTime(reviewTime);
        //Changing Report Status
        //If any expense is rejected report will go back to employee for changes
        if (!rejectExpenseIds.isEmpty()) {
            report.setManagerapprovalstatus(ManagerApprovalStatus.REJECTED);
            report.setIsSubmitted(false);
//            emailService.userRejectedNotification(reportId);
        }
        //If all the expenses are approved then report status will be "APPROVED"
        if (rejectExpenseIds.isEmpty() && partiallyApprovedMap.isEmpty()) {
            report.setManagerapprovalstatus(ManagerApprovalStatus.APPROVED);
            report.setFinanceapprovalstatus(FinanceApprovalStatus.PENDING);
//            emailService.userApprovedNotification(reportId);
        }
        //If any of the expenses are partially-approved then report status will be "PARTIALLY_APPROVED"
        if (rejectExpenseIds.isEmpty() && !partiallyApprovedMap.isEmpty()) {
            report.setManagerapprovalstatus(ManagerApprovalStatus.PARTIALLY_APPROVED);
            report.setFinanceapprovalstatus(FinanceApprovalStatus.PENDING);
            emailService.userPartialApprovedExpensesNotification(reportId);
        }
        reportsRepository.save(report);
    }



    @Scheduled(cron = "0 20 13 * * *")
    public void sendReportNotApprovedByManagerReminder() {
        LocalDate currentDate = LocalDate.now();

        List<Reports> reportsList = reportsRepository.findBymanagerapprovalstatus(ManagerApprovalStatus.PENDING);
        List<Long> reportIds = new ArrayList<>();
        for (Reports report : reportsList) {
            LocalDate submissionDate = report.getDateSubmitted();
            LocalDate expirationDate = submissionDate.plusDays(60);

            if (currentDate.isAfter(expirationDate.minusDays(5)) && currentDate.isBefore(expirationDate)) {
                reportIds.add(report.getReportId());
            }
        }
        emailService.reminderMailToManager(reportIds);
    }
}

