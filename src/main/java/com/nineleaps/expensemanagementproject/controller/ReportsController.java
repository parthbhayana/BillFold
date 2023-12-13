package com.nineleaps.expensemanagementproject.controller;

/**
          ReportsController

          Manages endpoints related to reports, including CRUD operations, report submissions, and status updates.

          ## Endpoints (Continued)

          [Previous documented endpoints...]

          ### GET /getReportsSubmittedToUser/{managerEmail}

          - **Description:** Retrieves reports submitted to a specific manager based on their email and request type.
          - **Path Parameters:**
            - managerEmail (String) - Email of the manager.
          - **Query Parameters:**
            - request (String) - Request parameter.
          - **Returns:** List of Reports objects submitted to the provided manager's email with the specified request type.

          ### GET /getAllSubmittedReports

          - **Description:** Retrieves all submitted reports.
          - **Returns:** List of all submitted Reports objects.

          ### GET /getAllReportsApprovedByManager

          - **Description:** Retrieves reports approved by a manager based on the request type.
          - **Query Parameters:**
            - request (String) - Request parameter.
          - **Returns:** List of Reports objects approved by the manager with the specified request type.
          ### POST /addReport/{employeeId}

          - **Description:** Adds a new report associated with the given employee and their related expense IDs.
          - **Request Body:** ReportsDTO object containing report details.
          - **Path Parameters:**
            - employeeId (Long) - ID of the associated employee.
          - **Query Parameters:**
            - expenseIds (List<Long>) - List of expense IDs associated with the report.
          - **Returns:** Reports object representing the newly added report.

          ### PATCH /addExpenseToReport/{reportId}

          - **Description:** Adds expenses to an existing report identified by the given report ID.
          - **Path Parameters:**
            - reportId (Long) - ID of the report to which expenses will be added.
          - **Query Parameters:**
            - expenseIds (List<Long>) - List of expense IDs to be added to the report.
          - **Returns:** Reports object after adding expenses to the specified report.

          ### POST /submitReport/{reportId}

          - **Description:** Submits a report identified by the given report ID.
          - **Path Parameters:**
            - reportId (Long) - ID of the report to be submitted.
          - **Response:** HTTP Response indicating the success or failure of the submission.

          ### POST /submitReportToManager/{reportId}

          - **Description:** Submits a report identified by the given report ID to a specific manager's email.
          - **Path Parameters:**
            - reportId (Long) - ID of the report to be submitted.
          - **Request Parameters:**
            - managerEmail (String) - Email address of the manager to whom the report is submitted.
          - **Response:** HTTP Response indicating the success or failure of the submission.

          ### PATCH /editReport/{reportId}

          - **Description:** Edits a report identified by the given report ID, updating its title, description, and expense associations.
          - **Path Parameters:**
            - reportId (Long) - ID of the report to be edited.
          - **Request Parameters:**
            - reportTitle (String) - Updated title for the report.
            - reportDescription (String) - Updated description for the report.
            - addExpenseIds (List<Long>) - IDs of expenses to be added to the report.
            - removeExpenseIds (List<Long>) - IDs of expenses to be removed from the report.
            - **Returns:** List of Reports objects after editing the specified report.
*/

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import com.nineleaps.expensemanagementproject.DTO.ReportsDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.service.IReportsService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@Slf4j
@RequestMapping("/api/v1/reports")
public class ReportsController {

    @Autowired
    private IReportsService reportsService;

    @GetMapping("/getAllReports/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public Set<Reports> getAllReports(@PathVariable Long employeeId) {
        log.info("Fetching all reports for employee with ID: {}", employeeId);
        return reportsService.getAllReports(employeeId);
    }

    @GetMapping("/getByReportId/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public Reports getReportByReportId(@PathVariable Long reportId) {
        log.info("Fetching report with ID: {}", reportId);
        return reportsService.getReportById(reportId);
    }

    @GetMapping("/getReportByEmployeeId/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public List<Reports> getReportByEmpId(@PathVariable Long employeeId, @RequestParam String request) {
        log.info("Fetching reports for employee ID: {} with request: {}", employeeId, request);
        return reportsService.getReportByEmpId(employeeId, request);
    }

    @GetMapping("/getReportsSubmittedToUser/{managerEmail}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public List<Reports> getReportsSubmittedToUser(@PathVariable String managerEmail, @RequestParam String request) {
        log.info("Fetching reports submitted to manager email: {} with request: {}", managerEmail, request);
        return reportsService.getReportsSubmittedToUser(managerEmail, request);
    }

