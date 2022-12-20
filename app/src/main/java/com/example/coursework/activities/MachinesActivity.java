package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.database.logics.ShiftLogic;
import com.example.coursework.database.logics.MachineLogic;
import com.example.coursework.database.models.ShiftModel;
import com.example.coursework.database.models.MachineModel;

import java.util.Arrays;
import java.util.List;

public class MachinesActivity extends AppCompatActivity {

    TableRow selectedRow;

    Button button_create;
    Button button_update;
    Button button_delete;
    Button button_back;

    MachineLogic logic;

    @Override
    public void onResume() {
        super.onResume();
        logic.open();
        fillTable(Arrays.asList( "Тип смены", "Начало смены", "Конец смены"), logic.getFullList());
        logic.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machines);

        button_create = findViewById(R.id.button_create);
        button_update = findViewById(R.id.button_update);
        button_delete = findViewById(R.id.button_delete);
        button_back = findViewById(R.id.button_back);

        logic = new MachineLogic(this);

        button_create.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MachinesActivity.this, MachineActivity.class);
                    intent.putExtra("id", 0);
                    startActivity(intent);
                }
        );

        button_update.setOnClickListener(
                v -> {
                    if (selectedRow != null) {
                        Intent intent = new Intent(MachinesActivity.this, MachineActivity.class);
                        TextView textView = (TextView) selectedRow.getChildAt(3);
                        intent.putExtra("id", Integer.valueOf(textView.getText().toString()));
                        startActivity(intent);
                        selectedRow = null;
                    }
                }
        );

        button_delete.setOnClickListener(
                v -> {
                    if (selectedRow != null) {
                        logic.open();
                        TextView textView = (TextView) selectedRow.getChildAt(3);
                        logic.delete(Integer.valueOf(textView.getText().toString()));
                        fillTable(Arrays.asList("Тип смены", "Начало смены", "Конец смены"), logic.getFullList());
                        logic.close();
                        selectedRow = null;
                    }
                }
        );

        button_back.setOnClickListener(v -> {
            finish();
        });


        logic.open();
        fillTable(Arrays.asList("Тип смены", "Начало смены", "Конец смены"), logic.getFullList());
        logic.close();

    }

    void fillTable(List<String> titles, List<MachineModel> machines) {

        TableLayout tableLayoutMachines = findViewById(R.id.tableLayoutMachines);

        tableLayoutMachines.removeAllViews();

        TableRow tableRowTitles = new TableRow(this);

        for (String title : titles) {
            TextView textView = new TextView(this);

            textView.setTextSize(16);
            textView.setText(title);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setWidth((int) (getWindowManager().getDefaultDisplay().getWidth() / 3.2));
            tableRowTitles.addView(textView);
        }

        tableRowTitles.setBackgroundColor(Color.parseColor("#FF6200EE"));
        tableLayoutMachines.addView(tableRowTitles);


        for (MachineModel machine : machines) {
            TableRow tableRow = new TableRow(this);

            ShiftLogic shiftLogic = new ShiftLogic(this);
            shiftLogic.open();
            ShiftModel shift = shiftLogic.getElement(machine.getShiftId());
            shiftLogic.close();

            TextView textViewShiftType = new TextView(this);
            textViewShiftType.setHeight(100);
            textViewShiftType.setTextSize(16);
            textViewShiftType.setText(shift.getType());
            textViewShiftType.setTextColor(Color.WHITE);
            textViewShiftType.setGravity(Gravity.CENTER);

            TextView textViewShiftBegin = new TextView(this);
            textViewShiftBegin.setHeight(100);
            textViewShiftBegin.setTextSize(16);
            textViewShiftBegin.setText(String.valueOf(machine.getShift_begin_time()));
            textViewShiftBegin.setTextColor(Color.WHITE);
            textViewShiftBegin.setGravity(Gravity.CENTER);

            TextView textViewShiftEnd = new TextView(this);
            textViewShiftEnd.setHeight(100);
            textViewShiftEnd.setTextSize(16);
            textViewShiftEnd.setText(String.valueOf(machine.getShift_end_time()));
            textViewShiftEnd.setTextColor(Color.WHITE);
            textViewShiftEnd.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
/*
            textViewId.setTextColor(Color.WHITE);
*/
            textViewId.setText(String.valueOf(machine.getId()));

            tableRow.addView(textViewShiftType);
            tableRow.addView(textViewShiftBegin);
            tableRow.addView(textViewShiftEnd);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#FF6200EE"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int i = 0; i < tableLayoutMachines.getChildCount(); i++) {
                    View view = tableLayoutMachines.getChildAt(i);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            });

            tableLayoutMachines.addView(tableRow);
        }
    }
}