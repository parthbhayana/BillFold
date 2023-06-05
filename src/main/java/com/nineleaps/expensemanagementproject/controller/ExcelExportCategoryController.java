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
public class ExcelExportCategoryController {
	@Autowired
	private ExcelGeneratorServiceCategoryBreakupImpl excelservice;
	
	
	@GetMapping("/excel/categorybreakup")
	public ResponseEntity<String> generateExcelReport(HttpServletResponse response, @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	@RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws Exception{
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=Category wise Expense Analytics.xls";

		response.setHeader(headerKey, headerValue);
		
		excelservice.generateExcelAndSendEmail(response,startDate, endDate);
		
		response.flushBuffer();
		return ResponseEntity.ok("Mail sent successfully!");
	}

}