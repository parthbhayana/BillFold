package com.nineleaps.expense_management_project.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expense_management_project.entity.StatusExcel;
import com.nineleaps.expense_management_project.service.IExcelGeneratorCategoryService;
import com.nineleaps.expense_management_project.service.IExcelGeneratorReportsService;

@RestController
public class ExcelController {
	@Autowired
	private IExcelGeneratorCategoryService excelService;

	@Autowired
	private IExcelGeneratorReportsService excelServiceReports;

	private static final String CONSTANT1 = "Email sent successfully!";
	private static final String CONSTANT2 = "Content-Disposition";
	private static final String CONSTANT3 = "No data available for the selected period. So, Email can't be sent!";

	// Endpoint to generate an Excel report for category-wise expense analytics and send it via email
	@GetMapping("/excel/categoryBreakup")
	public ResponseEntity<String> generateExcelReport(HttpServletResponse response,
													  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
													  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws Exception {

		response.setContentType("application/octet-stream");

		String headerKey = CONSTANT2;
		String headerValue = "attachment;filename=Category wise Expense Analytics.xls";

		response.setHeader(headerKey, headerValue);

		String result = excelService.generateExcelAndSendEmail(response, startDate, endDate);

		if (result.equals(CONSTANT1)) {
			response.flushBuffer();
			return ResponseEntity.ok(result);
		} else if (result.equals(CONSTANT3)) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.badRequest().body(result);
		}
	}

	// Endpoint to generate an Excel report for all reports with a specified status and send it via email
	@GetMapping("/excel/allReports")
	public ResponseEntity<String> generateExcelReport(HttpServletResponse response,
													  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
													  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
													  @RequestParam StatusExcel status) throws Exception {

		String fileName = "Billfold_All_Submissions_Status.xls";
		String headerKey = CONSTANT2;
		String headerValue = "attachment; filename=" + fileName;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader(headerKey, headerValue);

		String result = excelServiceReports.generateExcelAndSendEmail(response, startDate, endDate, status);

		if (result.equals(CONSTANT1)) {
			response.flushBuffer();
			return ResponseEntity.ok(result);
		} else if (result.equals(CONSTANT3)) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.badRequest().body(result);
		}
	}

	// Endpoint to reimburse and generate an Excel report for all reports and send it via email
	@GetMapping("/reimburse/allReports")
	public ResponseEntity<String> reimburseAndGenerateExcel(HttpServletResponse response) throws Exception {

		String fileName = "Billfold_All_Submissions_Status.xls";
		String headerKey = CONSTANT2;
		String headerValue = "attachment; filename=" + fileName;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader(headerKey, headerValue);

		String result = excelServiceReports.reimburseAndGenerateExcel(response);

		if (result.equals(CONSTANT1)) {
			response.flushBuffer();
			return ResponseEntity.ok(result);
		} else if (result.equals(CONSTANT3)) {
			return ResponseEntity.ok(result);
		}else {
			return ResponseEntity.badRequest().body(result);
		}


    }
}

