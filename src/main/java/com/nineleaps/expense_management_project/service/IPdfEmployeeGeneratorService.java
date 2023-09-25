package com.nineleaps.expense_management_project.service;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IPdfEmployeeGeneratorService {
    PdfPCell getCenterAlignedCell(String content, Font font);

    PdfPCell getCenterAlignedCells(String content, Font font);

    byte[] export(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException;
}