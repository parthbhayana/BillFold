package com.nineleaps.expensemanagementproject.service;

import java.io.ByteArrayOutputStream;

import java.time.LocalDate;

import java.util.List;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.FinanceApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.entity.StatusExcel;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service

public class ExcelGeneratorServiceAllSubmissionsImpl {

	@Autowired
	private ReportsRepository reportRepo;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private IExpenseService expenseService;

	public String generateExcelAndSendEmail(HttpServletResponse response, LocalDate startDate, LocalDate endDate,
			StatusExcel status) throws Exception {

		ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
		generateExcel(excelStream, startDate, endDate, status);
		byte[] excelBytes = excelStream.toByteArray();

		boolean emailsent = sendEmailWithAttachment("arjntomr9611@gmail.com", "BillFold:Excel Report",
				"Please find the attached Excel report.", excelBytes, "report.xls");
		if (emailsent) {
	        return "Email sent successfully!";
	    } else {
	        return "Failed to send email.";
	    }
	}

	public void generateExcel(ByteArrayOutputStream excelStream, LocalDate startDate, LocalDate endDate,
			StatusExcel status) throws Exception {

		if (status == StatusExcel.ALL) {
			List<Reports> reportlist = reportRepo.findByDateSubmittedBetween(startDate, endDate);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Billfold_All_reports_Pending_Reimbursed");
			HSSFRow row = sheet.createRow(0);

			row.createCell(0).setCellValue("Sl.no.");
			row.createCell(1).setCellValue("Employee Email");
			row.createCell(2).setCellValue("Employee Name");
			row.createCell(3).setCellValue("Report Id");
			row.createCell(4).setCellValue("Report Name");
			row.createCell(5).setCellValue("submitted on");
			row.createCell(6).setCellValue("Appproved  on");
			row.createCell(7).setCellValue("Approved by");
			row.createCell(8).setCellValue("Total Amount(INR)");
			row.createCell(9).setCellValue("Status");

			int dataRowIndex = 1;
			int sl = 1;

			for (Reports report : reportlist) {

				HSSFRow dataRow = sheet.createRow(dataRowIndex);
				dataRow.createCell(0).setCellValue(sl);
				dataRow.createCell(1).setCellValue(report.getEmployeeMail());
				Long id = report.getReportId();
				List<Expense> expenseList = expenseService.getExpenseByReportId(id);

				if (!expenseList.isEmpty()) {
					Expense expense = expenseList.get(0);
					Employee employee = expense.getEmployee();
					dataRow.createCell(2).setCellValue(employee.getFirstName() + " " + employee.getLastName());
					dataRow.createCell(3).setCellValue(report.getReportId());
					dataRow.createCell(4).setCellValue(report.getReportTitle());
					dataRow.createCell(5).setCellValue(report.getDateSubmitted().toString());
					LocalDate managerActionDate = report.getManagerActionDate();
					if (managerActionDate != null) {
						dataRow.createCell(6).setCellValue(managerActionDate.toString());
					}
					dataRow.createCell(7).setCellValue(report.getManagerEmail());
					dataRow.createCell(8).setCellValue(report.getTotalAmountINR());
					dataRow.createCell(9).setCellValue("Pending/Reimbursed");

					dataRowIndex++;
					sl++;
				}
			}

			// ServletOutputStream ops = response.getOutputStream();
			workbook.write(excelStream);
			workbook.close();
			// ops.close();

		}

		if (status == StatusExcel.PENDING) {
			List<Reports> reportlist = reportRepo.findByDateSubmittedBetween(startDate, endDate);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Billfold_All_reports_Pending");
			HSSFRow row = sheet.createRow(0);

			row.createCell(0).setCellValue("Sl.no.");
			row.createCell(1).setCellValue("Employee Email");
			row.createCell(2).setCellValue("Employee Name");
			row.createCell(3).setCellValue("Report Id");
			row.createCell(4).setCellValue("Report Name");
			row.createCell(5).setCellValue("submitted on");
			row.createCell(6).setCellValue("Appproved  on");
			row.createCell(7).setCellValue("Approved by");
			row.createCell(8).setCellValue("Total Amount(INR)");
			row.createCell(9).setCellValue("Status");

			int dataRowIndex = 1;
			int sl = 1;

			for (Reports report : reportlist) {
				if (report.getFinanceapprovalstatus() == FinanceApprovalStatus.PENDING) {
					HSSFRow dataRow = sheet.createRow(dataRowIndex);
					dataRow.createCell(0).setCellValue(sl);
					dataRow.createCell(1).setCellValue(report.getEmployeeMail());
					Long id = report.getReportId();
					List<Expense> expenseList = expenseService.getExpenseByReportId(id);

					if (!expenseList.isEmpty()) {
						Expense expense = expenseList.get(0);
						Employee employee = expense.getEmployee();
						dataRow.createCell(2).setCellValue(employee.getFirstName() + " " + employee.getLastName());
						dataRow.createCell(3).setCellValue(report.getReportId());
						dataRow.createCell(4).setCellValue(report.getReportTitle());
						dataRow.createCell(5).setCellValue(report.getDateSubmitted().toString());
						LocalDate managerActionDate = report.getManagerActionDate();
						if (managerActionDate != null) {
							dataRow.createCell(6).setCellValue(managerActionDate.toString());
						}
						dataRow.createCell(7).setCellValue(report.getManagerEmail());
						dataRow.createCell(8).setCellValue(report.getTotalAmountINR());
						dataRow.createCell(9).setCellValue("Pending");

						dataRowIndex++;
						sl++;
					}
				}
			}
			// ServletOutputStream ops = response.getOutputStream();
			workbook.write(excelStream);
			workbook.close();
			// ops.close();

		}

		if (status == StatusExcel.REIMBURSED) {
			List<Reports> reportlist = reportRepo.findByDateSubmittedBetween(startDate, endDate);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Billfold_All_reports_Reimbursed");
			HSSFRow row = sheet.createRow(0);

			row.createCell(0).setCellValue("Sl.no.");
			row.createCell(1).setCellValue("Employee Email");
			row.createCell(2).setCellValue("Employee Name");
			row.createCell(3).setCellValue("Report Id");
			row.createCell(4).setCellValue("Report Name");
			row.createCell(5).setCellValue("submitted on");
			row.createCell(6).setCellValue("Appproved  on");
			row.createCell(7).setCellValue("Approved by");
			row.createCell(8).setCellValue("Total Amount(INR)");
			row.createCell(9).setCellValue("Status");

			int dataRowIndex = 1;
			int sl = 1;
			for (Reports report : reportlist) {
				if (report.getFinanceapprovalstatus() == FinanceApprovalStatus.REIMBURSED) {
					HSSFRow dataRow = sheet.createRow(dataRowIndex);
					dataRow.createCell(0).setCellValue(sl);
					dataRow.createCell(1).setCellValue(report.getEmployeeMail());
					Long id = report.getReportId();
					List<Expense> expenseList = expenseService.getExpenseByReportId(id);

					if (!expenseList.isEmpty()) {
						Expense expense = expenseList.get(0);
						Employee employee = expense.getEmployee();
						dataRow.createCell(2).setCellValue(employee.getFirstName() + " " + employee.getLastName());
						dataRow.createCell(3).setCellValue(report.getReportId());
						dataRow.createCell(4).setCellValue(report.getReportTitle());
						dataRow.createCell(5).setCellValue(report.getDateSubmitted().toString());
						LocalDate managerActionDate = report.getManagerActionDate();
						if (managerActionDate != null) {
							dataRow.createCell(6).setCellValue(managerActionDate.toString());
						}
						dataRow.createCell(7).setCellValue(report.getManagerEmail());
						dataRow.createCell(8).setCellValue(report.getTotalAmountINR());
						dataRow.createCell(9).setCellValue("Reimbursed");

						dataRowIndex++;
						sl++;
					}
				}
			}

			// ServletOutputStream ops = response.getOutputStream();
			workbook.write(excelStream);
			workbook.close();
//			ops.close();

		}
	}

	private boolean sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachmentContent,
			String attachmentFilename) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(body);

			DataSource attachment = new ByteArrayDataSource(attachmentContent, "application/vnd.ms-excel");
			helper.addAttachment(attachmentFilename, attachment);

			mailSender.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}
}
