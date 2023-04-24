package com.nineleaps.expensemanagementproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.service.IReportsService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class ReportsController {

	@Autowired
	private IReportsService reportsService;

	@GetMapping("/getallreports")
	public List<Reports> getAllReports() {
		return reportsService.getAllReports();
	}

	@DeleteMapping("/deletereport/{reportid}")
	public void deleteReportById(@PathVariable("reportid") Long reportId) {
		reportsService.deleteReportById(reportId);
	}

	@GetMapping("/getbyreportid/{report_id}")
	public Reports getReportByReportId(@PathVariable("report_id") Long reportId) {
		return reportsService.getReportById(reportId);
	}

	@GetMapping("/getreportbyemployeeid")
	public List<Reports> getReportByEmpId(@RequestParam Long EmployeeId) {

		return reportsService.getReportByEmpId(EmployeeId);
	}

	@PostMapping("/addreport")
	public Reports addReport(@RequestBody Reports newReport) {
		return reportsService.addReport(newReport);
	}

	@PatchMapping("/addexpensetoreport")
	public Reports addExpenseToReport(@RequestParam Long reportId, @RequestParam Long expenseid) {
		return reportsService.addExpenseToReport(reportId, expenseid);
	}

	@PostMapping("/addReportComments")
	public Reports addReportComments(@RequestBody Reports report, @RequestParam Long reportId) {
		return reportsService.addReportComments(report, reportId);
	}

	@PostMapping("/submitReport")
	public Reports submitReport(@RequestParam Long reportId) {
		return reportsService.submitReport(reportId);
	}

	@PostMapping("/updateReport")
	public Reports updateReport(@RequestBody Reports report, @RequestParam Long reportId) {
		return reportsService.updateReport(report, reportId);
	}

	@PostMapping("/approveReport")
	public Reports approveReport(@RequestParam Long reportId) {
		return reportsService.approveReport(reportId);
	}

	@PostMapping("/rejectReport")
	public Reports rejectReport(@RequestParam Long reportId) {
		return reportsService.rejectReport(reportId);
	}

}
