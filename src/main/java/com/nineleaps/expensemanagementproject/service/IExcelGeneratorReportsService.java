package com.nineleaps.expensemanagementproject.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.entity.StatusExcel;

public interface IExcelGeneratorReportsService {

	String generateExcelAndSendEmail(HttpServletResponse response, LocalDate startDate, LocalDate endDate,
									 StatusExcel status) throws Exception ;



	void generateExcel(ByteArrayOutputStream excelStream, LocalDate startDate, LocalDate endDate,
					   StatusExcel status, List<Reports> reportlist) throws Exception;

	boolean sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachmentContent,
									String attachmentFilename);

	String reimburseAndGenerateExcel(HttpServletResponse response) throws Exception;
}