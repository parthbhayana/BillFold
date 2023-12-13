package com.nineleaps.expensemanagementproject.controller;


/**
		  ExcelController

		      Manages endpoints related to generating and handling Excel reports for expense categories and reimbursement submissions.

		      ## Endpoints

		      ### GET /excel/categoryBreakup

		      - **Description:** Generates an Excel report for category-wise expense analytics within a specified date range.
		      - **Request Parameters:**
		          - startDate (LocalDate) - Start date for analytics.
		          - endDate (LocalDate) - End date for analytics.
		      - **Response:** Sends an Excel file with category-wise expense analytics.

		      ### GET /excel/allReports

		      - **Description:** Generates an Excel report for all submissions' status within a specified date range and status.
		      - **Request Parameters:**
		          - startDate (LocalDate) - Start date for report.
		          - endDate (LocalDate) - End date for report.
		          - status (StatusExcel) - Status of submissions.
		      - **Response:** Sends an Excel file with submission status details.

		      ### GET /reimburse/allReports

		      - **Description:** Generates an Excel report for reimbursement submissions' status.
		      - **Response:** Sends an Excel file with reimbursement submissions' status details.

		      ## Note
		      - These endpoints generate Excel reports based on specified criteria and respond with the generated Excel files or relevant messages.
		      - The `generateExcelAndSendEmail` and `reimburseAndGenerateExcel` methods handle the Excel generation and sending logic.
 */
import java.time.LocalDate;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expensemanagementproject.entity.StatusExcel;
import com.nineleaps.expensemanagementproject.service.IExcelGeneratorCategoryService;
import com.nineleaps.expensemanagementproject.service.IExcelGeneratorReportsService;

@RestController
@Slf4j
@RequestMapping("/api/v1/excel")
public class ExcelController {

	@Autowired
	private IExcelGeneratorCategoryService excelService;

	@Autowired
	private IExcelGeneratorReportsService excelServiceReports;

	private static final String CONSTANT1 = "Email sent successfully!";
	private static final String CONSTANT2 = "Content-Disposition";
	private static final String CONSTANT3 = "No data available for the selected period.So, Email can't be sent!";

	@GetMapping("/excel/categoryBreakup")
	@PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
	public ResponseEntity<String> generateCategoryBreakupExcel(HttpServletResponse response,
															   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
															   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws Exception {
		log.info("Generating Excel for category breakup from {} to {}", startDate, endDate);

		response.setContentType("application/octet-stream");
		String headerKey = CONSTANT2;
		String headerValue = "attachment;filename=Category_wise_Expense_Analytics.xls";
		response.setHeader(headerKey, headerValue);

		String result = excelService.generateExcelAndSendEmail(response, startDate, endDate);

		if (result.equals(CONSTANT1)) {
			response.flushBuffer();
			log.info("Excel generated and sent successfully");
			return ResponseEntity.ok(result);
		} else if (result.equals(CONSTANT3)) {
			log.info("No records found for the given date range");
			return ResponseEntity.ok(result);
		} else {
			log.error("Error generating Excel: {}", result);
			return ResponseEntity.badRequest().body(result);
		}
	}


	@GetMapping("/excel/allReports")
	@PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
	public ResponseEntity<String> generateAllReportsExcel(HttpServletResponse response,
														  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
														  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
														  @RequestParam StatusExcel status) throws Exception {
		log.info("Generating Excel for all reports from {} to {} with status {}", startDate, endDate, status);

		String fileName = "Billfold_All_Submissions_Status.xls";
		String headerKey = CONSTANT2;
		String headerValue = "attachment; filename=" + fileName;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader(headerKey, headerValue);

		String result = excelServiceReports.generateExcelAndSendEmail(response, startDate, endDate, status);

		if (result.equals(CONSTANT1)) {
			response.flushBuffer();
			log.info("Excel for all reports generated and sent successfully");
			return ResponseEntity.ok(result);
		} else if (result.equals(CONSTANT3)) {
			log.info("No records found for the given date range and status");
			return ResponseEntity.ok(result);
		} else {
			log.error("Error generating Excel for all reports: {}", result);
			return ResponseEntity.badRequest().body(result);
		}
	}

	@GetMapping("/reimburse/allReports")
	@PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
	public ResponseEntity<String> reimburseAndGenerateExcel(HttpServletResponse response) throws Exception {
		log.info("Initiating reimbursement process and generating Excel report");

		String fileName = "Billfold_All_Submissions_Status.xls";
		String headerKey = CONSTANT2;
		String headerValue = "attachment; filename=" + fileName;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader(headerKey, headerValue);

		String result = excelServiceReports.reimburseAndGenerateExcel(response);

		if (result.equals(CONSTANT1)) {
			response.flushBuffer();
			log.info("Reimbursement process and Excel generation completed successfully");
			return ResponseEntity.ok(result);
		} else if (result.equals(CONSTANT3)) {
			log.info("No records found for reimbursement and Excel generation");
			return ResponseEntity.ok(result);
		} else {
			log.error("Error during reimbursement process and Excel generation: {}", result);
			return ResponseEntity.badRequest().body(result);
		}
	}


}
