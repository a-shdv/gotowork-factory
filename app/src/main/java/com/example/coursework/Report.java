package com.example.coursework;

import android.os.Environment;

import com.example.coursework.database.models.MachineModel;
import com.example.coursework.database.models.ShiftModel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {

    final int TABLE_WIDTH = 400;
    String[] columns = {"Machine", "Shift", "Workers"};
    final int COLUMN_COUNT = 3;

    public void generatePdf(List<MachineModel> machines) throws IOException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File file = new File(pdfPath, "report.pdf");
        PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("Report on the workers\' shifts by the machine");

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

        List<String> checked = new ArrayList<>();
        for (int i = 0; i < machines.size(); i++) {
            MachineModel machine = machines.get(i);
            if (machine != null && !checked.contains(machine.getMachine_type())) {
                table.addCell(machine.getId() + ". " + machine.getMachine_type());
                table.addCell(machine.getShiftId() + ". " + machine.getShiftName());
                table.addCell(machine.getMachineWorkers().toString());
                checked.add(machine.getMachine_type());
            }
        }

        document.add(table);
        document.close();
    }

}
