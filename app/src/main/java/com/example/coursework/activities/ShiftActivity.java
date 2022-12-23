package com.example.coursework.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursework.R;
import com.example.coursework.database.logics.ShiftLogic;
import com.example.coursework.database.models.ShiftModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ShiftActivity extends AppCompatActivity {
    String urlCreate = "http://192.168.31.7:8000/gotowork/shift/create.php";
    String urlUpdate = "http://192.168.31.7:8000/gotowork/shift/update.php";
    String type, date, bossId;

    Button button_create;
    Button button_cancel;
    EditText edit_text_type;
    DatePicker date_picker_shift_date;
    ShiftLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        type = date = bossId = "";

        int id = getIntent().getExtras().getInt("id");
        logic = new ShiftLogic(this);

        button_create = findViewById(R.id.button_create);
        button_cancel = findViewById(R.id.button_cancel);
        edit_text_type = findViewById(R.id.edit_text_shift_type);
        date_picker_shift_date = findViewById(R.id.date_picker_shift_date);

        if (id != 0 && LoginActivity.checkBoxOfflineMode.isChecked()) {
            logic.open();
            ShiftModel model = logic.getElement(id);
            logic.close();

            edit_text_type.setText(model.getType());
            Date date = new Date(model.getDate());
            int year = date.getYear() + 1900;
            int month = date.getMonth();
            int day = date.getDate();
            date_picker_shift_date.init(year, month, day, null);
        }

        button_create.setOnClickListener(
                v -> {
                    // OFFLINE
                    if (LoginActivity.checkBoxOfflineMode.isChecked()) {
                        Calendar date = new GregorianCalendar();
                        date.set(date_picker_shift_date.getYear(), date_picker_shift_date.getMonth(), date_picker_shift_date.getDayOfMonth());

                        ShiftModel model = new ShiftModel(edit_text_type.getText().toString(), date.getTime().getTime(), id);
                        logic.open();

                        if (id != 0) {
                            model.setId(id);
                            logic.update(model);
                        } else {
                            logic.insert(model);
                        }

                        logic.close();
                        this.finish();
                    } else { // ONLINE
                        Calendar calendar = new GregorianCalendar();
                        calendar.set(date_picker_shift_date.getYear(), date_picker_shift_date.getMonth(), date_picker_shift_date.getDayOfMonth());

                        bossId = getIntent().getExtras().getString("bossStrId");
                        type = edit_text_type.getText().toString().trim();
                        Date shiftTimeDate = new Date(calendar.getTime().getTime());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        date = simpleDateFormat.format(shiftTimeDate);

                        // CREATE
                        if (id == 0) {
                            if (!bossId.equals("") && !type.equals("") || !date.equals("")) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCreate, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("success")) {
                                            Toast.makeText(ShiftActivity.this, "Успех!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else if (response.equals("failure")) {
                                            Toast.makeText(ShiftActivity.this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> data = new HashMap<>();
                                        data.put("type", type);
                                        data.put("shift_date", date);
                                        data.put("boss_id", bossId);
                                        return data;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);
                            }
                            this.finish();
                        } else { // UPDATE
                            if (!Integer.toString(id).equals("") && !bossId.equals("") && !type.equals("") || !date.equals("")) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("success")) {
                                            Toast.makeText(ShiftActivity.this, "Успех!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else if (response.equals("failure")) {
                                            Toast.makeText(ShiftActivity.this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> data = new HashMap<>();
                                        data.put("id", Integer.toString(id));
                                        data.put("type", type);
                                        data.put("shift_date", date);
                                        data.put("boss_id", bossId);
                                        return data;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);
                            }
                            this.finish();
                        }
                    }
                }
        );

        button_cancel.setOnClickListener(
                v -> this.finish()
        );
    }
}