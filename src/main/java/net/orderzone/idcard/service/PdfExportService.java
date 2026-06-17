package net.orderzone.idcard.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import net.orderzone.idcard.model.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfExportService {

    public byte[] exportProfileToPdf(Profile profile) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        
        // Typical ID Card size: 3.375" x 2.125" -> approx 243 x 153 points. We will use a standard portrait card size
        PageSize cardSize = new PageSize(153, 243);
        pdf.setDefaultPageSize(cardSize);
        
        Document document = new Document(pdf);
        document.setMargins(10, 10, 10, 10);
        
        String orgName = profile.getTemplate() != null && profile.getTemplate().getOrganizationName() != null ? 
                profile.getTemplate().getOrganizationName() : "OrderZone Institution";

        document.add(new Paragraph(orgName)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(10));
                
        document.add(new Paragraph(profile.getType().name() + " ID CARD")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(8));

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Name: " + profile.getFullName())
                .setFontSize(8));
        document.add(new Paragraph("Reg No: " + profile.getRegistrationNumber())
                .setFontSize(8));
        
        if (profile.getDepartment() != null) {
            document.add(new Paragraph("Dept: " + profile.getDepartment())
                    .setFontSize(8));
        }

        document.close();
        return baos.toByteArray();
    }

    public byte[] exportBatchToPdf(List<Profile> profiles) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        
        document.add(new Paragraph("Batch ID Cards").setBold().setFontSize(16));
        
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));
        
        table.addCell(new Cell().add(new Paragraph("ID").setBold()));
        table.addCell(new Cell().add(new Paragraph("Reg No").setBold()));
        table.addCell(new Cell().add(new Paragraph("Name").setBold()));
        table.addCell(new Cell().add(new Paragraph("Type").setBold()));
        
        for (Profile p : profiles) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(p.getId()))));
            table.addCell(new Cell().add(new Paragraph(p.getRegistrationNumber())));
            table.addCell(new Cell().add(new Paragraph(p.getFullName())));
            table.addCell(new Cell().add(new Paragraph(p.getType().name())));
        }
        
        document.add(table);
        document.close();
        return baos.toByteArray();
    }
}
