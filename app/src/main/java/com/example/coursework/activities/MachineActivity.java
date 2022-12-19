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
    Button button_add_worker;
    Button button_delete_worker;

    Calendar discharge_date;
    Calendar receiving_date;

    EditText edit_text_count;

    List<MachineWorkersModel> machineWorkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        logic = new MachineLogic(this);

        int id = getIntent().getExtras().getInt("id");

        if(id != 0){
            MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(this);
            machineWorkers = machineWorkersLogic.getFilteredList(id);
        }

        button_discharge_date = findViewById(R.id.button_discharge_date);
        button_receiving_date = findViewById(R.id.button_receiving_date);
        button_create = findViewById(R.id.button_create);
        button_cancel = findViewById(R.id.button_cancel);
        button_add_worker = findViewById(R.id.button_add_worker);
        button_delete_worker = findViewById(R.id.button_delete_worker);

        discharge_date = new GregorianCalendar();
        receiving_date = new GregorianCalendar();

        edit_text_count = findViewById(R.id.edit_text_count);

        //комбо бокс покупателей
        ShiftLogic shiftLogic = new ShiftLogic(this);
        shiftLogic.open();
        List<ShiftModel> shifts = shiftLogic.getFullList();
        List<String> shiftsNames = new LinkedList<>();
        shiftLogic.close();

        for (ShiftModel shift : shifts) {
            shiftsNames.add(shift.getName());
        }

        Spinner spinnerShifts = findViewById(R.id.spinner_shifts);
        ArrayAdapter<String> adapterShifts = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shiftsNames);
        adapterShifts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShifts.setAdapter(adapterShifts);

        //комбо бокс лекарств
        WorkerLogic workerLogic = new WorkerLogic(this);
        workerLogic.open();
        List<WorkerModel> workers = workerLogic.getFullList();
        List<String> workersNames = new LinkedList<>();
        workerLogic.close();

        for (WorkerModel worker : workers) {
            workersNames.add(worker.getName());
        }

        Spinner spinnerWorkers = findViewById(R.id.spinner_workers);
        ArrayAdapter<String> adapterWorkers = new ArrayAdapter(this, android.R.layout.simple_spinner_item, workersNames);
        adapterWorkers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWorkers.setAdapter(adapterWorkers);

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
                    int shiftId = shifts.get(spinnerShifts.getSelectedItemPosition()).getId();
                    String shiftName =  shifts.get(spinnerShifts.getSelectedItemPosition()).getName();
                    MachineModel model = new MachineModel(receiving_date.getTime().getTime(), discharge_date.getTime().getTime(), shiftId,
                           shiftName, machineWorkers);
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

        button_add_worker.setOnClickListener(
                v -> {
                    int workerId = workers.get(spinnerWorkers.getSelectedItemPosition()).getId();
                    for(MachineWorkersModel machineWorker : machineWorkers){
                        if(machineWorker.getWorkerId() == workerId){
                            return;
                        }
                    }
                    machineWorkers.add(new MachineWorkersModel(id, workerId, Integer.valueOf(edit_text_count.getText().toString())));
                    edit_text_count.setText("");
                    spinnerWorkers.setSelection(0);
                    fillTable(Arrays.asList("Имя работника", "Количество"), machineWorkers);
                }
        );

        button_delete_worker.setOnClickListener(
                v -> {
                    TextView textView = (TextView) selectedRow.getChildAt(2);
                    int index = Integer.valueOf(textView.getText().toString());
                    machineWorkers.remove(index);
                    fillTable(Arrays.asList("Название лекарства", "Количество"), machineWorkers);
                }
        );

        fillTable(Arrays.asList("Название лекарства", "Количество"), machineWorkers);
    }


    void fillTable(List<String> titles, List<MachineWorkersModel> machineWorkers) {

        TableLayout tableLayoutMachineWorkers = findViewById(R.id.tableLayoutMachineWorkers);

        tableLayoutMachineWorkers.removeAllViews();

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
        tableLayoutMachineWorkers.addView(tableRowTitles);

        int index = 0;
        for (MachineWorkersModel machineWorker : machineWorkers) {
            TableRow tableRow = new TableRow(this);

            WorkerLogic workerLogic = new WorkerLogic(this);

            TextView textViewName = new TextView(this);
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setText(workerLogic.getElement(machineWorker.getWorkerId()).getName());
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewCount = new TextView(this);
            textViewCount.setHeight(100);
            textViewCount.setTextSize(16);
            textViewCount.setText(String.valueOf(machineWorker.getCount()));
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

                for (int i = 0; i < tableLayoutMachineWorkers.getChildCount(); i++) {
                    View view = tableLayoutMachineWorkers.getChildAt(i);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            });

            tableLayoutMachineWorkers.addView(tableRow);
            index++;
        }
    }
}