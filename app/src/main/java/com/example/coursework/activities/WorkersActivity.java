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
        fillTable(Arrays.asList("Название", "Тип", "Стоимость за упаковку"), logic.getFullList());
        logic.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);

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
                        fillTable(Arrays.asList("Название", "Тип", "Стоимость за упаковку"), logic.getFullList());
                        logic.close();
                        selectedRow = null;
                    }
                }
        );

        button_back.setOnClickListener(v -> {
            finish();
        });

        logic.open();
        fillTable(Arrays.asList("Название", "Тип", "Стоимость за упаковку"), logic.getFullList());
        logic.close();

    }

    void fillTable(List<String> titles, List<WorkerModel> medicines) {

        TableLayout tableLayoutMedicines = findViewById(R.id.tableLayoutMedicines);

        tableLayoutMedicines.removeAllViews();

        TableRow tableRowTitles = new TableRow(this);

        for (String title : titles) {
            TextView textView = new TextView(this);

            textView.setTextSize(16);
            textView.setText(title);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setWidth( (int)(getWindowManager().getDefaultDisplay().getWidth() / 3.2));
            tableRowTitles.addView(textView);
        }

        tableRowTitles.setBackgroundColor(Color.parseColor("#FF6200EE"));
        tableLayoutMedicines.addView(tableRowTitles);


        for (WorkerModel medicine : medicines) {
            TableRow tableRow = new TableRow(this);

            TextView textViewName = new TextView(this);
            textViewName.setText(medicine.getName());
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewType = new TextView(this);
            textViewName.setHeight(100);
            textViewType.setTextSize(16);
            textViewType.setText(medicine.getType());
            textViewType.setTextColor(Color.WHITE);
            textViewType.setGravity(Gravity.CENTER);

            TextView textViewPrice = new TextView(this);
            textViewName.setHeight(100);
            textViewPrice.setTextSize(16);
            textViewPrice.setText(String.valueOf(medicine.getPrice_per_package()));
            textViewPrice.setTextColor(Color.WHITE);
            textViewPrice.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(String.valueOf(medicine.getId()));

            tableRow.addView(textViewName);
            tableRow.addView(textViewType);
            tableRow.addView(textViewPrice);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#FF6200EE"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for(int i = 0; i < tableLayoutMedicines.getChildCount(); i++){
                    View view = tableLayoutMedicines.getChildAt(i);
                    if (view instanceof TableRow){
                        view.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            });

            tableLayoutMedicines.addView(tableRow);
        }
    }
}