    @GetMapping("/getAllSubmittedReports")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public List<Reports> getAllSubmittedReports() {
        log.info("Fetching all submitted reports");
        return reportsService.getAllSubmittedReports();
    }

    @GetMapping("/getAllReportsApprovedByManager")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public List<Reports> getAllReportsApprovedByManager(@RequestParam String request) {
        log.info("Fetching all reports approved by manager with request: {}", request);
        return reportsService.getAllReportsApprovedByManager(request);
    }


    @PostMapping("/addReport/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public Reports addReport(@RequestBody ReportsDTO reportsDTO, @PathVariable Long employeeId,
                             @RequestParam List<Long> expenseIds) {
        log.info("Adding report for employee with ID: {}", employeeId);
        return reportsService.addReport(reportsDTO, employeeId, expenseIds);
    }


    @PatchMapping("/addExpenseToReport/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public Reports addExpensesToReport(@PathVariable Long reportId, @RequestParam List<Long> expenseIds) {
        log.info("Adding expenses to report with ID: {}", reportId);
        return reportsService.addExpenseToReport(reportId, expenseIds);
    }

    @PostMapping("/submitReport/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public void submitReport(@PathVariable Long reportId, HttpServletResponse response) {
        try {
            log.info("Submitting report with ID: {}", reportId);
            reportsService.submitReport(reportId, response);
        } catch (MessagingException | IOException e) {
            log.error("Error submitting report with ID: {}", reportId, e);
            // Handle the exception or log the appropriate error message
        }
    }

    @PostMapping("/submitReportToManager/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public void submitReport(@PathVariable Long reportId, @RequestParam String managerEmail,
                             HttpServletResponse response) {
        try {
            log.info("Submitting report with ID: {} to manager with email: {}", reportId, managerEmail);
            reportsService.submitReport(reportId, managerEmail, response);
        } catch (MessagingException | IOException e) {
            log.error("Error submitting report with ID: {} to manager with email: {}", reportId, managerEmail, e);
            // Handle the exception or log the appropriate error message
        }
    }


    @PatchMapping("/editReport/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public List<Reports> editReport(@PathVariable Long reportId, @RequestParam String reportTitle,
                                    @RequestParam String reportDescription, @RequestParam List<Long> addExpenseIds,
                                    @RequestParam List<Long> removeExpenseIds) {
        try {
            log.info("Editing report with ID: {}", reportId);
            return reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
        } catch (Exception e) {
            log.error("Error editing report with ID: {}", reportId, e);
            // Handle the exception or log the appropriate error message
            return null; // Or return an appropriate error response
        }
    }


    @PostMapping("/approveReportByFinance")
    @PreAuthorize("hasRole('FINANCE_ADMIN')")
    public void reimburseReportByFinance(@RequestParam List<Long> reportIds,
                                         @RequestParam(value = "comments", defaultValue = "null") String comments) {
        try {
            log.info("Approving reports: {}", reportIds);
            reportsService.reimburseReportByFinance((ArrayList<Long>) reportIds, comments);
        } catch (Exception e) {
            log.error("Error approving reports: {}", reportIds, e);
            // Handle the exception or log the appropriate error message
        }
    }
    @PostMapping("/rejectReportByFinance/{reportId}")
    @PreAuthorize("hasRole('FINANCE_ADMIN')")
    public void rejectReportByFinance(@PathVariable Long reportId,
                                      @RequestParam(value = "comments", defaultValue = "null") String comments) {
        try {
            log.info("Rejecting report with ID: {}", reportId);
            reportsService.rejectReportByFinance(reportId, comments);
        } catch (Exception e) {
            log.error("Error rejecting report with ID: {}", reportId, e);
        }
    }

    @PostMapping("/hideReport/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public void hideReport(@PathVariable Long reportId) {
        try {
            log.info("Hiding report with ID: {}", reportId);
            reportsService.hideReport(reportId);
        } catch (Exception e) {
            log.error("Error hiding report with ID: {}", reportId, e);
        }
    }
    @GetMapping("/getTotalAmountInrByReportId")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public float totalAmountINR(@RequestParam Long reportId) {
        try {
            log.info("Calculating total amount in INR for report ID: {}", reportId);
            return reportsService.totalAmount(reportId);
        } catch (Exception e) {
            log.error("Error calculating total amount in INR for report ID: {}", reportId, e);
            return 0.0f;
        }
    }

