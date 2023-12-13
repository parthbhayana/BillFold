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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.service.IReportsService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportsController {

    @Autowired
    private IReportsService reportsService;

    @GetMapping("/getAllReports/{employeeId}")
    public Set<Reports> getAllReports(@PathVariable Long employeeId) {
        return reportsService.getAllReports(employeeId);
    }

    @GetMapping("/getByReportId/{reportId}")
    public Reports getReportByReportId(@PathVariable Long reportId) {
        return reportsService.getReportById(reportId);
    }

    @GetMapping("/getReportByEmployeeId/{employeeId}")
    public List<Reports> getReportByEmpId(@PathVariable Long employeeId, @RequestParam String request) {
        return reportsService.getReportByEmpId(employeeId, request);
    }

    @GetMapping("/getReportsSubmittedToUser/{managerEmail}")
    public List<Reports> getReportsSubmittedToUser(@PathVariable String managerEmail, @RequestParam String request) {
        return reportsService.getReportsSubmittedToUser(managerEmail, request);
    }

    @GetMapping("/getAllSubmittedReports")
    public List<Reports> getAllSubmittedReports() {
        return reportsService.getAllSubmittedReports();
    }

    @GetMapping("/getAllReportsApprovedByManager")
    public List<Reports> getAllReportsApprovedByManager(@RequestParam String request) {
        return reportsService.getAllReportsApprovedByManager(request);
    }

    @PostMapping("/addReport/{employeeId}")
    public Reports addReport(@RequestBody ReportsDTO reportsDTO, @PathVariable Long employeeId,
                             @RequestParam List<Long> expenseIds) {
        return reportsService.addReport(reportsDTO, employeeId, expenseIds);
    }

    @PatchMapping("/addExpenseToReport/{reportId}")
    public Reports addExpensesToReport(@PathVariable Long reportId, @RequestParam List<Long> expenseIds) {
        return reportsService.addExpenseToReport(reportId, expenseIds);
    }

    @PostMapping("/submitReport/{reportId}")
    public void submitReport(@PathVariable Long reportId,
                             HttpServletResponse response) throws MessagingException, IOException {

        reportsService.submitReport(reportId, response);
    }

    @PostMapping("/submitReportToManager/{reportId}")
    public void submitReport(@PathVariable Long reportId, @RequestParam String managerEmail,
                             HttpServletResponse response) throws MessagingException, IOException {

        reportsService.submitReport(reportId, managerEmail, response);
    }

    @PatchMapping("/editReport/{reportId}")
    public List<Reports> editReport(@PathVariable Long reportId, @RequestParam String reportTitle,
                                    @RequestParam String reportDescription, @RequestParam List<Long> addExpenseIds,
                                    @RequestParam List<Long> removeExpenseIds) {
        return reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
    }


    @PostMapping("/approveReportByFinance")
    public void reimburseReportByFinance(@RequestParam List<Long> reportIds,
                                         @RequestParam(value = "comments", defaultValue = "null") String comments) {
        reportsService.reimburseReportByFinance((ArrayList<Long>) reportIds, comments);
    }
    @PostMapping("/rejectReportByFinance/{reportId}")
    public void rejectReportByFinance(@PathVariable Long reportId,
                                      @RequestParam(value = "comments", defaultValue = "null") String comments) {
        reportsService.rejectReportByFinance(reportId, comments);
    }

    @PostMapping("/hideReport/{reportId}")
    public void hideReport(@PathVariable Long reportId) {
        reportsService.hideReport(reportId);
    }

    @GetMapping("/getTotalAmountInrByReportId")
    public float totalAmountINR(@RequestParam Long reportId) {
        return reportsService.totalAmount(reportId);
    }

    @GetMapping("/getReportsInDateRange")
    public List<Reports> getReportsInDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam String request) {
        return reportsService.getReportsInDateRange(startDate, endDate, request);
    }

    @GetMapping("/getReportsSubmittedToUserInDateRange")
    public List<Reports> getReportsSubmittedToUserInDateRange(@RequestBody String managerEmail,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                              @RequestParam String request) {
        return reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);
    }

    @GetMapping("/getAmountOfReportsInDateRange")
    public String getAmountOfReportsInDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return reportsService.getAmountOfReportsInDateRange(startDate, endDate);
    }

    @GetMapping("/getTotalApprovedAmount")
    public float totalApprovedAmount(Long reportId) {
        return reportsService.totalApprovedAmount(reportId);
    }

    @PostMapping("/notifyHr/{reportId}")
    public void notifyHr(@RequestParam Long reportId) throws MessagingException {
        reportsService.notifyHR(reportId);
    }

    @PostMapping("/notifyLnD/{reportId}")
    public void notifyLnD(@RequestParam Long reportId) throws MessagingException {
        reportsService.notifyLnD(reportId);
    }

    @PostMapping("/updateExpenseStatus/{reportId}")
    public void updateExpenseStatus(@PathVariable Long reportId, @RequestParam String reviewTime,
                                    @RequestParam String json, @RequestParam String comments, HttpServletResponse response)
            throws ParseException {
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

                if (Objects.equals(status, "approved")) {
                    approvedIds.add(expenseId);
                }
                if (Objects.equals(status, "rejected")) {
                    rejectedIds.add(expenseId);
                }
                if (Objects.equals(status, "partiallyApproved")) {
                    partialApprovedMap.put(expenseId, amountApproved);
                }
            }
            reportsService.updateExpenseStatus(reportId, approvedIds, rejectedIds, partialApprovedMap, reviewTime,
                    comments, response);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}