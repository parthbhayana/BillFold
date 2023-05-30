package com.nineleaps.expensemanagementproject.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.web.bind.annotation.DeleteMapping;
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

	@GetMapping("/getallreports")
	public List<Reports> getAllReports() {
		return reportsService.getAllReports();
	}

	@GetMapping("/getbyreportid/{report_id}")
	public Reports getReportByReportId(@PathVariable("report_id") Long reportId) {
		return reportsService.getReportById(reportId);
	}

	@GetMapping("/getreportbyemployeeid/{employeeId}")
	public List<Reports> getReportByEmpId(@PathVariable Long employeeId) {
		return reportsService.getReportByEmpId(employeeId);
	}

	@GetMapping("/getreportssubmittedtouser/{manageremail}")
	public List<Reports> getReportsSubmittedToUser(@PathVariable("manageremail") String managerEmail) {
		return reportsService.getReportsSubmittedToUser(managerEmail);
	}

	@GetMapping("/getallsubmittedreports")
	public List<Reports> getAllSubmittedReports() {
		return reportsService.getAllSubmittedReports();
	}

	@GetMapping("/getallreportsapprovedbymanager")
	public List<Reports> getAllReportsApprovedByManager() {
		return reportsService.getAllReportsApprovedByManager();
	}

	@PostMapping("/addreport/{empId}")
	public Reports addReport(@RequestBody Reports newReport, @PathVariable("empId") Long employeeId,
			@RequestParam ArrayList<Long> expenseIds) {
		return reportsService.addReport(newReport, employeeId, expenseIds);
	}

	@PatchMapping("/addexpensetoreport/{reportId}")
	public Reports addExpensesToReport(@PathVariable Long reportId, @RequestParam ArrayList<Long> expenseIds) {
		return reportsService.addExpenseToReport(reportId, expenseIds);
	}

	@PatchMapping("/removeexpensesfromreport/{reportId}")
	public Reports removeExpenseFromReport(@PathVariable Long reportId, @RequestParam ArrayList<Long> expenseIds) {
		return reportsService.removeExpenseFromReport(reportId, expenseIds);
	}

	@PostMapping("/submitReport/{reportId}")
	public void submitReport(@PathVariable Long reportId, @RequestParam String managerMail,
			HttpServletResponse response) throws AttributeNotFoundException {
		System.out.print(
				"---------------------------------------------------------------------------------" + managerMail);
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

	@PostMapping("/updateReport/{reportId}")
	public Reports updateReport(@RequestBody Reports report, @PathVariable Long reportId) {
		return reportsService.updateReport(report, reportId);
	}

	@PatchMapping("/editreport/{reportId}")
	public List<Reports> editReport(@PathVariable Long reportId, @RequestParam String reportTitle,
			@RequestParam String reportDescription, @RequestParam ArrayList<Long> addExpenseIds,
			@RequestParam ArrayList<Long> removeExpenseIds) {
		return reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
	}

	@PostMapping("/approveReportByManager/{reportId}")
	public void approveReportbymanager(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		reportsService.approveReportByManager(reportId, Comments);
	}

	@PostMapping("/rejectReportByManager/{reportId}")
	public void rejectReportbymanager(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		reportsService.rejectReportByManager(reportId, Comments);
	}

	@PostMapping("/approveReportByFinance/{reportId}")
	public void approveReportbyfinance(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		reportsService.approveReportByFinance(reportId, Comments);
	}

	@PostMapping("/rejectReportByFinance/{reportId}")
	public void rejectReportbyfinance(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		reportsService.rejectReportByFinance(reportId, Comments);
	}

	@PostMapping("/hidereport/{reportId}")
	public void hideReport(@PathVariable Long reportId) {
		reportsService.hideReport(reportId);
	}

	@GetMapping("/gettotalamountinrbyreportid")
	public float totalAmountINR(@RequestParam Long reportId) {
		return reportsService.totalamountINR(reportId);
	}

	@GetMapping("/gettotalamountcurrencybyreportid")
	public float totalAmountCurrency(@RequestParam Long reportId) {
		return reportsService.totalamountCurrency(reportId);
	}

	@GetMapping("/getreportsindaterange")
	public List<Reports> getReportsInDateRange(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		return reportsService.getReportsInDateRange(startDate, endDate);
	}

	@GetMapping("/getreportssubmittedtouserindaterange")
	public List<Reports> getReportsSubmittedToUserInDateRange(@RequestBody String managerEmail,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		return reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate);
	}

	@GetMapping("/getamountofreportsindaterange")
	public String getAmountOfReportsInDateRange(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		return reportsService.getAmountOfReportsInDateRange(startDate, endDate);
	}
}