package com.nineleaps.expensemanagementproject.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.entity.StatusExcel;
import com.nineleaps.expensemanagementproject.service.IExcelGeneratorCategoryService;
import com.nineleaps.expensemanagementproject.service.IExcelGeneratorReportsService;

@RestController
public class ExcelController {
	@Autowired
	private IExcelGeneratorCategoryService excelService;
	
	@Autowired
	private IExcelGeneratorReportsService excelServiceReports;

	@GetMapping("/excel/categoryBreakup")
	public ResponseEntity<String> generateExcelReport(HttpServletResponse response,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws Exception {

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=Category wise Expense Analytics.xls";

		response.setHeader(headerKey, headerValue);

		String result = excelService.generateExcelAndSendEmail(response, startDate, endDate);

		if (result.equals("Email sent successfully!")) {
			response.flushBuffer();
			return ResponseEntity.ok(result);
		} else if (result.equals("No data available for the selected period.So, Email can't be sent!")) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.badRequest().body(result);
		}
	}
	
	
	@GetMapping("/excel/allReports")

	public ResponseEntity<String> generateExcelReport(HttpServletResponse response,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
			@RequestParam StatusExcel status) throws Exception {

		String fileName = "Billfold_All_Submissions_Status.xls";
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader(headerKey, headerValue);

		String result = excelServiceReports.generateExcelAndSendEmail(response, startDate, endDate, status);

		if (result.equals("Email sent successfully!")) {
			response.flushBuffer();
			return ResponseEntity.ok(result);
		} else if (result.equals("No data available for the selected period.So, Email can't be sent!")) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.badRequest().body(result);
		}
	}
}