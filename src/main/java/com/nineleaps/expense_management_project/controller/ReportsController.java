package com.nineleaps.expense_management_project.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expense_management_project.dto.ReportsDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.service.IReportsService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class ReportsController {

    @Autowired
    private IReportsService reportsService;

    // Endpoint to get all reports for a specific employee
    @GetMapping("/getAllReports/{employeeId}")
    public Set<Reports> getAllReports(@PathVariable Long employeeId) {
        return reportsService.getAllReports(employeeId);
    }

    // Endpoint to get a report by its ID
    @GetMapping("/getByReportId/{reportId}")
    public Reports getReportByReportId(@PathVariable Long reportId) {
        return reportsService.getReportById(reportId);
    }

    // Endpoint to get reports submitted by an employee
    @GetMapping("/getReportByEmployeeId/{employeeId}")
    public List<Reports> getReportByEmpId(@PathVariable Long employeeId, @RequestParam String request) {
        return reportsService.getReportByEmpId(employeeId, request);
    }

    // Endpoint to get reports submitted to a manager
    @GetMapping("/getReportsSubmittedToUser/{managerEmail}")
    public List<Reports> getReportsSubmittedToUser(@PathVariable String managerEmail, @RequestParam String request) {
        return reportsService.getReportsSubmittedToUser(managerEmail, request);
    }

    // Endpoint to get all submitted reports
    @GetMapping("/getAllSubmittedReports")
    public List<Reports> getAllSubmittedReports() {
        return reportsService.getAllSubmittedReports();
    }

    // Endpoint to get all reports approved by a manager
    @GetMapping("/getAllReportsApprovedByManager")
    public List<Reports> getAllReportsApprovedByManager(@RequestParam String request) {
        return reportsService.getAllReportsApprovedByManager(request);
    }

    // Endpoint to add a new report
    @PostMapping("/addReport/{employeeId}")
    public Reports addReport(@RequestBody ReportsDTO reportsDTO, @PathVariable Long employeeId,
                             @RequestParam List<Long> expenseIds) {
        return reportsService.addReport(reportsDTO, employeeId, expenseIds);
    }

    // Endpoint to add expenses to a report
    @PatchMapping("/addExpenseToReport/{reportId}")
    public Reports addExpensesToReport(@PathVariable Long reportId, @RequestParam List<Long> expenseIds) {
        return reportsService.addExpenseToReport(reportId, expenseIds);
    }

    // Endpoint to submit a report
    @PostMapping("/submitReport/{reportId}")
    public void submitReport(@PathVariable Long reportId, HttpServletResponse response)
            throws MessagingException, IOException {

        reportsService.submitReport(reportId, response);
    }

    // Endpoint to submit a report to a manager
    @PostMapping("/submitReportToManager/{reportId}")
    public void submitReport(@PathVariable Long reportId, @RequestParam String managerEmail,
                             HttpServletResponse response) throws MessagingException, IOException {

        reportsService.submitReport(reportId, managerEmail, response);
    }

    // Endpoint to edit a report
    @PatchMapping("/editReport/{reportId}")
    public List<Reports> editReport(@PathVariable Long reportId, @RequestParam String reportTitle,
                                    @RequestParam String reportDescription, @RequestParam List<Long> addExpenseIds,
                                    @RequestParam List<Long> removeExpenseIds) {
        return reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
    }

    // Endpoint to approve reports by finance
    @PostMapping("/approveReportByFinance")
    public void reimburseReportByFinance(@RequestParam List<Long> reportIds,
                                         @RequestParam(value = "comments", defaultValue = "null") String comments) {
        reportsService.reimburseReportByFinance((ArrayList<Long>) reportIds, comments);
    }

    // Endpoint to reject a report by finance
    @PostMapping("/rejectReportByFinance/{reportId}")
    public void rejectReportByFinance(@PathVariable Long reportId,
                                      @RequestParam(value = "comments", defaultValue = "null") String comments) {
        reportsService.rejectReportByFinance(reportId, comments);
    }

    // Endpoint to hide a report
    @PostMapping("/hideReport/{reportId}")
    public void hideReport(@PathVariable Long reportId) {
        reportsService.hideReport(reportId);
    }

    // Endpoint to get the total amount (INR) for a report
    @GetMapping("/getTotalAmountInrByReportId")
    public float totalAmountINR(@RequestParam Long reportId) {
        return reportsService.totalAmount(reportId);
    }

    // Endpoint to get reports in a date range
    @GetMapping("/getReportsInDateRange")
    public List<Reports> getReportsInDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam String request) {
        return reportsService.getReportsInDateRange(startDate, endDate, request);
    }

    // Endpoint to get reports submitted to a user in a date range
    @GetMapping("/getReportsSubmittedToUserInDateRange")
    public List<Reports> getReportsSubmittedToUserInDateRange(@RequestBody String managerEmail,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam String request) {
        return reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);
    }

    // Endpoint to get the amount of reports in a date range
    @GetMapping("/getAmountOfReportsInDateRange")
    public String getAmountOfReportsInDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return reportsService.getAmountOfReportsInDateRange(startDate, endDate);
    }

    // Endpoint to get the total approved amount for a report
    @GetMapping("/getTotalApprovedAmount")
    public float totalApprovedAmount(Long reportId) {
        return reportsService.totalApprovedAmount(reportId);
    }

    // Endpoint to notify HR for a report
    @PostMapping("/notifyHr/{reportId}")
    public void notifyHr(@RequestParam Long reportId) throws MessagingException {
        reportsService.notifyHR(reportId);
    }

    // Endpoint to notify Learning and Development (LnD) for a report
    @PostMapping("/notifyLnD/{reportId}")
    public void notifyLnD(@RequestParam Long reportId) throws MessagingException {
        reportsService.notifyLnD(reportId);
    }

    // Endpoint to update the status of expenses in a report
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
