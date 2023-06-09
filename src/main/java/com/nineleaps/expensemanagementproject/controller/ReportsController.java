package com.nineleaps.expensemanagementproject.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

	@GetMapping("/get_all_reports")
	public List<Reports> getAllReports() {
		return reportsService.getAllReports();
	}

	@GetMapping("/get_by_report_id/{report_id}")
	public Reports getReportByReportId(@PathVariable Long reportId) {
		return reportsService.getReportById(reportId);
	}

	@GetMapping("/get_report_by_employee_id/{employee_id}")
	public List<Reports> getReportByEmpId(@PathVariable Long employeeId) {
		return reportsService.getReportByEmpId(employeeId);
	}

	@GetMapping("/get_reports_submitted_to_user/{manager_email}")
	public List<Reports> getReportsSubmittedToUser(@PathVariable String managerEmail) {
		return reportsService.getReportsSubmittedToUser(managerEmail);
	}

	@GetMapping("/get_all_submitted_reports")
	public List<Reports> getAllSubmittedReports() {
		return reportsService.getAllSubmittedReports();
	}

	@GetMapping("/get_all_reports_approved_by_manager")
	public List<Reports> getAllReportsApprovedByManager() {
		return reportsService.getAllReportsApprovedByManager();
	}

	@PostMapping("/add_report/{employee_id}")
	public Reports addReport(@RequestBody Reports newReport, @PathVariable Long employeeId,
			@RequestParam ArrayList<Long> expenseIds) {
		return reportsService.addReport(newReport, employeeId, expenseIds);
	}

	@PatchMapping("/add_expense_to_report/{report_id}")
	public Reports addExpensesToReport(@PathVariable Long reportId, @RequestParam ArrayList<Long> expenseIds) {
		return reportsService.addExpenseToReport(reportId, expenseIds);
	}

	@PatchMapping("/remove_expenses_from_report/{report_id}")
	public Reports removeExpenseFromReport(@PathVariable Long reportId, @RequestParam ArrayList<Long> expenseIds) {
		return reportsService.removeExpenseFromReport(reportId, expenseIds);
	}

	@PostMapping("/submit_report/{report_id}")
	public void submitReport(@PathVariable Long reportId, @RequestParam String managerMail,
			HttpServletResponse response) throws AttributeNotFoundException, FileNotFoundException, MessagingException {
		try {

			response.setContentType("application/pdf");
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String currentDateTime = dateFormatter.format(new Date());
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
			response.setHeader(headerKey, headerValue);
			pdfGeneratorService.export(reportId, response);

			reportsService.submitReport(reportId, managerMail);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {

			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try {
				response.getWriter().write("Error exporting PDF");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				response.getWriter().flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		reportsService.submitReport(reportId, managerMail);
	}

	@PostMapping("/update_report/{report_id}")
	public Reports updateReport(@RequestBody Reports report, @PathVariable Long reportId) {
		return reportsService.updateReport(report, reportId);
	}

	@PatchMapping("/edit_report/{report_id}")
	public List<Reports> editReport(@PathVariable Long reportId, @RequestParam String reportTitle,
			@RequestParam String reportDescription, @RequestParam ArrayList<Long> addExpenseIds,
			@RequestParam ArrayList<Long> removeExpenseIds) {
		return reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
	}

	@PostMapping("/approve_report_by_manager/{report_id}")
	public void approveReportbymanager(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		reportsService.approveReportByManager(reportId, Comments);
	}

	@PostMapping("/reject_report_by_manager/{report_id}")
	public void rejectReportbymanager(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		reportsService.rejectReportByManager(reportId, Comments);
	}

	@PostMapping("/approve_report_by_finance/{report_id}")
	public void approveReportbyfinance(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		reportsService.approveReportByFinance(reportId, Comments);
	}

	@PostMapping("/reject_report_by_finance/{report_id}")
	public void rejectReportbyfinance(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		reportsService.rejectReportByFinance(reportId, Comments);
	}

	@PostMapping("/hide_report/{report_id}")
	public void hideReport(@PathVariable Long reportId) {
		reportsService.hideReport(reportId);
	}

	@GetMapping("/get_total_amount_inr_by_report_id")
	public float totalAmountINR(@RequestParam Long reportId) {
		return reportsService.totalamountINR(reportId);
	}

	@GetMapping("/get_total_amount_currency_by_report_id")
	public float totalAmountCurrency(@RequestParam Long reportId) {
		return reportsService.totalamountCurrency(reportId);
	}

	@GetMapping("/get_reports_in_date_range")
	public List<Reports> getReportsInDateRange(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		return reportsService.getReportsInDateRange(startDate, endDate);
	}

	@GetMapping("/get_reports_submitted_to_user_in_date_range")
	public List<Reports> getReportsSubmittedToUserInDateRange(@RequestBody String managerEmail,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		return reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate);
	}

	@GetMapping("/get_amount_of_reports_in_date_range")
	public String getAmountOfReportsInDateRange(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		return reportsService.getAmountOfReportsInDateRange(startDate, endDate);
	}
}