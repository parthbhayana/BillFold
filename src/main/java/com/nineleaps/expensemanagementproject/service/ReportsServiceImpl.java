package com.nineleaps.expensemanagementproject.service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.FinanceApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.ManagerApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.Reports;
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
        Employee emp = employeeServices.getEmployeeDetailsById(employeeId);
        String employeeEmail = emp.getEmployeeEmail();
        newReport.setEmployeeMail(employeeEmail);
        LocalDate today = LocalDate.now();
        newReport.setDateCreated(today);
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
    public List<Reports> editReport(Long reportId, String reportTitle, String reportDescription, List<Long> addExpenseIds, List<Long> removeExpenseIds) {
        Long empId = null;
        Reports report = getReportById(reportId);
        if (report == null || report.getIsHidden()) {
            throw new NullPointerException("Report with ID " + reportId + " does not exist!");
        }
        if (report != null && !report.getIsHidden()) {
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
                    throw new IllegalStateException("Expense with ID " + expenseid + " is already reported in another report!");
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
        re.setTotalAmountINR(totalamountINR(reportId));
        re.setTotalAmountCurrency(totalamountCurrency(reportId));
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
                throw new IllegalStateException("Expense with ID " + expenseid + " is already reported in another report!");
            }
            if (report != null && !expense.getIsReported()) {
                expenseServices.updateExpense(reportId, expenseid);
            }
        }
        Reports re = getReportById(reportId);
        re.setTotalAmountINR(totalamountINR(reportId));
        re.setTotalAmountCurrency(totalamountCurrency(reportId));
        return reportsRepository.save(re);
    }

    @Override
    public List<Reports> getReportByEmpId(Long employeeId, String request) {
        switch (request) {
            case "drafts":
                return reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(employeeId, false, false);
            case "submitted":
                return reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.PENDING, true, false);
            case "rejected":
                return reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.REJECTED, true, false);
            case "approved":
                return reportsRepository.getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.APPROVED, true, false);
            default:
                throw new IllegalArgumentException("Enter a valid request !" + request);
        }
    }

    @Override
    public List<Reports> getReportsSubmittedToUser(String managerEmail, String request) {
        switch (request) {
            case "approved":
                return reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.APPROVED, true, false);
            case "rejected":
                return reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.REJECTED, true, false);
            case "pending":
                return reportsRepository.findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.PENDING, true, false);
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
                return reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REIMBURSED, true, false);
            case "rejected":
                return reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REJECTED, true, false);
            case "pending":
                return reportsRepository.findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.PENDING, true, false);
            default:
                throw new IllegalArgumentException("Enter a valid request !");
        }
    }


    @Override
    public void submitReport(Long reportId, String managerMail,HttpServletResponse response) throws MessagingException ,FileNotFoundException,IOException {
        boolean submissionStatus = true;
        ManagerApprovalStatus pending = ManagerApprovalStatus.PENDING;
        Reports re = getReportById(reportId);
        if (re == null || re.getIsHidden()) {
            throw new NullPointerException("Report with ID " + reportId + " does not exist!");
        }
        if (re != null && re.getIsSubmitted() == submissionStatus) {
            throw new IllegalStateException("Report with ID " + reportId + " is already submitted!");
        }
        if (re != null && re.getIsSubmitted() != submissionStatus) {
            re.setIsSubmitted(submissionStatus);
            re.setManagerapprovalstatus(pending);
            LocalDate today = LocalDate.now();
            re.setDateSubmitted(today);
            re.setTotalAmountINR(totalamountINR(reportId));
            re.setTotalAmountCurrency(totalamountCurrency(reportId));
            re.setManagerEmail(managerMail);
            reportsRepository.save(re);
            try {
                String decodedEmail = URLDecoder.decode(managerMail, "UTF-8");
                decodedEmail = decodedEmail.replace("=", "");
                re.setManagerEmail(decodedEmail);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Error decoding email: " + e.getMessage(), e);
            }
            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerKey, headerValue);
            try {
                pdfGeneratorService.export(reportId, response);
            } catch (IOException e) {

                e.printStackTrace();
            }
            emailService.managerNotification(reportId);
        }
    }

    @Override
    public void approveReportByManager(Long reportId, String comments) {
        ManagerApprovalStatus approvalStatus = ManagerApprovalStatus.APPROVED;
        FinanceApprovalStatus pending = FinanceApprovalStatus.PENDING;
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
            re.setFinanceapprovalstatus(pending);
            LocalDate today = LocalDate.now();
            re.setManagerActionDate(today);
            reportsRepository.save(re);
            emailService.userApprovedNotification(reportId);
        }
    }

    @Override
    public void rejectReportByManager(Long reportId, String comments) {
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
            LocalDate today = LocalDate.now();
            re.setManagerActionDate(today);
            reportsRepository.save(re);
            emailService.userRejectedNotification(reportId);
        }
    }

    @Override
    public void approveReportByFinance(Long reportId, String comments) {
        FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.REIMBURSED;
        Reports re = getReportById(reportId);
        if (!re.getIsSubmitted()) {
            throw new IllegalStateException("Report " + reportId + " is not Submitted!");
        }
        if (re == null || re.getIsHidden()) {
            throw new ObjectNotFoundException(reportId, "Report " + reportId + " does not exist!");
        }
        if (re != null && !re.getIsHidden() && re.getIsSubmitted() && re.getManagerapprovalstatus() == ManagerApprovalStatus.APPROVED) {
            re.setFinanceapprovalstatus(approvalStatus);
            re.setFinanceComments(comments);
            LocalDate today = LocalDate.now();
            re.setFinanceActionDate(today);
            reportsRepository.save(re);
            emailService.financeReimbursedNotification(reportId);
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
        if (re != null && !re.getIsHidden() && re.getIsSubmitted() && re.getManagerapprovalstatus() == ManagerApprovalStatus.APPROVED) {
            re.setFinanceapprovalstatus(approvalStatus);
            re.setFinanceComments(comments);
            LocalDate today = LocalDate.now();
            re.setFinanceActionDate(today);
            reportsRepository.save(re);
            emailService.financeRejectedNotification(reportId);
        }
    }

    @Override
    public float totalamountINR(Long reportId) {
        Reports report = reportsRepository.findById(reportId).get();
        List<Expense> expenses = expenseRepository.findByReports(report);

        float amtINR = 0;
        for (Expense expense2 : expenses) {
            amtINR += expense2.getAmountINR();
        }
        return amtINR;
    }

    @Override
    public float totalamountCurrency(Long reportId) {
        Reports report = reportsRepository.findById(reportId).get();
        List<Expense> expenses = expenseRepository.findByReports(report);

        float amtCurrency = 0;
        for (Expense expense2 : expenses) {
            amtCurrency += expense2.getAmount();
        }
        return amtCurrency;
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
    public List<Reports> getReportsSubmittedToUserInDateRange(String managerEmail, LocalDate startDate, LocalDate endDate) {
        return reportsRepository.findByManagerEmailAndDateSubmittedBetween(managerEmail, startDate, endDate);
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

}