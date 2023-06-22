package com.nineleaps.expensemanagementproject.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.management.AttributeNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.service.IReportsService;
import com.nineleaps.expensemanagementproject.service.PdfGeneratorServiceImpl;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class ReportsController {

    @Autowired
    private IReportsService reportsService;

    @Autowired
    PdfGeneratorServiceImpl pdfGeneratorService;

    @GetMapping("/getAllReports")
    public List<Reports> getAllReports() {
        return reportsService.getAllReports();
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
    public Reports addReport(@RequestBody Reports newReport, @PathVariable Long employeeId,
                             @RequestParam ArrayList<Long> expenseIds) {
        return reportsService.addReport(newReport, employeeId, expenseIds);
    }

    @PatchMapping("/addExpenseToReport/{reportId}")
    public Reports addExpensesToReport(@PathVariable Long reportId, @RequestParam ArrayList<Long> expenseIds) {
        return reportsService.addExpenseToReport(reportId, expenseIds);
    }


    @PostMapping("/submitReport/{reportId}")
    public void submitReport(@PathVariable Long reportId, @RequestParam String managerEmail,HttpServletResponse response) throws MessagingException,FileNotFoundException,IOException{

        reportsService.submitReport(reportId, managerEmail,response);
    }



    @PatchMapping("/editReport/{reportId}")
    public List<Reports> editReport(@PathVariable Long reportId, @RequestParam String reportTitle,
                                    @RequestParam String reportDescription, @RequestParam ArrayList<Long> addExpenseIds,
                                    @RequestParam ArrayList<Long> removeExpenseIds) {
        return reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
    }

    @PostMapping("/approveReportByManager/{reportId}")
    public void approveReportByManager(@PathVariable Long reportId,
                                       @RequestParam(value = "comments", defaultValue = "null") String comments) {
        reportsService.approveReportByManager(reportId, comments);
    }

    @PostMapping("/rejectReportByManager/{reportId}")
    public void rejectReportByManager(@PathVariable Long reportId,
                                      @RequestParam(value = "comments", defaultValue = "null") String comments) {
        reportsService.rejectReportByManager(reportId, comments);
    }

    @PostMapping("/approveReportByFinance/{reportId}")
    public void approveReportByFinance(@PathVariable Long reportId,
                                       @RequestParam(value = "comments", defaultValue = "null") String comments) {
        reportsService.approveReportByFinance(reportId, comments);
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
        return reportsService.totalamountINR(reportId);
    }

    @GetMapping("/getTotalAmountCurrencyByReportId")
    public float totalAmountCurrency(@RequestParam Long reportId) {
        return reportsService.totalamountCurrency(reportId);
    }

    @GetMapping("/getReportsInDateRange")
    public List<Reports> getReportsInDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return reportsService.getReportsInDateRange(startDate, endDate);
    }

    @GetMapping("/getReportsSubmittedToUserInDateRange")
    public List<Reports> getReportsSubmittedToUserInDateRange(@RequestBody String managerEmail,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam String request) {
        return reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate,request);
    }

    @GetMapping("/getAmountOfReportsInDateRange")
    public String getAmountOfReportsInDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return reportsService.getAmountOfReportsInDateRange(startDate, endDate);
    }
}
