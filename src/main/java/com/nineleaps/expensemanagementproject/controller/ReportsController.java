package com.nineleaps.expensemanagementproject.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.DTO.ReportsDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class ReportsController {

	@Autowired
	private IReportsService reportsService;

	@GetMapping("/getAllReports/{employeeId}")
	public Set<Reports> getAllReports(@PathVariable Long employeeId) {
		return reportsService.getAllReports(employeeId);
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
	public Reports addReport(@RequestBody ReportsDTO reportsDTO, @PathVariable Long employeeId,
			@RequestParam List<Long> expenseIds) {
		return reportsService.addReport(reportsDTO, employeeId, expenseIds);
	}

	@PatchMapping("/addExpenseToReport/{reportId}")
	public Reports addExpensesToReport(@PathVariable Long reportId, @RequestParam List<Long> expenseIds) {
		return reportsService.addExpenseToReport(reportId, expenseIds);
	}

	@PostMapping("/submitReport/{reportId}")
	public void submitReport(@PathVariable Long reportId, HttpServletResponse response)
			throws MessagingException, IOException {

		reportsService.submitReport(reportId, response);
	}

	@PostMapping("/submitReportToManager/{reportId}")
	public void submitReport(@PathVariable Long reportId, @RequestParam String managerEmail,
			HttpServletResponse response) throws MessagingException, IOException {

		reportsService.submitReport(reportId, managerEmail, response);
	}

	@PatchMapping("/editReport/{reportId}")
	public List<Reports> editReport(@PathVariable Long reportId, @RequestParam String reportTitle,
			@RequestParam String reportDescription, @RequestParam List<Long> addExpenseIds,
			@RequestParam List<Long> removeExpenseIds) {
		return reportsService.editReport(reportId, reportTitle, reportDescription, addExpenseIds, removeExpenseIds);
	}

	@PostMapping("/approveReportByFinance")
	public void reimburseReportByFinance(@RequestParam ArrayList<Long> reportIds,
			@RequestParam(value = "comments", defaultValue = "null") String comments) {
		reportsService.reimburseReportByFinance(reportIds, comments);
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
		return reportsService.totalAmount(reportId);
	}

	@GetMapping("/getReportsInDateRange")
	public List<Reports> getReportsInDateRange(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam String request) {
		return reportsService.getReportsInDateRange(startDate, endDate, request);
	}

	@GetMapping("/getReportsSubmittedToUserInDateRange")
	public List<Reports> getReportsSubmittedToUserInDateRange(@RequestBody String managerEmail,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam String request) {
		return reportsService.getReportsSubmittedToUserInDateRange(managerEmail, startDate, endDate, request);
	}

	@GetMapping("/getAmountOfReportsInDateRange")
	public String getAmountOfReportsInDateRange(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		return reportsService.getAmountOfReportsInDateRange(startDate, endDate);
	}

	@GetMapping("/getTotalApprovedAmount")
	public float totalApprovedAmount(Long reportId) {
		return reportsService.totalApprovedAmount(reportId);
	}

	@PostMapping("/notifyHr/{reportId}")
	public void notifyHr(@RequestParam Long reportId, HttpServletResponse response) throws MessagingException, IOException {
		reportsService.notifyHR(reportId, response);
	}

	@PostMapping("/notifyLnD/{reportId}")
	public void notifyLnD(@RequestParam Long reportId , HttpServletResponse response) throws MessagingException, IOException {
		reportsService.notifyLnD(reportId,response);
	}

	@GetMapping("/numberOfExpenses/{reportId}")
	public int numberOfExpenses(@RequestParam Long reportId) {
		return reportsService.numberOfExpenses(reportId);
	}

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