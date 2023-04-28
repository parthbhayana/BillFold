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

	@GetMapping("/getallreportsapprovedbymanager")
	public List<Reports> getAllReportsApprovedByManager() {
		return reportsService.getAllReportsApprovedByManager();
	}

	@PostMapping("/addreport/{empId}")
	public Reports addReport(@RequestBody Reports newReport, @PathVariable("empId") Long employeeId) {
		return reportsService.addReport(newReport, employeeId);
	}

	@PatchMapping("/addexpensetoreport/{reportId}/{expenseid}")
	public Reports addExpenseToReport(@PathVariable Long reportId, @PathVariable Long expenseid) {
		return reportsService.addExpenseToReport(reportId, expenseid);
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
	public Reports approveReportbymanager(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		return reportsService.approveReportByManager(reportId, Comments);
	}

	@PostMapping("/rejectReportByManager/{reportId}")
	public Reports rejectReportbymanager(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		return reportsService.rejectReportByManager(reportId, Comments);
	}

	@PostMapping("/approveReportByFinance/{reportId}")
	public Reports approveReportbyfinance(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		return reportsService.approveReportByFinance(reportId, Comments);
	}

	@PostMapping("/rejectReportByFinance/{reportId}")
	public Reports rejectReportbyfinance(@PathVariable Long reportId,
			@RequestParam(value = "Comments", defaultValue = "null") String Comments) {
		return reportsService.rejectReportByFinance(reportId, Comments);
	}

}
