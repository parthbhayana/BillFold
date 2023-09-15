package com.nineleaps.expensemanagementproject.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.entity.StatusExcel;

public interface IExcelGeneratorReportsService {

	String generateExcelAndSendEmail(HttpServletResponse response, LocalDate startDate, LocalDate endDate,
									 StatusExcel status) throws Exception ;

	void generateExcel(ByteArrayOutputStream excelStream, LocalDate startDate, LocalDate endDate, StatusExcel status)
			throws Exception;

	boolean sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachmentContent,
									String attachmentFilename);

	String reimburseAndGenerateExcel(HttpServletResponse response) throws IOException;
}