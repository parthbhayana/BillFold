package com.nineleaps.expensemanagementproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nineleaps.expensemanagementproject.dto.FinanceComments;
import com.nineleaps.expensemanagementproject.dto.ManagerComments;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.service.IReportsService;

@RestController
public class ReportsController {

	@Autowired
	private IReportsService reportsService;

	@GetMapping("/getallreports")
	public List<Reports> getAllReports() {
		return reportsService.getAllReports();
	}

//	@DeleteMapping("/deletereport/{reportid}")
//	public void deleteReportById(@PathVariable("reportid") Long reportId) {
//		reportsService.deleteReportById(reportId);
//	}

	@GetMapping("/getbyreportid/{report_id}")
	public Reports getReportByReportId(@PathVariable("report_id") Long reportId) {
		return reportsService.getReportById(reportId);
	}

	@GetMapping("/getreportbyemployeeid/{EmployeeId}")
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
	public Reports addReport(@RequestBody Reports newReport, @PathVariable("empId") Long employeeId,
			@RequestParam ArrayList<Long> expenseIds) {
		return reportsService.addReport(newReport, employeeId, expenseIds);
	}

	@PatchMapping("/addexpensetoreport/{reportId}")
	public Reports addExpensesToReport(@PathVariable Long reportId, @RequestParam List<Long> expenseIds) {
		return reportsService.addExpenseToReport(reportId, expenseIds);
	}

	@PostMapping("/submitReport/{reportId}")
	public Reports submitReport(@PathVariable Long reportId, @RequestBody String managerMail) {
		return reportsService.submitReport(reportId, managerMail);
	}

	@PostMapping("/updateReport/{reportId}")
	public Reports updateReport(@RequestBody Reports report, @PathVariable Long reportId) {
		return reportsService.updateReport(report, reportId);
	}

	@PostMapping("/approveReportByManager/{reportId}")
	public Reports approveReportbymanager(@PathVariable Long reportId,
			@org.springframework.web.bind.annotation.RequestBody ManagerComments managerComments) {
		String var = managerComments.getManagerComments();
		return reportsService.approveReportByManager(reportId, var);
	}

	@PostMapping("/rejectReportByManager/{reportId}")
	public Reports rejectReportbymanager(@PathVariable Long reportId,
			@org.springframework.web.bind.annotation.RequestBody ManagerComments managerComments) {
		String var = managerComments.getManagerComments();
		return reportsService.rejectReportByManager(reportId, var);
	}

	@PostMapping("/approveReportByFinance/{reportId}")
	public Reports approveReportbyfinance(@PathVariable Long reportId,
			@org.springframework.web.bind.annotation.RequestBody FinanceComments financeComments) {
		String var = financeComments.getFinanceComments();
		return reportsService.approveReportByFinance(reportId, var);
	}

	@PostMapping("/rejectReportByFinance/{reportId}")
	public Reports rejectReportbyfinance(@PathVariable Long reportId,
			@org.springframework.web.bind.annotation.RequestBody FinanceComments financeComments) {
		String var = financeComments.getFinanceComments();
		return reportsService.rejectReportByFinance(reportId, var);
	}

	@PostMapping("/hidereport/{reportId}")
	public void hideReport(@PathVariable Long reportId) {
		reportsService.hideReport(reportId);
	}
}