    @GetMapping("/getReportsInDateRange")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public List<Reports> getReportsInDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam String request) {
        try {
            log.info("Fetching reports between {} and {}", startDate, endDate);
            return reportsService.getReportsInDateRange(startDate, endDate, request);
        } catch (Exception e) {
            log.error("Error fetching reports in date range {} to {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }
    @GetMapping("/getReportsSubmittedToUserInDateRange")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public List<Reports> getReportsSubmittedToUserInDateRange(@RequestBody String managerEmail,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                              @RequestParam String request) {
        try {
            log.info("Fetching reports submitted to {} between {} and {}", managerEmail, startDate, endDate);
            return reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);
        } catch (Exception e) {
            log.error("Error fetching reports submitted to {} in date range {} to {}", managerEmail, startDate, endDate, e);
            // Handle the exception or log the appropriate error message
            return Collections.emptyList(); // or handle the exception as needed
        }
    }

    @GetMapping("/getAmountOfReportsInDateRange")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public String getAmountOfReportsInDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            log.info("Fetching the amount of reports in date range: {} to {}", startDate, endDate);
            return reportsService.getAmountOfReportsInDateRange(startDate, endDate);
        } catch (Exception e) {
            log.error("Error fetching amount of reports in date range {} to {}: {}", startDate, endDate, e.getMessage(), e);
            return "Error occurred while fetching reports";
        }
    }

    @GetMapping("/getTotalApprovedAmount")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public float totalApprovedAmount(@RequestParam Long reportId) {
        try {
            log.info("Fetching total approved amount for report ID: {}", reportId);
            return reportsService.totalApprovedAmount(reportId);
        } catch (Exception e) {
            log.error("Error fetching total approved amount for report ID {}: {}", reportId, e.getMessage(), e);
            // Handle the exception or log the appropriate error message
            return 0.0f; // Or any appropriate default value indicating an error
        }
    }

    @PostMapping("/notifyHr/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public void notifyHr(@PathVariable Long reportId) {
        try {
            log.info("Notifying HR for report ID: {}", reportId);
            reportsService.notifyHR(reportId);
            log.info("Notification sent successfully for report ID: {}", reportId);
        } catch (MessagingException e) {
            log.error("Error notifying HR for report ID {}: {}", reportId, e.getMessage(), e);
            // Handle the exception or log the appropriate error message
        }
    }

    @PostMapping("/notifyLnD/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE','FINANCE_ADMIN')")
    public void notifyLnD(@PathVariable Long reportId) {
        try {
            log.info("Notifying Learning and Development for report ID: {}", reportId);
            reportsService.notifyLnD(reportId);
            log.info("Notification sent successfully for report ID: {}", reportId);
        } catch (MessagingException e) {
            log.error("Error notifying Learning and Development for report ID {}: {}", reportId, e.getMessage(), e);
            // Handle the exception or log the appropriate error message
        }
    }

    @PostMapping("/updateExpenseStatus/{reportId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void updateExpenseStatus(@PathVariable Long reportId, @RequestParam String reviewTime,
                                    @RequestParam String json, @RequestParam String comments, HttpServletResponse response) {
        JSONParser parser = new JSONParser();
        try {
            Map<Long, Float> partialApprovedMap = new HashMap<>();
            List<Long> approvedIds = new ArrayList<>();
            List<Long> rejectedIds = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) parser.parse(json);
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;

                long expenseId = (Long) jsonObject.get("expenseId");
                float amountApproved = (Long) jsonObject.get("amountApproved");
                String status = (String) jsonObject.get("status");

                if ("approved".equals(status)) {
                    approvedIds.add(expenseId);
                }
                if ("rejected".equals(status)) {
                    rejectedIds.add(expenseId);
                }
                if ("partiallyApproved".equals(status)) {
                    partialApprovedMap.put(expenseId, amountApproved);
                }
            }

            log.info("Updating expense status for report ID: {}", reportId);
            reportsService.updateExpenseStatus(reportId, approvedIds, rejectedIds, partialApprovedMap, reviewTime,
                    comments, response);
            log.info("Expense status updated successfully for report ID: {}", reportId);
        } catch (ParseException e) {
            log.error("Error parsing JSON: {}", e.getMessage(), e);
            // Handle the parsing exception or log the appropriate error message
        } catch (MessagingException | IOException e) {
            log.error("Error updating expense status for report ID {}: {}", reportId, e.getMessage(), e);
            // Handle the MessagingException, IOException, or log the appropriate error message
            throw new RuntimeException(e);
        }
    }
}