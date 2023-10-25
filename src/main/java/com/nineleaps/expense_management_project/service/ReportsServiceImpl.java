package com.nineleaps.expense_management_project.service;

import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expense_management_project.dto.ReportsDTO;
import com.nineleaps.expense_management_project.entity.*;
import com.nineleaps.expense_management_project.firebase.PushNotificationRequest;
import com.nineleaps.expense_management_project.firebase.PushNotificationService;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;


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
    static final String CONSTANT1 = " does not exist!";
    static final String CONSTANT2 = "Report with ID ";
    private static final String CONSTANT3 = "Expense with ID ";
    public static final String CONSTANT4 = "rejected";
    public static final String CONSTANT5 = "approved";
    public static final String CONSTANT6 = "Enter a valid request !";
    public static final String CONSTANT7 = "pending";
    private static final String CONSTANT8 = "Report ";
    public static final String CONSTANT9 = " is not Submitted!";
    private static final String CONSTANT10 = "Submitted you an expense report.";

    @Override
    public Set<Reports> getAllReports(Long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            Sort sort = Sort.by(Sort.Direction.DESC, "dateCreated");
            List<Expense> expenses = expenseRepository.findByEmployeeAndIsHidden(employee, false, sort);
            Set<Reports> reportsList = new HashSet<>();

            for (Expense expense : expenses) {
                Reports reports = expense.getReports();
                if (reports != null) {
                    reportsList.add(reports);
                }
            }

            return reportsList;
        } else {

            return Collections.emptySet();
        }
    }

    @Override
    public Reports getReportById(Long reportId) {
        Optional<Reports> optionalReport = reportsRepository.findById(reportId);
        return optionalReport.orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public Reports addReport(ReportsDTO reportsDTO, Long employeeId, List<Long> expenseIds) {
        Employee employee = employeeServices.getEmployeeById(employeeId);

        if (employee == null) {
            throw new IllegalArgumentException("Employee with ID " + employeeId + " not found");
        }
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

//        Long id = newReport.getReportId();
//
//        List<Expense> expenses = expenseRepository.findAllById(expenseIds);

//        if (expenses != null && !expenses.isEmpty()) {
//            Long expensesCount = (long) expenses.size();
//            newReport.setExpensesCount(expensesCount);
//
//            float totalAmount = 0;
//            for (Expense expense : expenses) {
//                if (expense != null) {
//                    totalAmount += expense.getAmount();
//                }
//            }
//            newReport.setTotalAmount(totalAmount);
//
//            addExpenseToReport(id, expenseIds);
//
//            String reportTitle = newReport.getReportTitle();
//            for (Expense expense : expenses) {
//                if (expense != null) {
//                    expense.setReportTitle(reportTitle);
//                    expenseRepository.save(expense);
//                }
//            }
//        }

        return reportsRepository.save(newReport);
    }



    @Override
    public List<Reports> editReport(Long reportId, String reportTitle, String reportDescription,
                                    List<Long> addExpenseIds, List<Long> removeExpenseIds) {
        Reports report = reportsRepository.getReportByReportId(reportId);

        validateReportForEdit(report);

        updateReportAndExpenseTitles(reportId, reportTitle);

        addExpensesToReport(reportId, addExpenseIds);

        removeExpensesFromReport(removeExpenseIds);

        updateReportTotalAmounts(reportId);

        return getReportByEmpId(report.getEmployeeId(), "drafts");
    }

    void validateReportForEdit(Reports report) {
        if (report != null && Boolean.TRUE.equals(report.getIsSubmitted()) &&
                report.getManagerApprovalStatus() == ManagerApprovalStatus.PENDING) {
            throw new IllegalStateException("Cannot edit a report that is already submitted.");
        }


//        if (report != null) {
//            if (Boolean.TRUE.equals(report.getIsHidden())) {
//                throw new NullPointerException("The report is hidden.");
//            }
//        }


        if (report != null) {
            if (Boolean.TRUE.equals(report.getIsSubmitted()) &&
                    report.getManagerApprovalStatus() == ManagerApprovalStatus.REJECTED) {
                throw new IllegalStateException("Cannot edit a report that is rejected.");
            }
//        } else {
//            throw new NullPointerException("The report is null.");
        }

    }

    void updateReportAndExpenseTitles(Long reportId, String reportTitle) {
        Reports report = reportsRepository.getReportByReportId(reportId);
        if(report != null) {
            report.setReportTitle(reportTitle);
            reportsRepository.save(report);

            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
            for (Expense exp : expenseList) {
                if (exp != null) {
                    exp.setReportTitle(reportTitle);
                    expenseRepository.save(exp);
                }
            }
        }
    }

    void addExpensesToReport(Long reportId, List<Long> expenseIds) {
        for (Long expenseId : expenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);
            if (expense != null && expenseId != null) {
                if (Boolean.TRUE.equals(expense.getIsReported())) {
                    throw new IllegalStateException("Expense " + expenseId + " is already reported in another report.");
                }
//            }
            }


            expenseServices.updateExpense(reportId, expenseId);
        }
    }

    void removeExpensesFromReport(List<Long> expenseIds) {
        boolean reportedStatus = false;

        for (Long expenseId : expenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);
            if(expense != null && (Boolean.TRUE.equals(expense.getIsReported()))) {
                expense.setIsReported(reportedStatus);
                expense.setReports(null);
                expense.setReportTitle(null);
                expenseRepository.save(expense);

            }

        }
    }

    void updateReportTotalAmounts(Long reportId) {
        Reports report = reportsRepository.getReportByReportId(reportId);
        if(report != null){
            report.setTotalAmount(totalAmount(reportId));
            report.setTotalApprovedAmount(totalApprovedAmount(reportId));
            reportsRepository.save(report);}
    }

    @Override
    public Reports addExpenseToReport(Long reportId, List<Long> expenseIds) {

        for (Long expenseId : expenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);

            if (expense == null) {

                throw new NullPointerException("Expense not found for ID: " + expenseId);
            }
//            else if (Boolean.TRUE.equals(expense.getIsReported())) {
//                throw new IllegalStateException(CONSTANT3 + expenseId + " is already reported in another report!");
//            }
            else {
                expenseServices.updateExpense(reportId, expenseId);
            }
        }

        Reports re = reportsRepository.getReportByReportId(reportId);
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
            if (Boolean.TRUE.equals(reports2.getIsSubmitted())) {
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

        if (Boolean.TRUE.equals(re.getIsHidden())) {
            throw new NullPointerException(CONSTANT2 + reportId + CONSTANT1);
        }

        // Check if the report is already submitted and not rejected
//        if (Boolean.TRUE.equals(re.getIsSubmitted()) &&
//                re.getManagerApprovalStatus() != ManagerApprovalStatus.REJECTED) {
//            throw new IllegalStateException(CONSTANT2 + reportId + " is already submitted!");
//        }

        // Check if the report is not submitted or was previously rejected by the manager
//        if (Boolean.FALSE.equals(re.getIsSubmitted()) ||
//                re.getManagerApprovalStatus() == ManagerApprovalStatus.REJECTED) {
//            List<Expense> rejectedExpenses = expenseServices.getRejectedExpensesByReportId(reportId);
//            for (Expense expense : rejectedExpenses) {
//                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
//                expenseRepository.save(expense);
//            }
        re.setIsSubmitted(submissionStatus);
        re.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
        re.setDateSubmitted(LocalDate.now());
        re.setTotalAmount(totalAmount(reportId));

//             Fetch the manager's email and set it in the report
//            Employee employee = employeeServices.getEmployeeById(employeeId);
//            String managerEmail = employee.getManagerEmail();
//            if (managerEmail == null) {
//                throw new NullPointerException("Manager Email not found for Employee ID: " + employee.getEmployeeId());
//            }
//            re.setManagerEmail(managerEmail);

        reportsRepository.save(re);
//
//            // Send email notification to the manager
//            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
//            ArrayList<Long> expenseIds = new ArrayList<>();
//            for (Expense expense : expenseList) {
//                expenseIds.add(expense.getExpenseId());
//            }
//            emailService.managerNotification(reportId, expenseIds, response);
//        }
//
//        // Push Notification Functionality
//        String managerEmail = re.getManagerEmail();
//        Employee manager = employeeServices.getEmployeeByEmail(managerEmail);
//
//        if (manager != null && manager.getToken() != null) {
//            Employee employee = employeeServices.getEmployeeById(employeeId);
//            PushNotificationRequest notificationRequest = new PushNotificationRequest();
//            notificationRequest.setTitle(employee.getFirstName() + " " + employee.getLastName());
//            notificationRequest.setMessage(CONSTANT10);
//            notificationRequest.setToken(manager.getToken());
//            pushNotificationService.sendPushNotificationToToken(notificationRequest);
//        }
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
//        if (!re.getIsSubmitted() || re.getManagerApprovalStatus() == ManagerApprovalStatus.REJECTED) {
//            List<Expense> rejectedExpenses = expenseServices.getRejectedExpensesByReportId(reportId);
//            for (Expense expense : rejectedExpenses) {
//                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PENDING);
//                expenseRepository.save(expense);
//            }
        re.setIsSubmitted(submissionStatus);
        re.setManagerApprovalStatus(ManagerApprovalStatus.PENDING);
        re.setDateSubmitted(LocalDate.now());
        re.setTotalAmount(totalAmount(reportId));

        // Fetch the manager's email and set it in the report
//            Employee employee = employeeServices.getEmployeeById(employeeId);
//            if (employee != null) {
//                String managerEmailDB = employee.getManagerEmail();
//
//
//                if (managerEmailDB == null) {
//                    throw new NullPointerException("Manager Email not found for Employee ID: " + employee.getEmployeeId());
//                }
//                if (Objects.equals(managerEmail, managerEmailDB)) {
//                    re.setManagerEmail(managerEmailDB);
        reportsRepository.save(re);
        // Send email notification to the manager
//                List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
//                ArrayList<Long> expenseIds = new ArrayList<>();
//                for (Expense expense : expenseList) {
//                    expenseIds.add(expense.getExpenseId());
//                }
//                emailService.managerNotification(reportId, expenseIds, managerEmailDB, response);
//                // Push Notification Functionality
//                Employee manager = employeeServices.getEmployeeByEmail(managerEmailDB);
//                if (manager != null && manager.getToken() != null) {
////                    Employee employee = employeeServices.getEmployeeById(employeeId);
//                    PushNotificationRequest notificationRequest = new PushNotificationRequest();
//                    notificationRequest.setTitle(employee.getLastName());
//                    notificationRequest.setMessage("Submitted you an expense report.");
//                    notificationRequest.setToken(manager.getToken());
//                    System.out.println("TOKEN-" + manager.getToken());
//
//                    pushNotificationService.sendPushNotificationToToken(notificationRequest);
//                }
//            }
//            } else {
//                re.setManagerEmail(managerEmail);
//                reportsRepository.save(re);
//                // Send email notification to the manager
//                List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
//                ArrayList<Long> expenseIds = new ArrayList<>();
//                for (Expense expense : expenseList) {
//                    expenseIds.add(expense.getExpenseId());
//                }
//                emailService.managerNotification(reportId, expenseIds, managerEmail, response);
//                // Push Notification Functionality
//                Employee manager = employeeServices.getEmployeeByEmail(managerEmail);
//                if (manager != null && manager.getToken() != null) {
//                    Employee employee1 = employeeServices.getEmployeeById(employeeId);
//                    PushNotificationRequest notificationRequest = new PushNotificationRequest();
//                    notificationRequest.setTitle(employee1.getLastName());
//                    notificationRequest.setMessage("Submitted you an expense report.");
//                    notificationRequest.setToken(manager.getToken());
//                    System.out.println("TOKEN-" + manager.getToken());
//
//                    pushNotificationService.sendPushNotificationToToken(notificationRequest);
//                }
//            }
    }




    @Override
    public void reimburseReportByFinance(ArrayList<Long> reportIds, String comments) {
        FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.APPROVED;
        for (Long reportId : reportIds) {
            Reports re = getReportById(reportId);
            Long employeeId = re.getEmployeeId();
            if (Boolean.TRUE.equals(re.getIsHidden())) {
                throw new ObjectNotFoundException(reportId, CONSTANT8 + reportId + CONSTANT1);
            }
//            if (Boolean.FALSE.equals(re.getIsSubmitted())) {
//                throw new IllegalStateException(CONSTANT8 + reportId + " is not submitted!");
//            }
//
//            if (re.getManagerApprovalStatus() != ManagerApprovalStatus.APPROVED &&
//                    re.getManagerApprovalStatus() != ManagerApprovalStatus.PARTIALLY_APPROVED) {
//                throw new IllegalStateException(CONSTANT8 + reportId + " is not approved by the manager!");
//            }
//            re.setFinanceApprovalStatus(approvalStatus);
//            re.setFinanceComments(comments);
//            re.setFinanceActionDate(LocalDate.now());
//            reportsRepository.save(re);
//            emailService.userReimbursedNotification(reportId);
//            //Update Expenses Status
//            List<Expense> expenseList = expenseServices.getExpenseByReportId(reportId);
//            for (Expense exp : expenseList) {
//                exp.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
//                expenseRepository.save(exp);
//            }
            //Push Notification Functionality

//            Employee employee = employeeServices.getEmployeeById(employeeId);
//
//            PushNotificationRequest notificationRequest = new PushNotificationRequest();
//            notificationRequest.setTitle("[PUSHED TO REIMBURSEMENT]: " + re.getReportTitle());
//            notificationRequest.setMessage("Your expense report is pushed to reimbursement.");
//            notificationRequest.setToken(employee.getToken());
//            pushNotificationService.sendPushNotificationToToken(notificationRequest);
        }
    }

    @Override
    public void rejectReportByFinance(Long reportId, String comments) {
        FinanceApprovalStatus approvalStatus = FinanceApprovalStatus.REJECTED;
        Reports re = getReportById(reportId);
        Long employeeId = re.getEmployeeId();
//        if (Boolean.FALSE.equals(re.getIsSubmitted())) {
//            throw new IllegalStateException(CONSTANT8 + reportId + CONSTANT9);
//        }
        if (Boolean.TRUE.equals(re.getIsHidden())) {
            throw new ObjectNotFoundException(reportId, CONSTANT8 + reportId + CONSTANT1);
        }
        if (Boolean.FALSE.equals(re.getIsHidden()) && Boolean.TRUE.equals(re.getIsSubmitted()) &&
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

//        Employee employee = employeeServices.getEmployeeById(employeeId);
//
//        PushNotificationRequest notificationRequest = new PushNotificationRequest();
//        notificationRequest.setTitle("[REJECTED]: " + re.getReportTitle());
//        notificationRequest.setMessage("Accounts admin rejected your expense report.");
//        notificationRequest.setToken(employee.getToken());
//        pushNotificationService.sendPushNotificationToToken(notificationRequest);
    }

    @Override
    public float totalAmount(Long reportId) {
        Optional<Reports> reportOptional = reportsRepository.findById(reportId);

        if (reportOptional.isPresent()) {
            Reports report = reportOptional.get();
            List<Expense> expenses = expenseRepository.findByReports(Optional.of(report));

            float amtINR = 0;
            for (Expense expense : expenses) {
                amtINR += expense.getAmount();
            }
            return amtINR;
        } else {

            return 0;
        }
    }


    @Override
    public float totalApprovedAmount(Long reportId) {
        Reports report = reportsRepository.getReportByReportId(reportId);
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
            case CONSTANT5:
                return reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate,
                        FinanceApprovalStatus.APPROVED);
            case CONSTANT7:
                return reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate,
                        FinanceApprovalStatus.PENDING);
            case "reimbursed":
                return reportsRepository.findByDateSubmittedBetweenAndFinanceApprovalStatus(startDate, endDate,
                        FinanceApprovalStatus.REIMBURSED);
            case CONSTANT4:
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
        LocalDate managerActionDate = LocalDate.now();
        Reports report = getReportById(reportId);
        validateReport(report, reportId);


        processApprovedExpenses(approveExpenseIds);
        processRejectedExpenses(rejectExpenseIds);
        processPartiallyApprovedExpenses(partiallyApprovedMap);

        updateReportStatus(reportId, report, approveExpenseIds, rejectExpenseIds, partiallyApprovedMap, reviewTime,
                comments, managerActionDate, response);
    }

    private void validateReport(Reports report, Long reportId) {
        if (Boolean.TRUE.equals(report.getIsHidden())) {
            throw new IllegalStateException(CONSTANT2 + reportId + CONSTANT1);
        } else if (Boolean.FALSE.equals(report.getIsSubmitted())) {
            throw new IllegalStateException(CONSTANT2 + reportId + " is not submitted!");
        }
    }

    void processApprovedExpenses(List<Long> approveExpenseIds) {
        for (Long expenseId : approveExpenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);
            validateExpense(expense, expenseId);

            if (Boolean.TRUE.equals(expense.getIsReported()) &&
                    expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.PENDING) {
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.APPROVED);
                expense.setAmountApproved(expense.getAmount());
                expenseRepository.save(expense);
            }
        }
    }

    void processRejectedExpenses(List<Long> rejectExpenseIds) {
        for (Long expenseId : rejectExpenseIds) {
            Expense expense = expenseServices.getExpenseById(expenseId);
            validateExpense(expense, expenseId);

            if (Boolean.TRUE.equals(expense.getIsReported()) &&
                    expense.getManagerApprovalStatusExpense() == ManagerApprovalStatusExpense.PENDING) {
                expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.REJECTED);
                expense.setAmountApproved(0.0);
                expenseRepository.save(expense);
            }
        }
    }

    void processPartiallyApprovedExpenses(Map<Long, Float> partiallyApprovedMap) {
        for (Map.Entry<Long, Float> entry : partiallyApprovedMap.entrySet()) {
            Long expId = entry.getKey();
            Float amt = entry.getValue();
            Expense expense = expenseServices.getExpenseById(expId);
            validateExpense(expense, expId);

            expense.setManagerApprovalStatusExpense(ManagerApprovalStatusExpense.PARTIALLY_APPROVED);
            expense.setAmountApproved(Double.valueOf(amt));
            expenseRepository.save(expense);
        }
    }

    void validateExpense(Expense expense, Long expenseId) {
        if (Boolean.TRUE.equals(expense.getIsHidden())) {
            throw new IllegalStateException(CONSTANT3 + expenseId + CONSTANT1);
        }
    }

    private void updateReportStatus(Long reportId, Reports report, List<Long> approveExpenseIds,
                                    List<Long> rejectExpenseIds, Map<Long, Float> partiallyApprovedMap,
                                    String reviewTime, String comments, LocalDate managerActionDate,
                                    HttpServletResponse response) {
        if (rejectExpenseIds.isEmpty() && partiallyApprovedMap.isEmpty()) {
            report.setManagerApprovalStatus(ManagerApprovalStatus.APPROVED);
            report.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
            reportsRepository.save(report);
            // Email and push notifications...
//        } else if (!rejectExpenseIds.isEmpty()) {
//            report.setManagerApprovalStatus(ManagerApprovalStatus.REJECTED);
//            reportsRepository.save(report);
//            // Email and push notifications...
//        } else if (rejectExpenseIds.isEmpty() && !partiallyApprovedMap.isEmpty()) {
//            report.setManagerApprovalStatus(ManagerApprovalStatus.PARTIALLY_APPROVED);
//            report.setFinanceApprovalStatus(FinanceApprovalStatus.PENDING);
//            reportsRepository.save(report);
//            // Email and push notifications...
//        }
            report.setManagerReviewTime(reviewTime);
            report.setManagerActionDate(managerActionDate);
            report.setManagerComments(comments);

            report.setTotalApprovedAmount(totalApprovedAmount(reportId));
            reportsRepository.save(report);
        }
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

    @Override
    public void notifyLnD(Long reportId) throws MessagingException {
        Reports report = getReportById(reportId);
        Long employeeId = report.getEmployeeId();
        Employee employee = employeeServices.getEmployeeById(employeeId);
        String lndEmail = employee.getLndEmail();
        String lndName = employee.getLndName();
        emailService.notifyLnD(reportId, lndEmail, lndName);
    }


}