package com.nineleaps.expensemanagementproject.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import static com.lowagie.text.Element.*;
import static com.lowagie.text.Element.ALIGN_LEFT;
@Service
public class PdfEmployeeGeneratorServiceImpl implements IPdfEmployeeGeneratorService  {

        @Autowired
        ExpenseRepository expenseRepository;
        @Autowired
        ReportsRepository reportsRepository;
        @Autowired
        EmployeeRepository employeeRepository;
        @Autowired
        JavaMailSender mailSender;
        @Autowired
        IExpenseService expenseService;


        public byte[] generatePdf(Long reportId, List<Long> expenseIds) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            @SuppressWarnings("unused")
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            class FooterEvent extends PdfPageEventHelper {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfContentByte pdfContentByte = writer.getDirectContent();
                    float x = PageSize.A4.getWidth() - document.rightMargin();
                    float y = document.bottomMargin() - 20;
                    Font billFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 25, Font.NORMAL, Color.BLACK);
                    Font foldFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 25, Font.NORMAL, new Color(0, 110, 230));
                    Font generatedbyFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL, Color.BLACK);
                    Paragraph footerParagraph = new Paragraph();
                    footerParagraph.setAlignment(Element.ALIGN_RIGHT);
                    footerParagraph.add(new Chunk("Generated By ", generatedbyFont));
                    footerParagraph.add(new Chunk("Bill", billFont));
                    footerParagraph.add(new Chunk("Fold.", foldFont));
                    ColumnText.showTextAligned(pdfContentByte, Element.ALIGN_RIGHT, footerParagraph, x, y, 0);
                }
            }
            FooterEvent event = new FooterEvent();
            writer.setPageEvent(event);

            document.open();
            Font fontheader01 = FontFactory.getFont(FontFactory.TIMES);
            fontheader01.setSize(10);
            Paragraph headerParagraph01 = new Paragraph("Report Id: " + reportId, fontheader01);
            headerParagraph01.setAlignment(ALIGN_RIGHT);


            Font fontHeader = FontFactory.getFont(FontFactory.TIMES);
            fontHeader.setSize(22);
            Paragraph headerParagraph = new Paragraph("BillFold - Expense Report", fontHeader);
            headerParagraph.setAlignment(ALIGN_CENTER);
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            Font font = FontFactory.getFont(FontFactory.TIMES, 12);
            table.addCell(getCenterAlignedCell("Date", font));
            table.addCell(getCenterAlignedCell("Merchant", font));
            table.addCell(getCenterAlignedCell("Description", font));
            table.addCell(getCenterAlignedCell("Amount", font));
            table.addCell(getCenterAlignedCell("Status", font));

            Font fontParagraph = FontFactory.getFont(FontFactory.TIMES);
            fontParagraph.setSize(12);
            Reports report = reportsRepository.findById(reportId).get();
            List<Expense> expenses = expenseRepository.findByReports(report);
            Employee employee = null;
            if (!expenseIds.isEmpty()) {
                Long firstExpense = expenseIds.get(0);
                Expense firstExpenses = expenseService.getExpenseById(firstExpense);
                employee = employeeRepository.findById(firstExpenses.getEmployee().getEmployeeId()).orElse(null);
            }
            float total = 0;
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MMM d, yyyy");
            for (Long expenseId : expenseIds) {
                Expense expenseList = expenseService.getExpenseById(expenseId);
                LocalDate dateCreated = expenseList.getDate();
                table.addCell(getCenterAlignedCells(dateCreated.format(formatter1), font));
                table.addCell(getCenterAlignedCells(expenseList.getMerchantName(), font));
                table.addCell(getCenterAlignedCells(expenseList.getDescription(), font));
                if(expenseList.getAmountApprovedINR()!=null)
                {
                    table.addCell((getCenterAlignedCells(expenseList.getAmountApprovedINR().toString(), font)));
                }
                else {
                    table.addCell(getCenterAlignedCells(expenseList.getAmount().toString(), font));
                }
                table.addCell(getCenterAlignedCells(String.valueOf(expenseList.getManagerApprovalStatusExpense()), font));

                total += (expenseList.getAmountApprovedINR() != null) ? expenseList.getAmountApprovedINR() : expenseList.getAmount();
            }






            Font fontParagraph1 = FontFactory.getFont(FontFactory.TIMES_BOLD);
            fontParagraph1.setSize(14);
            Chunk currencyChunk = new Chunk("INR", fontParagraph1);
            Chunk totalChunk = new Chunk(String.valueOf(total), fontParagraph1);
            Paragraph pdfParagraph01 = new Paragraph();
            pdfParagraph01.setAlignment(ALIGN_RIGHT);
            pdfParagraph01.add("Total Amount: ");
            pdfParagraph01.add(currencyChunk);
            pdfParagraph01.add(" ");
            pdfParagraph01.add(totalChunk);

            Font fontParagraph11 = FontFactory.getFont(FontFactory.TIMES);
            fontParagraph11.setSize(14);
            @SuppressWarnings("null")
            Paragraph pdfParagraph = new Paragraph("Employee Name : " + employee.getFirstName() + " " + employee.getLastName(), fontParagraph);
            pdfParagraph.setAlignment(ALIGN_LEFT);
            Font fontParagraph12 = FontFactory.getFont(FontFactory.TIMES);
            fontParagraph12.setSize(12);
            Paragraph pdfParagraph02 = new Paragraph("Employee Email : " + employee.getEmployeeEmail(), fontParagraph);
            pdfParagraph02.setAlignment(ALIGN_LEFT);
            Paragraph pdfParagraph002 = new Paragraph("Employee Official ID : " + employee.getOfficialEmployeeId());
            pdfParagraph002.setAlignment(ALIGN_LEFT);
            Paragraph emptyParagraph = new Paragraph(" ");
            Font fontParagraph13 = FontFactory.getFont(FontFactory.TIMES);
            fontParagraph13.setSize(20);
            Paragraph pdfParagraph03 = new Paragraph("Report Title : "+report.getReportTitle(), fontParagraph13);
            pdfParagraph03.setAlignment(ALIGN_LEFT);
            Paragraph pdfParagraph011 = new Paragraph();
            pdfParagraph011.setAlignment(ALIGN_RIGHT);
            pdfParagraph011.add("Total Amount: ");
            pdfParagraph011.add(currencyChunk);
            pdfParagraph011.add(" ");
            pdfParagraph011.add(totalChunk);
            Font fontNote = FontFactory.getFont(FontFactory.TIMES_ITALIC, 10);
            Paragraph noteParagraph = new Paragraph("Notes:\n", fontNote);
            noteParagraph.setAlignment(ALIGN_LEFT);
            noteParagraph.setIndentationLeft(20);
            noteParagraph.add("The information on this receipt was manually entered. Please verify for authenticity.\n");
            Paragraph lineSeparator = new Paragraph(
                    "----------------------------------------------------------------------------------------------------------------------------------");
            lineSeparator.setAlignment(Element.ALIGN_CENTER);
            lineSeparator.setSpacingAfter(10);







