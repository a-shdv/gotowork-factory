package com.example.coursework;

import android.os.Environment;

import com.example.coursework.database.models.MachineModel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {

    final int TABLE_WIDTH = 400;
    String[] columns = {"Supplier name", "Number of supplies"};
    final int COLUMN_COUNT = 2;

    public void generatePdf(List<MachineModel> receipts, Date dateFrom, Date dateTo) throws IOException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File file = new File(pdfPath, "report.pdf");
        PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("Report on the number of supplies for the period from to " + dateFrom.getDate() + " / " + dateFrom.getMonth() + " / " + (dateFrom.getYear() + 1900) + " to " + dateTo.getDate() + " / " + dateTo.getMonth() + " / " + (dateTo.getYear() + 1900));

        document.add(paragraph);

        float columnWidth[] = new float[COLUMN_COUNT];
        float size = TABLE_WIDTH / COLUMN_COUNT;

        for (int i = 0; i < columnWidth.length; i++) {
            columnWidth[i] = size;
        }
        Table table = new Table(columnWidth);
        for (int i = 0; i < columnWidth.length; i++) {
            table.addCell(columns[i]);
        }

        Map<Integer, Integer> supplierReceipts = new HashMap<>();

        for (MachineModel receipt : receipts) {
            if (supplierReceipts.containsKey(receipt.getSupplierId())) {
                supplierReceipts.put(receipt.getSupplierId(), supplierReceipts.get(receipt.getSupplierId()) + 1);
            } else {
                supplierReceipts.put(receipt.getSupplierId(), 1);
            }
        }


/*        for (int i = 0; i < receipts.size(); i++) {
            if (!checked.contains(receipts.get(i).getSupplierName())) {
                checked.add(receipts.get(i).getSupplierName());
            }
        }*/

        List<String> checked = new ArrayList<>();
        for (int i = 0; i < receipts.size(); i++) {
            if (receipts.get(i) != null && !checked.contains(receipts.get(i).getSupplierName())) {
                table.addCell(receipts.get(i).getSupplierId() + ". " + receipts.get(i).getSupplierName());
                table.addCell(String.valueOf(supplierReceipts.get(receipts.get(i).getSupplierId())));
                checked.add(receipts.get(i).getSupplierName());
            }
        }

/*        for (ReceiptModel receipt : receipts) {
            if (receipt.getSupplierId() != 0) {
                table.addCell(receipt.getSupplierId() + ". " + receipt.getSupplierName());
                table.addCell(String.valueOf(supplierReceipts.get(receipt.getSupplierId())));
            }
        }*/

        document.add(table);
        document.close();
    }

}
