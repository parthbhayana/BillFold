package com.nineleaps.expense_management_project.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

public interface IPdfGeneratorService {


	PdfPCell getCenterAlignedCell(String content, Font font);

	PdfPCell getCenterAlignedCells(String content, Font font);


    byte[] export(Long reportId, List<Long> expenseIds, HttpServletResponse response, String role) throws IOException;


}