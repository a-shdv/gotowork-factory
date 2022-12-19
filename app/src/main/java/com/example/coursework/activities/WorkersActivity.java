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
import com.example.coursework.database.logics.WorkerLogic;
import com.example.coursework.database.models.WorkerModel;

import java.util.Arrays;
import java.util.List;

public class WorkersActivity extends AppCompatActivity {

    TableRow selectedRow;
    Button button_create;
    Button button_update;
    Button button_delete;
    Button button_back;
    WorkerLogic logic;

    @Override
    public void onResume() {
        super.onResume();
        logic.open();
        fillTable(Arrays.asList("Имя", "Зарплата"), logic.getFullList());
        logic.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        button_create = findViewById(R.id.button_create);
        button_update = findViewById(R.id.button_update);
        button_delete = findViewById(R.id.button_delete);
        button_back = findViewById(R.id.button_back);

        logic = new WorkerLogic(this);

        button_create.setOnClickListener(
                v -> {
                    Intent intent = new Intent(WorkersActivity.this, WorkerActivity.class);
                    intent.putExtra("id", 0);
                    startActivity(intent);
                }
        );

        button_update.setOnClickListener(
                v -> {
                    if(selectedRow != null) {
                        Intent intent = new Intent(WorkersActivity.this, WorkerActivity.class);
                        TextView textView = (TextView) selectedRow.getChildAt(3);
                        intent.putExtra("id", Integer.valueOf(textView.getText().toString()));
                        startActivity(intent);
                        selectedRow = null;
                    }
                }
        );

        button_delete.setOnClickListener(
                v -> {
                    if(selectedRow != null) {
                        logic.open();
                        TextView textView = (TextView) selectedRow.getChildAt(3);
                        logic.delete(Integer.valueOf(textView.getText().toString()));
                        fillTable(Arrays.asList("Имя", "Зарплата"), logic.getFullList());
                        logic.close();
                        selectedRow = null;
                    }
                }
        );

        button_back.setOnClickListener(v -> {
            finish();
        });

        logic.open();
        fillTable(Arrays.asList("Имя", "Зарплата"), logic.getFullList());
        logic.close();

    }

    void fillTable(List<String> titles, List<WorkerModel> workers) {

        TableLayout tableLayoutWorkers = findViewById(R.id.tableLayoutWorkers);

        tableLayoutWorkers.removeAllViews();

        TableRow tableRowTitles = new TableRow(this);

        for (String title : titles) {
            TextView textView = new TextView(this);

            textView.setTextSize(16);
            textView.setText(title);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setWidth( (int)(getWindowManager().getDefaultDisplay().getWidth() / 2.2));
            tableRowTitles.addView(textView);
        }

        tableRowTitles.setBackgroundColor(Color.parseColor("#FF6200EE"));
        tableLayoutWorkers.addView(tableRowTitles);


        for (WorkerModel worker : workers) {
            TableRow tableRow = new TableRow(this);

            TextView textViewName = new TextView(this);
            textViewName.setText(worker.getName());
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewSalary = new TextView(this);
            textViewName.setHeight(100);
            textViewSalary.setTextSize(16);
            textViewSalary.setText(String.valueOf(worker.getSalary()));
            textViewSalary.setTextColor(Color.WHITE);
            textViewSalary.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(String.valueOf(worker.getId()));

            tableRow.addView(textViewName);
            tableRow.addView(textViewSalary);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#FF6200EE"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for(int i = 0; i < tableLayoutWorkers.getChildCount(); i++){
                    View view = tableLayoutWorkers.getChildAt(i);
                    if (view instanceof TableRow){
                        view.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            });

            tableLayoutWorkers.addView(tableRow);
        }
    }
}