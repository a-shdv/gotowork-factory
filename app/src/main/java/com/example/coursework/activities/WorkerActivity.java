package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.coursework.R;
import com.example.coursework.database.logics.WorkerLogic;
import com.example.coursework.database.models.WorkerModel;

public class WorkerActivity extends AppCompatActivity {

    Button button_create;
    Button button_cancel;
    EditText edit_text_medicine_name;
    EditText edit_text_type;
    EditText edit_text_price;
    WorkerLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        int id = getIntent().getExtras().getInt("id");

        logic = new WorkerLogic(this);

        button_create = findViewById(R.id.button_create);
        button_cancel = findViewById(R.id.button_cancel);
        edit_text_medicine_name = findViewById(R.id.edit_text_medicine_name);
        edit_text_type = findViewById(R.id.edit_text_type);
        edit_text_price = findViewById(R.id.edit_text_price);

        if (id != 0) {
            logic.open();
            WorkerModel model = logic.getElement(id);
            logic.close();

            edit_text_medicine_name.setText(model.getName());
            edit_text_type.setText(model.getType());
            edit_text_price.setText(String.valueOf(model.getPrice_per_package()));
        }

        button_create.setOnClickListener(
                v -> {
                    WorkerModel model = new WorkerModel(edit_text_medicine_name.getText().toString(), edit_text_type.getText().toString(), Float.parseFloat(edit_text_price.getText().toString()));
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
    }
}