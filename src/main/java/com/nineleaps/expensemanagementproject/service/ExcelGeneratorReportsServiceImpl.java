package com.nineleaps.expensemanagementproject.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service

public class ExcelGeneratorReportsServiceImpl implements IExcelGeneratorReportsService {

	@Autowired
	ReportsRepository reportRepository;

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	IExpenseService expenseService;

	@Autowired
	EmployeeRepository employeeRepository;
	private static final String CONSTANT1 = "Sl.no.";
	private static final String CONSTANT3 = "Employee Official Id";
	private static final String CONSTANT2 = "Employee Email";
	private static final String CONSTANT4 = "Employee Name";
	private static final String CONSTANT5 = "Report Id";
	private static final String CONSTANT6 = "Report Name";
	private static final String CONSTANT7 = "submitted on";
	private static final String CONSTANT8 = "Month";
	private static final String CONSTANT9 = "Approved on";
	private static final String CONSTANT10 = "Approved by";
	private static final String CONSTANT11 = "Total Amount(INR)";
	private static final String CONSTANT12 = "Status";

	@Override
	public String generateExcelAndSendEmail(HttpServletResponse response, LocalDate startDate, LocalDate endDate,
			StatusExcel status) throws Exception {

		List<Reports> reportlist = reportRepository.findByDateSubmittedBetween(startDate, endDate);

		if (reportlist.isEmpty()) {
			return "No data available for the selected period.So, Email can't be sent!";
		}

		ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
		generateExcel(excelStream, startDate, endDate, status);
		byte[] excelBytes = excelStream.toByteArray();

		Employee financeAdmin = employeeRepository.findByRole("FINANCE_ADMIN");
		if (financeAdmin == null) {
			throw new IllegalStateException("Finance admin cannot found. So, Email can't be send");
		}

		boolean emailSent = sendEmailWithAttachment(financeAdmin.getEmployeeEmail(), "BillFold:Excel Report",
				"Please find the attached Excel report.", excelBytes, "report.xls");
		if (emailSent) {
			return "Email sent successfully!";
		} else {
			return "Email not sent";
		}
	}