//            Paragraph historyTitle = new Paragraph("Report History and comments:",
//                    FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
//            historyTitle.setAlignment(Element.ALIGN_LEFT);
//            historyTitle.setSpacingAfter(10);
//            Paragraph historyContent = new Paragraph();
//            historyContent.setAlignment(Element.ALIGN_LEFT);
//            historyContent.setFont(FontFactory.getFont(FontFactory.TIMES, 10));
//
//            LocalDate dateTimeCreated = report.getDateCreated();
//            LocalDate dateSubmitted = report.getDateSubmitted();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
//
//            String createdMessage = "Report Created on:\n" + dateTimeCreated.format(formatter);
//            historyContent.add(createdMessage);
//            historyContent.add(Chunk.NEWLINE);
//
//            String submissionMessage = "Report submitted to you (cc: you) on:\n" + dateSubmitted.format(formatter);
//            historyContent.add(submissionMessage);




            Font fontParagraph14 = FontFactory.getFont(FontFactory.TIMES_ITALIC);
            fontParagraph14.setSize(14);
            Paragraph pdfParagraph04 = new Paragraph("Report Description : "+report.getReportDescription(), fontParagraph14);
            pdfParagraph04.setAlignment(ALIGN_LEFT);




            document.add(headerParagraph01);
            document.add(headerParagraph);
            document.add(lineSeparator);
            document.add(emptyParagraph);
            document.add(emptyParagraph);
            document.add(pdfParagraph03);
            document.add(pdfParagraph04);
            document.add(emptyParagraph);
            document.add(pdfParagraph002);
            document.add(pdfParagraph);
            document.add(pdfParagraph02);
            document.add(emptyParagraph);
            document.add(emptyParagraph);
            document.add(emptyParagraph);
            document.add(table);
            document.add(pdfParagraph011);
            document.add(emptyParagraph);
            document.add(emptyParagraph);
            document.add(emptyParagraph);
            document.add(emptyParagraph);
            document.add(noteParagraph);
            document.add(lineSeparator);
