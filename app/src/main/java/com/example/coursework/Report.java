package com.example.coursework;

import android.os.Environment;

import com.example.coursework.database.models.ShiftModel;
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
    String[] columns = {"Shift name", "Number of shifts"};
    final int COLUMN_COUNT = 2;

    public void generatePdf(List<ShiftModel> shifts, Date dateFrom, Date dateTo) throws IOException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File file = new File(pdfPath, "report.pdf");
        PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("Report on the number of shifts for the period from to " + dateFrom.getDate() + " / " + dateFrom.getMonth() + " / " + (dateFrom.getYear() + 1900) + " to " + dateTo.getDate() + " / " + dateTo.getMonth() + " / " + (dateTo.getYear() + 1900));

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

        Map<Integer, Integer> shiftMachines = new HashMap<>();

        for (ShiftModel shift : shifts) {
            if (shiftMachines.containsKey(shift.getId())) {
                shiftMachines.put(shift.getId(), shiftMachines.get(shift.getId()) + 1);
            } else {
                shiftMachines.put(shift.getId(), 1);
            }
        }

        List<String> checked = new ArrayList<>();
        for (int i = 0; i < shifts.size(); i++) {
            if (shifts.get(i) != null && !checked.contains(shifts.get(i).getType())) {
                table.addCell(shifts.get(i).getId() + ". " + shifts.get(i).getType());
                table.addCell(String.valueOf(shiftMachines.get(shifts.get(i).getId())));
                checked.add(shifts.get(i).getType());
            }
        }

        document.add(table);
        document.close();
    }

}
