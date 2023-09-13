package com.nineleaps.expensemanagementproject.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.DTO.ReportsDTO;
import com.nineleaps.expensemanagementproject.entity.Reports;


public interface IReportsService {


    Set<Reports> getAllReports(Long employeeId);

    Reports getReportById(Long reportId);

    Reports addReport(ReportsDTO newReport, Long employeeId, List<Long> expenseids);

    List<Reports> editReport(Long reportId, String reportTitle, String reportDescription, List<Long> addExpenseIds,
                             List<Long> removeExpenseIds);

    List<Reports> getReportByEmpId(Long employeeId, String request);

    Reports addExpenseToReport(Long reportId, List<Long> employeeids);

    void submitReport(Long reportId, HttpServletResponse response) throws MessagingException, IOException;

    void submitReport(Long reportId, String managerEmail,
                      HttpServletResponse response) throws MessagingException, IOException;

    void rejectReportByFinance(Long reportId, String comments);

    List<Reports> getAllSubmittedReports();

    List<Reports> getReportsSubmittedToUser(String managerEmail, String request);

    List<Reports> getAllReportsApprovedByManager(String request);

    void hideReport(Long reportId);

    float totalAmountINR(Long reportId);

    float totalAmountCurrency(Long reportId);

    float totalApprovedAmountCurrency(Long reportId);

    float totalApprovedAmountINR(Long reportId);


    String getAmountOfReportsInDateRange(LocalDate startDate, LocalDate endDate);


    List<Reports> getReportsInDateRange(LocalDate startDate, LocalDate endDate, String request);

    List<Reports> getReportsSubmittedToUserInDateRange(String managerEmail, LocalDate startDate, LocalDate endDate,
                                                       String request);

    void reimburseReportByFinance(ArrayList<Long> reportIds, String comments);


//    void approveReportByManager(Long reportId, String comments,
//                                HttpServletResponse response) throws MessagingException, IOException;
//
//    void rejectReportByManager(Long reportId, String comments,
//                               HttpServletResponse response) throws MessagingException, IOException;
//


    void updateExpenseStatus(Long reportId, List<Long> approveExpenseIds, List<Long> rejectExpenseIds,
                             Map<Long, Float> partiallyApprovedMap, String reviewTime, String comments,
                             HttpServletResponse response) throws MessagingException, IOException;

    void notifyHR(Long reportId) throws MessagingException;

    void notifyLnD(Long reportId) throws MessagingException;

    int numberOfExpenses(Long reportId);
}