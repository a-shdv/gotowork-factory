package com.example.coursework;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import com.example.coursework.activities.LoginActivity;
import com.example.coursework.database.models.MachineModel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {
    URL url;
    InputStream inputStream;
    String line = "";
    String result = "";

    public void createPdf() throws JSONException, FileNotFoundException {
        final int TABLE_WIDTH = 400;
        String[] columns = {"Worker", "Shift"};
        final int COLUMN_COUNT = 2;

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File file = new File(pdfPath, "report.pdf");
        PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("Report on the workers\' shifts");

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

        HashMap<String, String> shifts = getShifts();
        HashMap<String, String> workers = getWorkers();

        List<String> checkedShifts = new ArrayList<>();
        List<String> checkedWorkers = new ArrayList<>();
        for (Map.Entry<String, String> entryShifts : shifts.entrySet()) {
            for (Map.Entry<String, String> entryWorkers : workers.entrySet()) {
                if (!checkedShifts.contains(entryShifts.getValue())) {
                    if (!checkedWorkers.contains(entryWorkers.getValue())) {
                        table.addCell(entryWorkers.getKey() + ". " + entryWorkers.getValue());
                        table.addCell(entryShifts.getKey() + ". " + entryShifts.getValue());
                        checkedShifts.add(entryShifts.getValue());
                        checkedWorkers.add(entryWorkers.getValue());
                    }

                }
            }
        }

        document.add(table);
        document.close();
    }

    public void createPdf(List<MachineModel> machines) throws IOException, JSONException {
        final int TABLE_WIDTH = 400;
        String[] columns = {"Machine", "Shift", "Workers"};
        final int COLUMN_COUNT = 3;

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

        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
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
        } else {
            HashMap<String, String> shifts = getShifts();
            HashMap<String, String> workers = getWorkers();

            List<String> checkedShifts = new ArrayList<>();
            List<String> checkedWorkers = new ArrayList<>();
            for (Map.Entry<String, String> entryShifts : shifts.entrySet()) {
                for (Map.Entry<String, String> entryWorkers : workers.entrySet()) {
                    if (!checkedShifts.contains(entryShifts.getValue())) {
                        if (!checkedWorkers.contains(entryWorkers.getValue())) {
                            table.addCell(entryWorkers.getKey() + ". " + entryWorkers.getValue());
                            table.addCell(entryShifts.getKey() + ". " + entryShifts.getValue());
                            checkedShifts.add(entryShifts.getValue());
                            checkedWorkers.add(entryWorkers.getValue());
                        }

                    }
                }
            }
        }
        document.add(table);
        document.close();
    }

    public HashMap<String, String> getShifts() throws JSONException {
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        try {
            url = new URL("http://192.168.31.7:8000/gotowork/shift/get.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            inputStream = new BufferedInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read inputStream content into a String
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;

        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            result.put(jsonObject.getString("id"), jsonObject.getString("type"));
        }
        return result;
    }

    public HashMap<String, String> getWorkers() throws JSONException {
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        try {
            url = new URL("http://192.168.31.7:8000/gotowork/worker/get.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            inputStream = new BufferedInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read inputStream content into a String
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;

        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            result.put(jsonObject.getString("id"), jsonObject.getString("name"));
        }
        return result;
    }

}
