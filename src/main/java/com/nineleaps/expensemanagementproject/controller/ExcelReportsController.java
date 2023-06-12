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
import com.nineleaps.expensemanagementproject.service.IExcelGeneratorReportsService;

@RestController
public class ExcelReportsController {
	@Autowired
	private IExcelGeneratorReportsService excelserviceallsubmissions;

	@GetMapping("/excel/all_submissions_status")

	public ResponseEntity<String> generateExcelReport(HttpServletResponse response,
			@RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

			@RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
			@RequestParam StatusExcel status) throws Exception {

		String fileName = "Billfold_All_Submissions_Status.xls";
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader(headerKey, headerValue);

		String result = excelserviceallsubmissions.generateExcelAndSendEmail(response, startDate, endDate, status);

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