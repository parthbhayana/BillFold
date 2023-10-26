package com.nineleaps.expense_management_project.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;

@Service
public class ExcelGeneratorCategoryServiceImpl implements IExcelGeneratorCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JavaMailSender mailSender;

    private static final int CHART_IMAGE_WIDTH = 640;
    private static final int CHART_IMAGE_HEIGHT = 480;

    @Override
    public String generateExcelAndSendEmail(HttpServletResponse response, LocalDate startDate,
                                            LocalDate endDate) throws Exception {

        @SuppressWarnings("unused") List<Category> categories = categoryRepository.findAll();
        HashMap<String, Double> categoryAmountMap = categoryTotalAmount(startDate, endDate);

        if (categoryAmountMap.isEmpty()) {
            return "No data available for the selected period.So, Email can't be sent!";
        }

        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        generateExcel(excelStream, startDate, endDate);
        byte[] excelBytes = excelStream.toByteArray();

        Employee financeadmin = employeeRepository.findByRole("FINANCE_ADMIN");

        boolean emailsent = sendEmailWithAttachment(financeadmin.getEmployeeEmail(), "BillFold:Excel Report",
                "Please find the " + "attached Excel report.", excelBytes, "report.xls");
        if (emailsent) {
            return "Email sent successfully!";
        } else {
            return "Email not sent";
        }
    }


    @Override
    public void generateExcel(ByteArrayOutputStream excelStream, LocalDate startDate,
                              LocalDate endDate) throws Exception {

        List<Category> categories = categoryRepository.findAll();
        HashMap<String, Double> categoryAmountMap = categoryTotalAmount(startDate, endDate);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Category Wise Expense Analytics");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("Sl.no.");
        row.createCell(1).setCellValue("Category Name");
        row.createCell(2).setCellValue("Total Amount");
        row.createCell(3).setCellValue("Percentage");
        int dataRowIndex = 1;
        int sl = 1;
        float totalAmountSum = 0.0f;

        for (Category category : categories) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(sl);
            dataRow.createCell(1).setCellValue(category.getCategoryDescription());

            String categoryName = category.getCategoryDescription();
            if (categoryAmountMap.containsKey(categoryName)) {
                Double totalAmount = categoryAmountMap.get(categoryName);
                dataRow.createCell(2).setCellValue(totalAmount);
                totalAmountSum += totalAmount;
            } else {
                dataRow.createCell(2).setCellValue(0.0f);
            }
            sl++;
            dataRowIndex++;
        }




        @SuppressWarnings("rawtypes") DefaultPieDataset dataset = new DefaultPieDataset();


        JFreeChart chart = ChartFactory.createPieChart("Category Wise Expense Analytics", dataset, true, true, false);
        @SuppressWarnings("rawtypes") PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(null);

        HSSFPatriarch drawing = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 1, 20, 15);
        @SuppressWarnings("unused") HSSFPicture picture =
                drawing.createPicture(anchor, loadChartImage(chart, CHART_IMAGE_WIDTH, CHART_IMAGE_HEIGHT, workbook));

        workbook.write(excelStream);
        workbook.close();
    }

    @Override
    public int loadChartImage(JFreeChart chart, int width, int height, HSSFWorkbook workbook) throws IOException {
        try (ByteArrayOutputStream chartOut = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(chartOut, chart, width, height);
            return workbook.addPicture(chartOut.toByteArray(), Workbook.PICTURE_TYPE_PNG);
        }
    }

    @Override
    public HashMap<String, Double> categoryTotalAmount(LocalDate startDate, LocalDate endDate) {
        List<Expense> expenseList = expenseRepository.findByDateBetween(startDate, endDate);
        HashMap<String, Double> categoryAmountMap = new HashMap<>();

        for (Expense expense : expenseList) {
            Category category = expense.getCategory();
            String categoryName = category.getCategoryDescription();
            Double amt = expense.getAmountApproved();
            if (categoryAmountMap.containsKey(categoryName)) {
                Double previousAmt = categoryAmountMap.get(categoryName);
                categoryAmountMap.put(categoryName, previousAmt + amt);
            } else {
                categoryAmountMap.put(categoryName, amt);
            }
        }
        return categoryAmountMap;

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
}