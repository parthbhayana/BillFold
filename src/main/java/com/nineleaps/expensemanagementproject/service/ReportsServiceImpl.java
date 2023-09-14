package com.nineleaps.expensemanagementproject.service;

import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.DTO.ReportsDTO;
import com.nineleaps.expensemanagementproject.entity.*;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationRequest;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationService;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private IExpenseService expenseServices;

    @Autowired
    private IEmployeeService employeeServices;

    @Autowired
    private IEmailService emailService;

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

    @Override
    public Set<Reports> getAllReports(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        List<Expense> expenses=expenseRepository.findByEmployeeAndIsHidden(employee,false);
        Set<Reports> reportsList = new HashSet<>();
        for (Expense expense : expenses) {
            Reports reports = expense.getReports();
            if (reports != null) {
                reportsList.add(reports);
            }
        }

        return  reportsList;
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
        newReport.setEmployeeMail(employeeEmail);
        newReport.setManagerEmail(managerEmail);
        newReport.setEmployeeName(employeeName);
        newReport.setOfficialEmployeeId(officialEmployeeId);
        newReport.setDateCreated(LocalDate.now());
        newReport.setEmployeeId(employeeId);
        reportsRepository.save(newReport);
        Long id = newReport.getReportId();
        List<Expense> expp = expenseRepository.findAllById(expenseids);
        //Setting Amounts
        float amt = 0;
        for (Expense expense2 : expp) {
            amt += expense2.getAmount();
        }
        newReport.setTotalAmount(amt);
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
        if (report.getIsSubmitted() && report.getManagerApprovalStatus() == ManagerApprovalStatus.PENDING) {
            throw new IllegalStateException(
                    "Can not edit Report with ReportId:" + reportId + " as it is already submitted!");
        } else if (report.getIsHidden()) {
            throw new NullPointerException(CONSTANT2 + reportId + CONSTANT1);
        } else if (!report.getIsHidden() ||
                report.getIsSubmitted() && report.getManagerApprovalStatus() == ManagerApprovalStatus.REJECTED) {
            report.setReportTitle(reportTitle);
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
        re.setTotalAmount(totalAmount(reportId));
        re.setTotalApprovedAmount(totalApprovedAmount(reportId));
        return getReportByEmpId(empId, "drafts");
    }

    @Override
    public Reports addExpenseToReport(Long reportId, List<Long> expenseids) {
        Reports report = getReportById(reportId);
        Long count= 0L;
        if (report.getIsHidden()) {
            throw new NullPointerException(CONSTANT2 + reportId + CONSTANT1);
        }
        for (Long expenseid : expenseids) {
            Expense expense = expenseServices.getExpenseById(expenseid);
            if (expense.getIsReported()) {
                throw new IllegalStateException(CONSTANT3 + expenseid + " is already reported in another report!");
            }
            if (!expense.getIsReported()) {
                count++;
                System.out.println(count+"hihihihihihi");
                Reports rep = getReportById(reportId);
                rep.setExpensesCount(count);
                rep.setTotalAmount(totalAmount(reportId));

                expenseServices.updateExpense(reportId, expenseid);
            }
        }
        Reports re = getReportById(reportId);
        re.setExpensesCount(count);
        re.setTotalAmount(totalAmount(reportId));
        return reportsRepository.save(re);
    }

    @Override
    public List<Reports> getReportByEmpId(Long employeeId, String request) {
        switch (request) {
            case "drafts":
                return reportsRepository.getReportsByEmployeeIdAndIsSubmittedAndIsHidden(employeeId, false, false);
            case "submitted":
                return reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.PENDING, true, false);
            case CONSTANT4:
                return reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(employeeId,
                        ManagerApprovalStatus.REJECTED, false);
            case CONSTANT5:
                List<Reports> approvedList =
                        reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.APPROVED, true, false);
                List<Reports> partiallyApprovedList =
                        reportsRepository.getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(employeeId, ManagerApprovalStatus.PARTIALLY_APPROVED, true, false);
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
                        reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.APPROVED, true, false);
                List<Reports> partiallyApprovedList =
                        reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.PARTIALLY_APPROVED, true, false);
                List<Reports> mergedList = new ArrayList<>();
                mergedList.addAll(approvedList);
                mergedList.addAll(partiallyApprovedList);
                return mergedList;
            case CONSTANT4:
                return reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsHidden(managerEmail,
                        ManagerApprovalStatus.REJECTED, false);
            case CONSTANT7:
                return reportsRepository.findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(managerEmail, ManagerApprovalStatus.PENDING, true, false);
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
                        reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.APPROVED, true, false);
                List<Reports> partiallyApprovedList1 =
                        reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.APPROVED, true, false);
                List<Reports> mergedList1 = new ArrayList<>();
                mergedList1.addAll(approvedList1);
                mergedList1.addAll(partiallyApprovedList1);
                return mergedList1;
            case CONSTANT4:
                List<Reports> approvedList2 =
                        reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REJECTED, true, false);
                List<Reports> partiallyApprovedList2 =
                        reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.REJECTED, true, false);
                List<Reports> mergedList2 = new ArrayList<>();
                mergedList2.addAll(approvedList2);
                mergedList2.addAll(partiallyApprovedList2);
                return mergedList2;


            case CONSTANT7:
                List<Reports> approvedList3 =
                        reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.PENDING, true, false);
                List<Reports> partiallyApprovedList3 =
                        reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.PENDING, true, false);
                List<Reports> mergedList3 = new ArrayList<>();
                mergedList3.addAll(approvedList3);
                mergedList3.addAll(partiallyApprovedList3);
                return mergedList3;
            case "reimbursed":
                List<Reports> approvedList4 =
                        reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.APPROVED, FinanceApprovalStatus.REIMBURSED, true, false);
                List<Reports> partiallyApprovedList4 =
                        reportsRepository.findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(ManagerApprovalStatus.PARTIALLY_APPROVED, FinanceApprovalStatus.REIMBURSED, true, false);
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
        if (re.getIsSubmitted() && re.getManagerApprovalStatus() != ManagerApprovalStatus.REJECTED) {
            throw new IllegalStateException(CONSTANT2 + reportId + " is already submitted!");
        }

        // Check if the report is not submitted or was previously rejected by the manager
        if (!re.getIsSubmitted() || re.getManagerApprovalStatus() == ManagerApprovalStatus.REJECTED) {
            List<Expense> rejectedExpenses = expenseServices.getRejectedExpensesByReportId(reportId);
            for (Expense expense : rejectedExpenses) {
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
                expenseRepository.save(expense);
            }
            re.setIsSubmitted(submissionStatus);
            re.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
            re.setDateSubmitted(LocalDate.now());
            re.setTotalAmount(totalAmount(reportId));

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
    public void submitReport(Long reportId, String managerEmail,
                             HttpServletResponse response) throws MessagingException, IOException {
        boolean submissionStatus = true;
        Reports re = getReportById(reportId);
        Long employeeId = re.getEmployeeId();

        if (re == null || re.getIsHidden()) {
            throw new NullPointerException(CONSTANT2 + reportId + CONSTANT1);
        }

        // Check if the report is already submitted and not rejected
        if (re.getIsSubmitted() && re.getManagerApprovalStatus() != ManagerApprovalStatus.REJECTED) {
            throw new IllegalStateException(CONSTANT2 + reportId + " is already submitted!");
        }

        // Check if the report is not submitted or was previously rejected by the manager
        if (!re.getIsSubmitted() || re.getManagerApprovalStatus() == ManagerApprovalStatus.REJECTED) {
            List<Expense> rejectedExpenses = expenseServices.getRejectedExpensesByReportId(reportId);
            for (Expense expense : rejectedExpenses) {
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
                expenseRepository.save(expense);
            }
            re.setIsSubmitted(submissionStatus);
            re.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
            re.setDateSubmitted(LocalDate.now());
            re.setTotalAmount(totalAmount(reportId));

            // Fetch the manager's email and set it in the report
            Employee employee = employeeServices.getEmployeeById(employeeId);
            String managerEmailDB = employee.getManagerEmail();
            if (managerEmailDB == null) {
                throw new NullPointerException("Manager Email not found for Employee ID: " + employee.getEmployeeId());
            }
            if (Objects.equals(managerEmail, managerEmailDB)) {
                re.setManagerEmail(managerEmailDB);
                reportsRepository.save(re);
                // Send email notification to the manager
                List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
                ArrayList<Long> expenseIds = new ArrayList<>();
                for (Expense expense : expenseList) {
                    expenseIds.add(expense.getExpenseId());
                }
                emailService.managerNotification(reportId, expenseIds, managerEmailDB, response);
                // Push Notification Functionality
                Employee manager = employeeServices.getEmployeeByEmail(managerEmailDB);
                if (manager != null && manager.getToken() != null) {
//                    Employee employee = employeeServices.getEmployeeById(employeeId);
                    PushNotificationRequest notificationRequest = new PushNotificationRequest();
                    notificationRequest.setTitle(employee.getFirstName() + " " + employee.getLastName());
                    notificationRequest.setMessage("Submitted you an expense report.");
                    notificationRequest.setToken(manager.getToken());
                    System.out.println("TOKEN-" + manager.getToken());

                    pushNotificationService.sendPushNotificationToToken(notificationRequest);
                }
            } else {
                re.setManagerEmail(managerEmail);
                reportsRepository.save(re);
                // Send email notification to the manager
                List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
                ArrayList<Long> expenseIds = new ArrayList<>();
                for (Expense expense : expenseList) {
                    expenseIds.add(expense.getExpenseId());
                }
                emailService.managerNotification(reportId, expenseIds, managerEmail, response);
                // Push Notification Functionality
                Employee manager = employeeServices.getEmployeeByEmail(managerEmail);
                if (manager != null && manager.getToken() != null) {
//                    Employee employee = employeeServices.getEmployeeById(employeeId);
                    PushNotificationRequest notificationRequest = new PushNotificationRequest();
                    notificationRequest.setTitle(employee.getFirstName() + " " + employee.getLastName());
                    notificationRequest.setMessage("Submitted you an expense report.");
                    notificationRequest.setToken(manager.getToken());
                    System.out.println("TOKEN-" + manager.getToken());

                    pushNotificationService.sendPushNotificationToToken(notificationRequest);
                }
            }
        }
    }

//    @Override
//    public void approveReportByManager(Long reportId, String comments,
//                                       HttpServletResponse response) throws MessagingException, IOException {
//        Reports re = getReportById(reportId);
//        Long employeeId = re.getEmployeeId();
//        if (!re.getIsSubmitted()) {
//            throw new IllegalStateException(CONSTANT8 + reportId + CONSTANT9);
//        }
//        if (re.getIsHidden()) {
//            throw new ObjectNotFoundException(reportId, CONSTANT8 + reportId + CONSTANT1);
//        }
//        if (!re.getIsHidden() && re.getIsSubmitted()) {
//            re.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
//            re.setManagerComments(comments);
//            re.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
//            re.setManagerActionDate(LocalDate.now());
//            reportsRepository.save(re);
//            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
//            List<Long> expenseIds = new ArrayList<>();
//            for (Expense expense : expenseList) {
//                expenseIds.add(expense.getExpenseId());
//            }
//        }
//        //Update Expenses Status
//        List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
//        for (Expense exp : expenseList) {
//            exp.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
//            expenseRepository.save(exp);
//        }
//        re.setTotalApprovedAmountINR(totalApprovedAmountINR(reportId));
//        re.setTotalApprovedAmountCurrency(totalApprovedAmountCurrency(reportId));
//        reportsRepository.save(re);
//
//        //Email Notification
//        List<Expense> expenseList1 = expenseServices.getExpenseByReportId(reportId);
//        ArrayList<Long> expenseIds = new ArrayList<>();
//        for (Expense expense : expenseList1) {
//            expenseIds.add(expense.getExpenseId());
//        }
//        emailService.financeNotification(reportId, expenseIds, response);
//        emailService.userApprovedNotification(reportId, expenseIds);
//
//        //Push Notification Functionality
//        //To Employee
//        Employee employee = employeeServices.getEmployeeById(employeeId);
//
//        PushNotificationRequest notificationRequest = new PushNotificationRequest();
//        notificationRequest.setTitle("[APPROVED]: " + re.getReportTitle());
//        notificationRequest.setMessage(employee.getManagerName() + " approved your expense report.");
//        notificationRequest.setToken(employee.getToken());
//        System.out.println("TOKEN-" + employee.getToken());
//        pushNotificationService.sendPushNotificationToToken(notificationRequest);
//
//        //To Admin ---??
//
//    }
//
//    @Override
//    public void rejectReportByManager(Long reportId, String comments,
//                                      HttpServletResponse response) throws MessagingException, IOException {
//        ManagerApprovalStatus approvalStatus = ManagerApprovalStatus.REJECTED;
//        Reports re = getReportById(reportId);
//        Long employeeId = re.getEmployeeId();
//        if (!re.getIsSubmitted()) {
//            throw new IllegalStateException(CONSTANT8 + reportId + CONSTANT9);
//        }
//        if (re.getIsHidden()) {
//            throw new ObjectNotFoundException(reportId, CONSTANT8 + reportId + CONSTANT1);
//        }
//        if (!re.getIsHidden() && re.getIsSubmitted()) {
//            re.setManagerApprovalStatus(approvalStatus);
//            re.setManagerComments(comments);
//            re.setManagerActionDate(LocalDate.now());
//            reportsRepository.save(re);
//            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
//            List<Long> expenseIds = new ArrayList<>();
//            for (Expense expense : expenseList) {
//                expenseIds.add(expense.getExpenseId());
//            }
//            emailService.userRejectedNotification(reportId, expenseIds);
//        }
//        //Update Expenses Status
//        List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
//        for (Expense exp : expenseList) {
//            exp.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.REJECTED);
//            expenseRepository.save(exp);
//        }
//        //Email Notification ------------- ???????
//
//        //Push Notification Functionality
//
//        Employee employee = employeeServices.getEmployeeById(employeeId);
//
//        PushNotificationRequest notificationRequest = new PushNotificationRequest();
//        notificationRequest.setTitle("[REJECTED]: " + re.getReportTitle());
//        notificationRequest.setMessage(employee.getManagerName() + " rejected your expense report.");
//        notificationRequest.setToken(employee.getToken());
//        System.out.println("TOKEN-" + employee.getToken());
//
//        pushNotificationService.sendPushNotificationToToken(notificationRequest);
//    }


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

            if (re.getManagerApprovalStatus() != ManagerApprovalStatus.APPROVED &&
                    re.getManagerApprovalStatus() != ManagerApprovalStatus.PARTIALLY_APPROVED) {
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
                re.getManagerApprovalStatus() == ManagerApprovalStatus.APPROVED) {
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
    public float totalAmount(Long reportId) {
        Reports report = reportsRepository.findById(reportId).get();
        List<Expense> expenses = expenseRepository.findByReports(report);

        float amtINR = 0;
        for (Expense expense2 : expenses) {
            amtINR += expense2.getAmount();
        }
        return amtINR;
    }


    @Override
    public float totalApprovedAmount(Long reportId) {
        Reports report = getReportById(reportId);
        List<Expense> expenseList = expenseRepository.findExpenseByReportsAndIsReportedAndIsHidden(report, true, false);
        float totalApprovedAmount = 0;
        for (Expense expense2 : expenseList) {
            Double amountApproved = expense2.getAmountApproved();
            if (amountApproved != null) {
                totalApprovedAmount += amountApproved.floatValue();
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
            exp.setManagerApprovalStatusExpense(null);
            exp.setAmountApproved(null);
            expenseRepository.save(exp);
        }
        report.setIsHidden(hidden);
        reportsRepository.save(report);
    }

    @Override
    public List<Reports> getReportsInDateRange(LocalDate startDate, LocalDate endDate, String request) {

        switch (request) {
            case "approved":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate,
                        FinanceApprovalStatus.APPROVED);
            case "pending":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate,
                        FinanceApprovalStatus.PENDING);
            case "reimbursed":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate,
                        FinanceApprovalStatus.REIMBURSED);
            case "rejected":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate,
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
                return reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(managerEmail, startDate, endDate, ManagerApprovalStatus.APPROVED, true, false);
            case CONSTANT4:
                return reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(managerEmail, startDate, endDate, ManagerApprovalStatus.REJECTED, true, false);
            case CONSTANT7:
                return reportsRepository.findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(managerEmail, startDate, endDate, ManagerApprovalStatus.PENDING, true, false);

            default:
                throw new IllegalArgumentException(CONSTANT6);
        }
    }

    @Override
    public String getAmountOfReportsInDateRange(LocalDate startDate, LocalDate endDate) {
        List<Reports> reports = reportsRepository.findByDateSubmittedBetween(startDate, endDate);
        float total = 0;
        for (Reports report2 : reports) {
            total += report2.getTotalAmount();
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
                expense.setAmountApproved(expense.getAmount());
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
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
                expense.setAmountApproved(0.0);
                expenseRepository.save(expense);
            }
        }
        for (Map.Entry<Long, Float> entry : partiallyApprovedMap.entrySet()) {
            Long expId = entry.getKey();
            Float amt = entry.getValue();
            Expense expense = expenseServices.getExpenseById(expId);
            expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PARTIALLY_APPROVED);
            expense.setAmountApproved(Double.valueOf(amt));
            expenseRepository.save(expense);
        }
        report.setManagerReviewTime(reviewTime);
        report.setManagerActionDate(managerActionDate);
        report.setManagerComments(comments);

        //If all the expenses are approved then report status will be "APPROVED"
        if (rejectExpenseIds.isEmpty() && partiallyApprovedMap.isEmpty()) {
            report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
            report.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
            reportsRepository.save(report);
            //Email Notification
            emailService.userApprovedNotification(reportId, approveExpenseIds, response);
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
        else if (!rejectExpenseIds.isEmpty()) {
            report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);
            reportsRepository.save(report);
            //Email Notification
            emailService.userRejectedNotification(reportId, rejectExpenseIds, response);
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
            reportsRepository.save(report);
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
        report.setTotalApprovedAmount(totalApprovedAmount(reportId));
        reportsRepository.save(report);
    }


    @Override
    public void notifyHR(Long reportId, HttpServletResponse response) throws MessagingException, IOException {
        Reports report = getReportById(reportId);
        Long employeeId = report.getEmployeeId();
        Employee employee = employeeServices.getEmployeeById(employeeId);
        String hrEmail = employee.getHrEmail();
        String hrName = employee.getHrName();
        emailService.notifyHr(reportId, hrEmail, hrName, response);

    }

    @Override
    public void notifyLnD(Long reportId, HttpServletResponse response) throws MessagingException, IOException {
        Reports report = getReportById(reportId);
        Long employeeId = report.getEmployeeId();
        Employee employee = employeeServices.getEmployeeById(employeeId);
        String lndEmail = employee.getLndEmail();
        String lndName = employee.getLndName();
        emailService.notifyLnD(reportId, lndEmail, lndName, response);
    }

    @Scheduled(cron = "0 0 12 22,24 * ?")
    public void sendReportNotApprovedByManagerReminder() {
        LocalDate currentDate = LocalDate.now();

        List<Reports> reportsList = reportsRepository.findBymanagerApprovalStatus(ManagerApprovalStatus.PENDING);
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

    @Override
    public int numberOfExpenses(Long reportId)
    {
        int count=0;
        List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
        for (Expense expense:expenseList)
        {
            count++;
        }
        return count;

    }
}

