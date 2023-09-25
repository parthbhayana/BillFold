package com.nineleaps.expense_management_project.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.JFreeChart;





public interface IExcelGeneratorCategoryService {

	String generateExcelAndSendEmail(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws Exception;

	void generateExcel(ByteArrayOutputStream excelStream, LocalDate startDate, LocalDate endDate) throws Exception;

	int loadChartImage(JFreeChart chart, int width, int height, HSSFWorkbook workbook) throws IOException;

	HashMap<String, Double> categoryTotalAmount(LocalDate startDate, LocalDate endDate);

	boolean sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachmentContent,
									String attachmentFilename);

}