//            document.add(historyTitle);
//            document.add(historyContent);


            int supportingPdfStartPage = writer.getPageNumber();

            document.newPage();
//        for (Long expense : expenseIds) {
//            Expense expenseList = expenseService.getExpenseById(expense);
//            if (expenseList.getSupportingDocuments() == null)
//                continue;
//            if (expenseList.getSupportingDocuments() != null) {
//                byte[] imageData = expenseList.getSupportingDocuments();
//                InputStream in = new ByteArrayInputStream(imageData);
//                BufferedImage bufferedImage = ImageIO.read(in);
//                Image image = Image.getInstance(bufferedImage, null);
//                image.scaleAbsolute(600f, 600f);
//                image.setAlignment(Image.MIDDLE);
//                document.add(image);
//                document.newPage();
//            }
//        }


            for (Long expenseId : expenseIds) {
                Expense expense = expenseService.getExpenseById(expenseId);
                byte[] supportingDocument = expense.getSupportingDocuments();

                if (supportingDocument != null) {
                    if (isPdfFormat(supportingDocument)) {
                        PdfReader pdfReader = new PdfReader(supportingDocument);
                        int numPages = pdfReader.getNumberOfPages();

                        for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                            PdfImportedPage page = writer.getImportedPage(pdfReader, pageNum);
                            document.newPage();
                            PdfContentByte contentByte = writer.getDirectContent();
                            contentByte.addTemplate(page, 0, 0);
                        }

                        pdfReader.close();
                    } else if (isImageFormat(supportingDocument)) {
                        document.newPage();
                        InputStream imageInputStream = new ByteArrayInputStream(supportingDocument);
                        BufferedImage bufferedImage = ImageIO.read(imageInputStream);
                        com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(bufferedImage, null);
                        image.scaleAbsolute(600f, 600f);
                        image.setAlignment(Image.MIDDLE);
                        document.add(image);
                        document.newPage();
                    }
                }
            }

            writer.setPageCount(supportingPdfStartPage - 1);
            document.close();
            writer.close();
            return baos.toByteArray();
        }

        private boolean isImageFormat(byte[] data) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
                return bufferedImage != null;
            } catch (IOException e) {
                return false;
            }
        }

        private boolean isPdfFormat(byte[] data) {
            try {
                PdfReader pdfReader = new PdfReader(data);
                pdfReader.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }


        @Override
        public PdfPCell getCenterAlignedCell(String content, Font font) {
            PdfPCell cell = new PdfPCell(new Phrase(content, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(new Color(240, 240, 240));
            cell.setBorderWidth(0.01f);
            cell.setBorderWidthLeft(0);
            cell.setBorderWidthRight(0);
            return cell;
        }

        @Override
        public PdfPCell getCenterAlignedCells(String content, Font font) {
            PdfPCell cell = new PdfPCell(new Phrase(content, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0.01f);
            cell.setBorderWidthLeft(0);
            cell.setBorderWidthRight(0);
            return cell;
        }

        @Override
        public byte[] export(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException {
            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerKey, headerValue);
            byte[] pdfBytes = generatePdf(reportId, expenseIds);
            response.setContentLength(pdfBytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(pdfBytes);
            outputStream.flush();
            outputStream.close();
            return pdfBytes;
        }
    }










