package com.nineleaps.expensemanagementproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	public void deleteReport(@PathVariable("Reportid") Long reportId) {
		reportsService.deleteReport(reportId);
	}

	@GetMapping("/getbyreportid/{reportid}")
	public Reports getReportById(@PathVariable(name = "ReportId") Long ReportId) {
		return reportsService.getReportByReportId(ReportId);
	}

	@PostMapping("/addreport/{expenseid}")
	public Reports addReport(@RequestBody Reports newReport, @PathVariable Long expenseid) {
		return reportsService.addReport(newReport, expenseid);
	}
}
