package com.nineleaps.expensemanagementproject.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;

public interface IPdfGeneratorService {




	PdfPCell getCenterAlignedCell(String content, Font font);

	PdfPCell getCenterAlignedCells(String content, Font font);


	void export(Long reportId, List<Long> expenses, HttpServletResponse response) throws IOException;
}
