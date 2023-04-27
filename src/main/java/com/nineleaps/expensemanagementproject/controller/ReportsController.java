package com.nineleaps.expensemanagementproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping("/getreportbyemployeeid/{employeeId}")
	public List<Reports> getReportByEmpId(@PathVariable Long EmployeeId) {
		return reportsService.getReportByEmpId(EmployeeId);
	}

	@GetMapping("/getreportssubmittedtouser/{manageremail}")
	public List<Reports> getReportsSubmittedToUser(@PathVariable("manageremail") String managerEmail) {
		return reportsService.getReportsSubmittedToUser(managerEmail);
	}

	@GetMapping("/getallsubmittedreports")
	public List<Reports> getAllSubmittedReports() {
		return reportsService.getAllSubmittedReports();
	}

	@PostMapping("/addreport/{empId}")
	public Reports addReport(@RequestBody Reports newReport, @PathVariable("empId") Long employeeId) {
		return reportsService.addReport(newReport, employeeId);
	}

	@PatchMapping("/addexpensetoreport/{reportId}/{expenseid}")
	public Reports addExpenseToReport(@PathVariable Long reportId, @PathVariable Long expenseid) {
		return reportsService.addExpenseToReport(reportId, expenseid);
	}

	@PostMapping("/addReportComments/{reportId}")
	public Reports addReportComments(@RequestBody Reports report, @PathVariable Long reportId) {
		return reportsService.addReportComments(report, reportId);
	}

	@PostMapping("/submitReport/{reportId}")
	public Reports submitReport(@PathVariable Long reportId) {
		return reportsService.submitReport(reportId);
	}

	@PostMapping("/updateReport/{reportId}")
	public Reports updateReport(@RequestBody Reports report, @PathVariable Long reportId) {
		return reportsService.updateReport(report, reportId);
	}

	@PostMapping("/approveReportByManager/{reportId}")
	public Reports approveReportbymanager(@PathVariable Long reportId) {
		return reportsService.approveReportByManager(reportId);
	}

	@PostMapping("/rejectReportByManager/{reportId}")
	public Reports rejectReportbymanager(@PathVariable Long reportId) {
		return reportsService.rejectReportByManager(reportId);
	}

	@PostMapping("/approveReportByFinance/{reportId}")
	public Reports approveReportbyfinance(@PathVariable Long reportId) {
		return reportsService.approveReportByFinance(reportId);
	}

	@PostMapping("/rejectReportByFinance/{reportId}")
	public Reports rejectReportbyfinance(@PathVariable Long reportId) {
		return reportsService.rejectReportByFinance(reportId);
	}

}
