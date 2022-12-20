package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.Report;
import com.example.coursework.database.logics.MachineLogic;
import com.example.coursework.database.logics.ShiftLogic;
import com.example.coursework.database.models.ShiftModel;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    TextView text_view_report_info;

    Button button_date_from;
    Button button_date_to;
    Button button_report;

    Calendar dateFrom = new GregorianCalendar();
    Calendar dateTo = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        button_date_from = findViewById(R.id.button_date_from);
        button_date_to = findViewById(R.id.button_date_to);
        button_report = findViewById(R.id.button_report);

        text_view_report_info = findViewById(R.id.text_view_report_info);

        dateFrom.set(2022, 1, 1);
        dateTo.set(2022, 1, 1);

        String text = "Отчет по количеству поставок за период с " + dateFrom.getTime().getDate() + " / " + dateFrom.getTime().getMonth() + " / " + (dateFrom.getTime().getYear()+ 1900) + " по " + dateTo.getTime().getDate() + " / " + dateTo.getTime().getMonth() + " / " + (dateTo.getTime().getYear()+ 1900);
        text_view_report_info.setText(text);

        button_date_from.setOnClickListener(
                v -> {
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dateFrom.set(year, monthOfYear + 1, dayOfMonth);
                            text_view_report_info.clearComposingText();
                            String text = "Отчет по количеству поставок за период с " + dateFrom.getTime().getDate() + " / " + dateFrom.getTime().getMonth() + " / " + (dateFrom.getTime().getYear()+ 1900) + " по " + dateTo.getTime().getDate() + " / " + dateTo.getTime().getMonth() + " / " + (dateTo.getTime().getYear()+ 1900);
                            text_view_report_info.setText(text);
                        }
                    };
                    DatePickerDialog datePickerDialog;
                    datePickerDialog = new DatePickerDialog(this,
                            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            dateSetListener, 2022, 0, 1);

                    datePickerDialog.show();
                }
        );

        button_date_to.setOnClickListener(
                v -> {
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dateTo.set(year, monthOfYear + 1, dayOfMonth);
                            text_view_report_info.clearComposingText();
                            String text = "Отчет по количеству поставок за период с " + dateFrom.getTime().getDate() + " / " + dateFrom.getTime().getMonth() + " / " + (dateFrom.getTime().getYear()+ 1900) + " по " + dateTo.getTime().getDate() + " / " + dateTo.getTime().getMonth() + " / " + (dateTo.getTime().getYear()+ 1900);
                            text_view_report_info.setText(text);
                        }
                    };
                    DatePickerDialog datePickerDialog;
                    datePickerDialog = new DatePickerDialog(this,
                            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            dateSetListener, 2022, 0, 1);

                    datePickerDialog.show();
                }
        );

        button_report.setOnClickListener(
                v -> {
                    Report report = new Report();
                    MachineLogic shiftLogic = new MachineLogic(this);

                    try {
                        report.generatePdf(shiftLogic.getFullList(), dateFrom.getTime(), dateTo.getTime());
                        Toast.makeText(ReportActivity.this, "Отчет успешно сформирован!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );


    }
}