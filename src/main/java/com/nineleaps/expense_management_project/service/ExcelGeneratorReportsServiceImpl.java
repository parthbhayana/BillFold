package com.nineleaps.expense_management_project.service;

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
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.entity.StatusExcel;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;

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

    private static final String[] COLUMN_HEADERS = {
            "Sl.no.",
            "Employee Email",
            "Employee Official Id",
            "Employee Name",
            "Report Id",
            "Report Name",
            "submitted on",
            "Month",
            "Approved on",
            "Approved by",
            "Total Amount(INR)",
            "Status"
    };

    @Override
    public String generateExcelAndSendEmail(HttpServletResponse response, LocalDate startDate, LocalDate endDate,
                                            StatusExcel status) throws Exception {
        List<Reports> reportlist = reportRepository.findByDateSubmittedBetween(startDate, endDate);

        if (reportlist.isEmpty()) {
            return "No data available for the selected period. So, Email can't be sent!";
        }

        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        generateExcel(excelStream, startDate, endDate, status, reportlist);
        byte[] excelBytes = excelStream.toByteArray();

        Employee financeAdmin = employeeRepository.findByRole("FINANCE_ADMIN");


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
                              StatusExcel status, List<Reports> reportlist) throws Exception{
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("Billfold_All_reports");
            HSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < COLUMN_HEADERS.length; i++) {
                headerRow.createCell(i).setCellValue(COLUMN_HEADERS[i]);
            }
            int dataRowIndex = 1;
            int sl = 1;
            for (Reports report : reportlist) {
                if (status == StatusExcel.ALL || (status == StatusExcel.PENDING && report.getFinanceApprovalStatus() == FinanceApprovalStatus.PENDING)
                        || (status == StatusExcel.REIMBURSED && report.getFinanceApprovalStatus() == FinanceApprovalStatus.REIMBURSED)) {
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
            }
            workbook.write(excelStream);
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
            return false;
        }
    }

    @Override
    public String reimburseAndGenerateExcel(HttpServletResponse response) throws Exception {
        List<Reports> reportlist = reportRepository.findByfinanceApprovalStatus(FinanceApprovalStatus.APPROVED);

        if (reportlist.isEmpty()) {
            return "No data available for the selected period. So, Email can't be sent!";
        }

        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        generateExcel(excelStream, LocalDate.now(), LocalDate.now(), StatusExcel.REIMBURSED, reportlist);
        byte[] excelBytes = excelStream.toByteArray();

        Employee financeAdmin = employeeRepository.findByRole("FINANCE_ADMIN");

        boolean emailSent = sendEmailWithAttachment(financeAdmin.getEmployeeEmail(), "BillFold:Excel Report",
                "Please find the attached Excel report.", excelBytes, "report.xls");
        if (emailSent) {
            return "Email sent successfully!";
        } else {
            return "Email not sent";
        }
    }
}
