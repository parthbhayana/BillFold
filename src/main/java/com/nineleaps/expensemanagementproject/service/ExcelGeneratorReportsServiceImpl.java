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
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service

public class ExcelGeneratorReportsServiceImpl implements IExcelGeneratorReportsService {

    @Autowired
    private ReportsRepository reportRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IExpenseService expenseService;

    @Autowired
    private EmployeeRepository employeeRepository;

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

        Employee financeadmin = employeeRepository.findByRole("FINANCE_ADMIN");
        if (financeadmin == null) {
            throw new IllegalStateException("Finance admin cannot found. So, Email can't be send");
        }

        boolean emailsent = sendEmailWithAttachment(financeadmin.getEmployeeEmail(), "BillFold:Excel Report",
                "Please find the attached Excel report.", excelBytes, "report.xls");
        if (emailsent) {
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

            row.createCell(0).setCellValue("Sl.no.");
            row.createCell(1).setCellValue("Employee Official Id");
            row.createCell(2).setCellValue("Employee Email");
            row.createCell(3).setCellValue("Employee Name");
            row.createCell(4).setCellValue("Report Id");
            row.createCell(5).setCellValue("Report Name");
            row.createCell(6).setCellValue("submitted on");
            row.createCell(7).setCellValue("Month");
            row.createCell(8).setCellValue("Appproved  on");
            row.createCell(9).setCellValue("Approved by");
            row.createCell(10).setCellValue("Total Amount(INR)");
            row.createCell(11).setCellValue("Status");
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
                    dataRow.createCell(10).setCellValue(report.getTotalAmountINR());
                    dataRow.createCell(11).setCellValue(String.valueOf(report.getFinanceapprovalstatus()));

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

            row.createCell(0).setCellValue("Sl.no.");
            row.createCell(1).setCellValue("Employee Official Id");
            row.createCell(2).setCellValue("Employee Email");
            row.createCell(3).setCellValue("Employee Name");
            row.createCell(4).setCellValue("Report Id");
            row.createCell(5).setCellValue("Report Name");
            row.createCell(6).setCellValue("submitted on");
            row.createCell(7).setCellValue("Month");
            row.createCell(8).setCellValue("Appproved  on");
            row.createCell(9).setCellValue("Approved by");
            row.createCell(10).setCellValue("Total Amount(INR)");
            row.createCell(11).setCellValue("Status");
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
                        dataRow.createCell(10).setCellValue(report.getTotalAmountINR());
                        dataRow.createCell(11).setCellValue("Pending");

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

            row.createCell(0).setCellValue("Sl.no.");
            row.createCell(1).setCellValue("Employee Official Id");
            row.createCell(2).setCellValue("Employee Email");
            row.createCell(3).setCellValue("Employee Name");
            row.createCell(4).setCellValue("Report Id");
            row.createCell(5).setCellValue("Report Name");
            row.createCell(6).setCellValue("submitted on");
            row.createCell(7).setCellValue("Month");
            row.createCell(8).setCellValue("Appproved  on");
            row.createCell(9).setCellValue("Approved by");
            row.createCell(10).setCellValue("Total Amount(INR)");
            row.createCell(11).setCellValue("Status");

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
                        dataRow.createCell(10).setCellValue(report.getTotalAmountINR());
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
}
