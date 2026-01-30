package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfInvoiceService {

    public byte[] generateInvoice(Order order) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);

            doc.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            doc.add(new Paragraph("Bakery Shop Invoice", titleFont));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Order ID: " + order.getId(), textFont));
            doc.add(new Paragraph("Customer: " + order.getName(), textFont));
            doc.add(new Paragraph("Email: " + order.getEmail(), textFont));
            doc.add(new Paragraph("Mobile: " + order.getMobile(), textFont));
            doc.add(new Paragraph("Address: " + order.getAddress(), textFont));
            doc.add(new Paragraph(" "));

            double grandTotal = 0;

            for (OrderItem item : order.getItems()) {
                double sub = item.getPrice() * item.getQty();
                double gst = (sub * item.getGst()) / 100;
                double total = sub + gst;

                grandTotal += total;

                doc.add(new Paragraph(
                        item.getName() + " x " + item.getQty() +
                        " = ₹" + String.format("%.2f", total),
                        textFont
                ));
            }

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(
                    "Grand Total: ₹" + String.format("%.2f", grandTotal),
                    titleFont
            ));

            doc.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }
}