	@Override
	public void generateExcel(ByteArrayOutputStream excelStream, LocalDate startDate, LocalDate endDate,
			StatusExcel status) throws Exception {

		if (status == StatusExcel.ALL) {
			List<Reports> reportlist = reportRepository.findByDateSubmittedBetween(startDate, endDate);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Billfold_All_reports_Pending_Reimbursed");
			HSSFRow row = sheet.createRow(0);

			row.createCell(0).setCellValue(CONSTANT1);
			row.createCell(1).setCellValue(CONSTANT2);
			row.createCell(2).setCellValue(CONSTANT3);
			row.createCell(3).setCellValue(CONSTANT4);
			row.createCell(4).setCellValue(CONSTANT5);
			row.createCell(5).setCellValue(CONSTANT6);
			row.createCell(6).setCellValue(CONSTANT7);
			row.createCell(7).setCellValue(CONSTANT8);
			row.createCell(8).setCellValue(CONSTANT9);
			row.createCell(9).setCellValue(CONSTANT10);
			row.createCell(10).setCellValue(CONSTANT11);
			row.createCell(11).setCellValue(CONSTANT12);
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
					dataRow.createCell(2).setCellValue(employee.getOfficialEmployeeId());
					dataRow.createCell(3).setCellValue(employee.getFirstName() + " " + employee.getLastName());
					dataRow.createCell(4).setCellValue(report.getReportId());
					dataRow.createCell(5).setCellValue(report.getReportTitle());
					dataRow.createCell(6).setCellValue(report.getDateSubmitted().toString());
					LocalDate submittedDate = report.getDateSubmitted();
					String monthName = submittedDate.getMonth().toString();
					dataRow.createCell(7).setCellValue(monthName);

					LocalDate managerActionDate = report.getManagerActionDate();
					if (managerActionDate != null) {
						dataRow.createCell(8).setCellValue(managerActionDate.toString());
					}
					dataRow.createCell(9).setCellValue(employee.getManagerEmail());
					dataRow.createCell(10).setCellValue(report.getTotalAmount());
					dataRow.createCell(11).setCellValue(String.valueOf(report.getFinanceApprovalStatus()));

					dataRowIndex++;
					sl++;
				}
			}
			workbook.write(excelStream);
			workbook.close();

		}

		if (status == StatusExcel.PENDING) {
			List<Reports> reportlist = reportRepository.findByDateSubmittedBetween(startDate, endDate);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Billfold_All_reports_Pending");
			HSSFRow row = sheet.createRow(0);

			row.createCell(0).setCellValue(CONSTANT1);
			row.createCell(1).setCellValue(CONSTANT2);
			row.createCell(2).setCellValue(CONSTANT3);
			row.createCell(3).setCellValue(CONSTANT4);
			row.createCell(4).setCellValue(CONSTANT5);
			row.createCell(5).setCellValue(CONSTANT6);
			row.createCell(6).setCellValue(CONSTANT7);
			row.createCell(7).setCellValue(CONSTANT8);
			row.createCell(8).setCellValue(CONSTANT9);
			row.createCell(9).setCellValue(CONSTANT10);
			row.createCell(10).setCellValue(CONSTANT11);
			row.createCell(11).setCellValue(CONSTANT12);
			int dataRowIndex = 1;
			int sl = 1;

			for (Reports report : reportlist) {
				if (report.getFinanceApprovalStatus() == FinanceApprovalStatus.PENDING) {
					HSSFRow dataRow = sheet.createRow(dataRowIndex);
					dataRow.createCell(0).setCellValue(sl);
					dataRow.createCell(1).setCellValue(report.getEmployeeMail());
					Long id = report.getReportId();
					List<Expense> expenseList = expenseService.getExpenseByReportId(id);

					if (!expenseList.isEmpty()) {
						Expense expense = expenseList.get(0);
						Employee employee = expense.getEmployee();
						dataRow.createCell(2).setCellValue(employee.getOfficialEmployeeId());
						dataRow.createCell(3).setCellValue(employee.getFirstName() + " " + employee.getLastName());
						dataRow.createCell(4).setCellValue(report.getReportId());
						dataRow.createCell(5).setCellValue(report.getReportTitle());
						dataRow.createCell(6).setCellValue(report.getDateSubmitted().toString());
						LocalDate submittedDate = report.getDateSubmitted();
						String monthName = submittedDate.getMonth().toString();
						dataRow.createCell(7).setCellValue(monthName);

						LocalDate managerActionDate = report.getManagerActionDate();
						if (managerActionDate != null) {
							dataRow.createCell(8).setCellValue(managerActionDate.toString());
						}
						dataRow.createCell(9).setCellValue(employee.getManagerEmail());
						dataRow.createCell(10).setCellValue(report.getTotalAmount());
						dataRow.createCell(11).setCellValue(report.getFinanceApprovalStatus().ordinal());

						dataRowIndex++;
						sl++;
					}
				}
			}
			workbook.write(excelStream);
			workbook.close();
		}

		if (status == StatusExcel.REIMBURSED) {
			List<Reports> reportlist = reportRepository.findByDateSubmittedBetween(startDate, endDate);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Billfold_All_reports_Reimbursed");
			HSSFRow row = sheet.createRow(0);

			row.createCell(0).setCellValue(CONSTANT1);
			row.createCell(1).setCellValue(CONSTANT2);
			row.createCell(2).setCellValue(CONSTANT3);
			row.createCell(3).setCellValue(CONSTANT4);
			row.createCell(4).setCellValue(CONSTANT5);
			row.createCell(5).setCellValue(CONSTANT6);
			row.createCell(6).setCellValue(CONSTANT7);
			row.createCell(7).setCellValue(CONSTANT8);
			row.createCell(8).setCellValue(CONSTANT9);
			row.createCell(9).setCellValue(CONSTANT10);
			row.createCell(10).setCellValue(CONSTANT11);
			row.createCell(11).setCellValue(CONSTANT12);

			int dataRowIndex = 1;
			int sl = 1;
			for (Reports report : reportlist) {
				if (report.getFinanceApprovalStatus() == FinanceApprovalStatus.REIMBURSED) {
					HSSFRow dataRow = sheet.createRow(dataRowIndex);
					dataRow.createCell(0).setCellValue(sl);
					dataRow.createCell(1).setCellValue(report.getEmployeeMail());
					Long id = report.getReportId();
					List<Expense> expenseList = expenseService.getExpenseByReportId(id);

					if (!expenseList.isEmpty()) {
						Expense expense = expenseList.get(0);
						Employee employee = expense.getEmployee();
						dataRow.createCell(2).setCellValue(employee.getOfficialEmployeeId());
						dataRow.createCell(3).setCellValue(employee.getFirstName() + " " + employee.getLastName());
						dataRow.createCell(4).setCellValue(report.getReportId());
						dataRow.createCell(5).setCellValue(report.getReportTitle());
						dataRow.createCell(6).setCellValue(report.getDateSubmitted().toString());
						LocalDate submittedDate = report.getDateSubmitted();
						String monthName = submittedDate.getMonth().toString();
						dataRow.createCell(7).setCellValue(monthName);

						LocalDate managerActionDate = report.getManagerActionDate();
						if (managerActionDate != null) {
							dataRow.createCell(8).setCellValue(managerActionDate.toString());
						}
						dataRow.createCell(9).setCellValue(employee.getManagerEmail());
						dataRow.createCell(10).setCellValue(report.getTotalAmount());
						dataRow.createCell(11).setCellValue("Reimbursed");

						dataRowIndex++;
						sl++;
					}
				}
			}
			workbook.write(excelStream);
			workbook.close();
		}
	}

	@Override
	public boolean sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachmentContent,
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

	@Override
	public String reimburseAndGenerateExcel(HttpServletResponse response) throws IOException {
		List<Reports> reportlist = reportRepository.findByfinanceApprovalStatus(FinanceApprovalStatus.APPROVED);

		if (reportlist.isEmpty()) {
			return "No data available for the selected period.So, Email can't be sent!";
		}

		ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
		generateExcelAndReimburse(excelStream);
		byte[] excelBytes = excelStream.toByteArray();

		Employee financeAdmin = employeeRepository.findByRole("FINANCE_ADMIN");
		if (financeAdmin == null) {
			throw new IllegalStateException("Finance admin cannot found. So, Email can't be send");
		}

		boolean emailSent = sendEmailWithAttachment(financeAdmin.getEmployeeEmail(), "BillFold:Excel Report",
				"Please find the attached Excel report.", excelBytes, "report.xls");
		if (emailSent) {
			return "Email sent successfully!";
		} else {
			return "Email not sent";
		}
	}

	public void generateExcelAndReimburse(ByteArrayOutputStream excelStream) throws IOException {

		List<Reports> reportlist = reportRepository.findByfinanceApprovalStatus(FinanceApprovalStatus.APPROVED);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Billfold_All_reports_Pending_Reimbursed");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue(CONSTANT1);
		row.createCell(1).setCellValue(CONSTANT2);
		row.createCell(2).setCellValue(CONSTANT3);
		row.createCell(3).setCellValue(CONSTANT4);
		row.createCell(4).setCellValue(CONSTANT5);
		row.createCell(5).setCellValue(CONSTANT6);
		row.createCell(6).setCellValue(CONSTANT7);
		row.createCell(7).setCellValue(CONSTANT8);
		row.createCell(8).setCellValue(CONSTANT9);
		row.createCell(9).setCellValue(CONSTANT10);
		row.createCell(10).setCellValue(CONSTANT11);

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
				dataRow.createCell(2).setCellValue(employee.getOfficialEmployeeId());
				dataRow.createCell(3).setCellValue(employee.getFirstName() + " " + employee.getLastName());
				dataRow.createCell(4).setCellValue(report.getReportId());
				dataRow.createCell(5).setCellValue(report.getReportTitle());
				dataRow.createCell(6).setCellValue(report.getDateSubmitted().toString());
				LocalDate submittedDate = report.getDateSubmitted();
				String monthName = submittedDate.getMonth().toString();
				dataRow.createCell(7).setCellValue(monthName);

				LocalDate managerActionDate = report.getManagerActionDate();
				if (managerActionDate != null) {
					dataRow.createCell(8).setCellValue(managerActionDate.toString());
				}
				dataRow.createCell(9).setCellValue(employee.getManagerEmail());
				dataRow.createCell(10).setCellValue(report.getTotalApprovedAmount());

				dataRowIndex++;
				sl++;

			}
			report.setFinanceApprovalStatus(FinanceApprovalStatus.REIMBURSED);
			reportRepository.save(report);
		}
		workbook.write(excelStream);
		workbook.close();

	}

}
