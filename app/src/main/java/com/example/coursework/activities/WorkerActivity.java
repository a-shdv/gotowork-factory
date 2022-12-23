package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursework.R;
import com.example.coursework.database.logics.WorkerLogic;
import com.example.coursework.database.models.WorkerModel;

import java.util.HashMap;
import java.util.Map;

public class WorkerActivity extends AppCompatActivity {

    String URL = "http://192.168.31.7:8000/gotowork/worker/create.php";
    String name, salary;

    Button button_create;
    Button button_cancel;
    EditText edit_text_worker_name;
    EditText edit_text_salary;
    WorkerLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        int id = getIntent().getExtras().getInt("id");

        logic = new WorkerLogic(this);

        button_create = findViewById(R.id.button_create);
        button_cancel = findViewById(R.id.button_cancel);
        edit_text_worker_name = findViewById(R.id.edit_text_worker_name);
        edit_text_salary = findViewById(R.id.edit_text_salary);

        name = salary = "";

        if (id != 0) {
            logic.open();
            WorkerModel model = logic.getElement(id);
            logic.close();

            edit_text_worker_name.setText(model.getName());
            edit_text_salary.setText(String.valueOf(model.getSalary()));
        }

        button_create.setOnClickListener(
                v -> {
                    // OFFLINE
                    if (LoginActivity.checkBoxOfflineMode.isChecked()) {
                        WorkerModel model = new WorkerModel(edit_text_worker_name.getText().toString(),
                                Float.parseFloat(edit_text_salary.getText().toString()));
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
                        name = edit_text_worker_name.getText().toString().trim();
                        salary = edit_text_salary.getText().toString().trim();
                        if (!name.equals("") || !salary.equals("")) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("success")) {
                                        Toast.makeText(WorkerActivity.this, "Успех!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if (response.equals("failure")) {
                                        Toast.makeText(WorkerActivity.this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("name", name);
                                    data.put("salary", salary);
                                    return data;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
                            this.finish();
                        }
                    }
                }
        );

        button_cancel.setOnClickListener(
                v -> {
                    this.finish();
                }
        );
    }
}