package com.nineleaps.expensemanagementproject.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expensemanagementproject.service.ExcelGeneratorServiceCategoryBreakupImpl;

@RestController
public class ExcelCategoryBreakupController {
	@Autowired
	private ExcelGeneratorServiceCategoryBreakupImpl excelservice;

	@GetMapping("/excel/category_breakup")
	public ResponseEntity<String> generateExcelReport(HttpServletResponse response,
			@RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws Exception {

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=Category wise Expense Analytics.xls";

		response.setHeader(headerKey, headerValue);

		String result = excelservice.generateExcelAndSendEmail(response, startDate, endDate);

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