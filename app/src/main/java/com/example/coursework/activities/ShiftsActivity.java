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
import com.example.coursework.database.models.ShiftModel;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class ShiftsActivity extends AppCompatActivity {

    TableRow selectedRow;

    Button button_create;
    Button button_update;
    Button button_delete;
    Button button_back;
    ShiftLogic logic;
    int userId;

    @Override
    public void onResume() {
        super.onResume();
        logic.open();
        fillTable(Arrays.asList("Тип смены", "Дата смены"), logic.getFilteredList(userId));
        logic.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        button_create = findViewById(R.id.button_create);
        button_update = findViewById(R.id.button_update);
        button_delete = findViewById(R.id.button_delete);
        button_back = findViewById(R.id.button_back);

        logic = new ShiftLogic(this);

        userId = getIntent().getExtras().getInt("userId");

        logic.open();
        fillTable(Arrays.asList("Тип смены", "Дата смены"), logic.getFilteredList(userId));
        logic.close();

        button_create.setOnClickListener(v -> {
            Intent intent = new Intent(ShiftsActivity.this, ShiftActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("id", 0);
            startActivity(intent);
        });

        button_update.setOnClickListener(v -> {
            if (selectedRow != null) {
                Intent intent = new Intent(ShiftsActivity.this, ShiftActivity.class);
                intent.putExtra("userId", userId);
                TextView textView = (TextView) selectedRow.getChildAt(2);
                intent.putExtra("id", Integer.valueOf(textView.getText().toString()));
                startActivity(intent);
                selectedRow = null;
            }
        });

        button_delete.setOnClickListener(v -> {
            if (selectedRow != null) {
                logic.open();
                TextView textView = (TextView) selectedRow.getChildAt(2);
                logic.delete(Integer.parseInt(textView.getText().toString()));
                fillTable(Arrays.asList("Тип смены", "Дата смены"), logic.getFilteredList(userId));
                logic.close();
                selectedRow = null;
            }
        });

        button_back.setOnClickListener(v -> {
            finish();
        });
    }


    void fillTable(List<String> titles, List<ShiftModel> shifts) {

        TableLayout tableLayoutShifts = findViewById(R.id.tableLayoutShifts);

        tableLayoutShifts.removeAllViews();

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
        tableLayoutShifts.addView(tableRowTitles);


        for (ShiftModel shift : shifts) {
            TableRow tableRow = new TableRow(this);

            TextView textViewName = new TextView(this);
            textViewName.setText(shift.getType());
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewShiftDate = new TextView(this);
            textViewName.setHeight(100);
            textViewShiftDate.setTextSize(16);
            textViewShiftDate.setText(String.valueOf(new Date(shift.getDate())));
            textViewShiftDate.setTextColor(Color.WHITE);
            textViewShiftDate.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(String.valueOf(shift.getId()));

            tableRow.addView(textViewName);
            tableRow.addView(textViewShiftDate);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#FF6200EE"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int i = 0; i < tableLayoutShifts.getChildCount(); i++) {
                    View view = tableLayoutShifts.getChildAt(i);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            });

            tableLayoutShifts.addView(tableRow);
        }
    }
}