package com.example.coursework.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coursework.R;
import com.example.coursework.database.logics.ShiftLogic;
import com.example.coursework.database.models.ShiftModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ShiftActivity extends AppCompatActivity {

    Button button_create;
    Button button_cancel;
    EditText edit_text_name;
    DatePicker date_picker_shift_date;
    ShiftLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        int userId = getIntent().getExtras().getInt("userId");
        int id = getIntent().getExtras().getInt("id");

        logic = new ShiftLogic(this);

        button_create = findViewById(R.id.button_create);
        button_cancel = findViewById(R.id.button_cancel);
        edit_text_name = findViewById(R.id.edit_text_shift_type);
        date_picker_shift_date = findViewById(R.id.date_picker_shift_date);

        if (id != 0){
            logic.open();
            ShiftModel model = logic.getElement(id);
            logic.close();

            edit_text_name.setText(model.getType());
            Date date = new Date(model.getDate());
            int year = date.getYear() + 1900;
            int month = date.getMonth();
            int day = date.getDate();
            date_picker_shift_date.init(year, month, day,null );
        }

        button_create.setOnClickListener(
                v -> {
                    Calendar date = new GregorianCalendar();
                    date.set( date_picker_shift_date.getYear(), date_picker_shift_date.getMonth(), date_picker_shift_date.getDayOfMonth());

                    ShiftModel model = new ShiftModel(edit_text_name.getText().toString(),date.getTime().getTime(), userId);
                    logic.open();

                    if(id != 0){
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
                v -> this.finish()
        );
    }
}