package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.database.logics.ShiftLogic;
import com.example.coursework.database.logics.WorkerLogic;
import com.example.coursework.database.logics.MachineLogic;
import com.example.coursework.database.logics.MachineWorkersLogic;
import com.example.coursework.database.models.ShiftModel;
import com.example.coursework.database.models.WorkerModel;
import com.example.coursework.database.models.MachineWorkersModel;
import com.example.coursework.database.models.MachineModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class MachineActivity extends AppCompatActivity {

    TableRow selectedRow;

    MachineLogic logic;

    Button button_discharge_date;
    Button button_receiving_date;
    Button button_create;
    Button button_cancel;
    Button button_add_medicine;
    Button button_delete_medicine;

    Calendar discharge_date;
    Calendar receiving_date;

    EditText edit_text_count;

    List<MachineWorkersModel> receiptMedicines = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        logic = new MachineLogic(this);

        int id = getIntent().getExtras().getInt("id");

        if(id != 0){
            MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(this);
            receiptMedicines = machineWorkersLogic.getFilteredList(id);
        }

        button_discharge_date = findViewById(R.id.button_discharge_date);
        button_receiving_date = findViewById(R.id.button_receiving_date);
        button_create = findViewById(R.id.button_create);
        button_cancel = findViewById(R.id.button_cancel);
        button_add_medicine = findViewById(R.id.button_add_medicine);
        button_delete_medicine = findViewById(R.id.button_delete_medicine);

        discharge_date = new GregorianCalendar();
        receiving_date = new GregorianCalendar();

        edit_text_count = findViewById(R.id.edit_text_count);

        //комбо бокс покупателей
        ShiftLogic shiftLogic = new ShiftLogic(this);
        shiftLogic.open();
        List<ShiftModel> suppliers = shiftLogic.getFullList();
        List<String> suppliersNames = new LinkedList<>();
        shiftLogic.close();

        for (ShiftModel supplier : suppliers) {
            suppliersNames.add(supplier.getName());
        }

        Spinner spinnerSuppliers = findViewById(R.id.spinner_suppliers);
        ArrayAdapter<String> adapterSuppliers = new ArrayAdapter(this, android.R.layout.simple_spinner_item, suppliersNames);
        adapterSuppliers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSuppliers.setAdapter(adapterSuppliers);

        //комбо бокс лекарств
        WorkerLogic workerLogic = new WorkerLogic(this);
        workerLogic.open();
        List<WorkerModel> medicines = workerLogic.getFullList();
        List<String> medicinesNames = new LinkedList<>();
        workerLogic.close();

        for (WorkerModel medicine : medicines) {
            medicinesNames.add(medicine.getName());
        }

        Spinner spinnerMedicines = findViewById(R.id.spinner_medicines);
        ArrayAdapter<String> adapterMedicines = new ArrayAdapter(this, android.R.layout.simple_spinner_item, medicinesNames);
        adapterMedicines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicines.setAdapter(adapterMedicines);

        button_discharge_date.setOnClickListener(
                v -> {
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            discharge_date.set(year, monthOfYear + 1, dayOfMonth);
                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            dateSetListener, 2021, 0, 1);

                    datePickerDialog.show();
                }
        );

        button_receiving_date.setOnClickListener(
                v -> {
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            receiving_date.set(year, monthOfYear + 1, dayOfMonth);
                        }
                    };
                    DatePickerDialog datePickerDialog;
                        datePickerDialog = new DatePickerDialog(this,
                                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                                dateSetListener, 2021, 0, 1);

                    datePickerDialog.show();
                }
        );

        button_create.setOnClickListener(
                v -> {
                    int supplierId = suppliers.get(spinnerSuppliers.getSelectedItemPosition()).getId();
                    String supplierName =  suppliers.get(spinnerSuppliers.getSelectedItemPosition()).getName();
                    MachineModel model = new MachineModel(receiving_date.getTime().getTime(), discharge_date.getTime().getTime(), supplierId,
                           supplierName, receiptMedicines);
                    logic.open();


                    if (id != 0) {
                        model.setId(id);
                        logic.update(model);

                    } else {
                        logic.insert(model);
                    }

                    logic.close();
                    this.finish();
                }
        );

        button_cancel.setOnClickListener(
                v -> {
                    this.finish();
                }
        );

        button_add_medicine.setOnClickListener(
                v -> {
                    int medicineId = medicines.get(spinnerMedicines.getSelectedItemPosition()).getId();
                    for(MachineWorkersModel receiptMedicine : receiptMedicines){
                        if(receiptMedicine.getMedicineId() == medicineId){
                            return;
                        }
                    }
                    receiptMedicines.add(new MachineWorkersModel(id, medicineId, Integer.valueOf(edit_text_count.getText().toString())));
                    edit_text_count.setText("");
                    spinnerMedicines.setSelection(0);
                    fillTable(Arrays.asList("Название лекарства", "Количество"), receiptMedicines);
                }
        );

        button_delete_medicine.setOnClickListener(
                v -> {
                    TextView textView = (TextView) selectedRow.getChildAt(2);
                    int index = Integer.valueOf(textView.getText().toString());
                    receiptMedicines.remove(index);
                    fillTable(Arrays.asList("Название лекарства", "Количество"), receiptMedicines);
                }
        );

        fillTable(Arrays.asList("Название лекарства", "Количество"), receiptMedicines);
    }


    void fillTable(List<String> titles, List<MachineWorkersModel> receiptMedicines) {

        TableLayout tableLayoutReceiptMedicines = findViewById(R.id.tableLayoutReceiptMedicines);

        tableLayoutReceiptMedicines.removeAllViews();

        TableRow tableRowTitles = new TableRow(this);

        for (String title : titles) {
            TextView textView = new TextView(this);

            textView.setTextSize(16);
            textView.setText(title);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setWidth((int) (getWindowManager().getDefaultDisplay().getWidth() / 2.2));
            tableRowTitles.addView(textView);
        }

        tableRowTitles.setBackgroundColor(Color.parseColor("#FF6200EE"));
        tableLayoutReceiptMedicines.addView(tableRowTitles);

        int index = 0;
        for (MachineWorkersModel receiptMedicine : receiptMedicines) {
            TableRow tableRow = new TableRow(this);

            WorkerLogic workerLogic = new WorkerLogic(this);

            TextView textViewName = new TextView(this);
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setText(workerLogic.getElement(receiptMedicine.getMedicineId()).getName());
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewCount = new TextView(this);
            textViewCount.setHeight(100);
            textViewCount.setTextSize(16);
            textViewCount.setText(String.valueOf(receiptMedicine.getCount()));
            textViewCount.setTextColor(Color.WHITE);
            textViewCount.setGravity(Gravity.CENTER);

            TextView textViewIndex = new TextView(this);
            textViewIndex.setVisibility(View.INVISIBLE);
            textViewIndex.setText(String.valueOf(index));

            tableRow.addView(textViewName);
            tableRow.addView(textViewCount);
            tableRow.addView(textViewIndex);

            tableRow.setBackgroundColor(Color.parseColor("#FF6200EE"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int i = 0; i < tableLayoutReceiptMedicines.getChildCount(); i++) {
                    View view = tableLayoutReceiptMedicines.getChildAt(i);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            });

            tableLayoutReceiptMedicines.addView(tableRow);
            index++;
        }
    }
}