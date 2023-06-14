package com.nineleaps.expensemanagementproject.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.nineleaps.expensemanagementproject.service.IPdfGeneratorService;


@Controller
public class PdfController {
	@Autowired
	IPdfGeneratorService pdfGeneratorService;

	@GetMapping("/openpdf/export/{report_id}")
	public void createPDF(@PathVariable("report-id") Long reportId, HttpServletResponse response) throws IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		pdfGeneratorService.export(reportId, response);
	}
}