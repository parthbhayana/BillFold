package com.nineleaps.expensemanagementproject.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;

@Service
public class PdfGeneratorServiceImpl {
	@Autowired
	ExpenseRepository expenseRepository;
	@Autowired
	ReportsRepository reportsRepository;

	public void export(Long reportId, HttpServletResponse response) throws IOException {

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD);
		fontHeader.setSize(22);
		Paragraph headerParagraph = new Paragraph("EXPENSE REPORT", fontHeader);
		headerParagraph.setAlignment(Paragraph.ALIGN_CENTER);
		Table table = new Table(4);
		table.setWidth(100);
		table.addCell("Date");
		table.addCell("Merchant");
		table.addCell("Description");
		table.addCell("Amount");
//        table.addCell(new Cell().add(new Paragraph("Date").setFont(fontHeader).setFontSize(14)).setBackgroundColor(Color.RED));
//        table.addCell(new Cell().add(new Paragraph("Merchant").setFont(fontHeader).setFontSize(14)));
//        table.addCell(new Cell().add(new Paragraph("Description").setFont(fontHeader).setFontSize(14)));
//        table.addCell(new Cell().add(new Paragraph("Amount").setFont(fontHeader).setFontSize(14)));
		Font fontParagraph = FontFactory.getFont(FontFactory.TIMES);
		fontParagraph.setSize(14);
		Reports report = reportsRepository.findById(reportId).get();
		List<Expense> expenses = expenseRepository.findByReports(report);
		float total = 0;
		for (Expense expense : expenses) {
			table.addCell(expense.getDate().toString());
			table.addCell(expense.getMerchantName());
			table.addCell(expense.getDescription());
			table.addCell(expense.getAmount().toString());
			total += expense.getAmount();
		}
		// String imageUrl = "file:///home/nineleaps/Desktop/tiger.jpeg";
//		String imageUrl = "expense.getSupportingDocument()";
//		Image image = Image.getInstance(new URL(imageUrl));
//		image.scaleAbsolute(200f, 200f); // Set the size of the image
//		image.setAlignment(Image.MIDDLE);
		Font fontParagraph1 = FontFactory.getFont(FontFactory.TIMES);
		fontParagraph1.setSize(14);
		Paragraph pdfParagraph01 = new Paragraph("Total Expense Amount:Rs." + total, fontParagraph);
		pdfParagraph01.setAlignment(Paragraph.ALIGN_LEFT);
		Font fontParagraph11 = FontFactory.getFont(FontFactory.TIMES);
		fontParagraph11.setSize(14);
		Paragraph pdfParagraph = new Paragraph("Expense Report for Employee Id:", fontParagraph);
		pdfParagraph.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(headerParagraph);
		document.add(pdfParagraph);
		document.add(table);
		document.add(pdfParagraph01);
//		document.add(image);
		document.close();
	}
}