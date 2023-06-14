package com.nineleaps.expensemanagementproject.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;

public interface IPdfGeneratorService {

	public void export(Long reportId, HttpServletResponse response) throws IOException;

	byte[] generatePdf(Long reportId) throws IOException;

	PdfPCell getCenterAlignedCell(String content, Font font);

	PdfPCell getCenterAlignedCells(String content, Font font);

	byte[] fetchImageAsByteArray(String imageUrl) throws IOException;

}
