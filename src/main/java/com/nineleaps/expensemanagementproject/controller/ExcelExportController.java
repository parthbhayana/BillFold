package com.nineleaps.expensemanagementproject.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.service.ExcelGeneratorServiceImpl;

@RestController
public class ExcelExportController {
	@Autowired
	private ExcelGeneratorServiceImpl excelservice;
	
	
	@GetMapping("/excel")
	public void generateExcelReport(HttpServletResponse response, @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	@RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws Exception{
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=Category wise Expense Analytics.xls";

		response.setHeader(headerKey, headerValue);
		
		excelservice.generateExcelAndSendEmail(response,startDate, endDate);
		
		response.flushBuffer();
	}

}