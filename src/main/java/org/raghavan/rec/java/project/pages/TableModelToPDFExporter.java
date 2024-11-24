package org.raghavan.rec.java.project.pages;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import javax.swing.table.DefaultTableModel;
import java.io.File;

public class TableModelToPDFExporter {


    public void exportToPDF(DefaultTableModel tableModel, String filePath) {
        try {
            // Initialize PDF writer and document with landscape page size
            PdfWriter writer = new PdfWriter(filePath);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);

            // Set page size to landscape (A4 landscape)
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

            // Create a Document instance
            Document document = new Document(pdfDoc);

            // Add a title to the PDF
            document.add(new Paragraph("Timetable")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            // Create a table with the same number of columns as the table model
            int columnCount = tableModel.getColumnCount();
            Table table = new Table(UnitValue.createPercentArray(columnCount)).useAllAvailableWidth();

            // Add table headers
            for (int col = 0; col < columnCount; col++) {
                table.addHeaderCell(new Cell()
                        .add(new Paragraph(tableModel.getColumnName(col)))
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setTextAlignment(TextAlignment.CENTER));
            }

            // Add table rows
            int rowCount = tableModel.getRowCount();
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Object value = tableModel.getValueAt(row, col);
                    table.addCell(new Cell()
                            .add(new Paragraph(value != null ? value.toString() : ""))
                            .setTextAlignment(TextAlignment.CENTER));
                }
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            System.out.println("PDF created successfully: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
