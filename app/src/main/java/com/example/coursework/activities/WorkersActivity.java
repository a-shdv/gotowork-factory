package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.database.logics.WorkerLogic;
import com.example.coursework.database.models.WorkerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkersActivity extends AppCompatActivity {
    URL url;
    InputStream inputStream;
    String line = "";
    String result = "";
    String urlDelete = "http://192.168.31.7:8000/gotowork/worker/delete.php";

    TableRow selectedRow;
    Button button_create;
    Button button_update;
    Button button_delete;
    Button button_back;
    WorkerLogic logic;

    @Override
    public void onResume() {
        super.onResume();
        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            logic.open();
            fillTable(Arrays.asList("Имя", "Зарплата"), logic.getFullList());
            logic.close();
        } else {
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            try {
                url = new URL("http://192.168.31.7:8000/gotowork/worker/get.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                inputStream = new BufferedInputStream(connection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Read inputStream content into a String
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                inputStream.close();
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fillTableFromJSON(Arrays.asList("Имя", "Зарплата"), new JSONArray(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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
                    if (selectedRow != null) {
                        Intent intent = new Intent(WorkersActivity.this, WorkerActivity.class);
                        TextView textView = (TextView) selectedRow.getChildAt(2);
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
                        TextView textView = (TextView) selectedRow.getChildAt(2);
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

        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            logic.open();
            fillTable(Arrays.asList("Имя", "Зарплата"), logic.getFullList());
            logic.close();
        } else {
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            try {
                url = new URL("http://192.168.31.7:8000/gotowork/worker/get.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                inputStream = new BufferedInputStream(connection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Read inputStream content into a String
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                inputStream.close();
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fillTableFromJSON(Arrays.asList("Имя", "Зарплата"), new JSONArray(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            textView.setWidth((int) (getWindowManager().getDefaultDisplay().getWidth() / 2.2));
            tableRowTitles.addView(textView);
        }

        tableRowTitles.setBackgroundColor(Color.parseColor("#0A2647"));
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

            tableRow.setBackgroundColor(Color.parseColor("#0A2647"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int j = 0; j < tableLayoutWorkers.getChildCount(); j++) {
                    View view = tableLayoutWorkers.getChildAt(j);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#0A2647"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#144272"));
            });

            tableLayoutWorkers.addView(tableRow);
        }
    }

    void fillTableFromJSON(List<String> titles, JSONArray workers) throws JSONException {
        TableLayout tableLayoutWorkers = findViewById(R.id.tableLayoutWorkers);

        tableLayoutWorkers.removeAllViews();

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

        tableRowTitles.setBackgroundColor(Color.parseColor("#0A2647"));
        tableLayoutWorkers.addView(tableRowTitles);


        for(int i = 0; i < workers.length(); i++){
            JSONObject json = workers.getJSONObject(i);
            String id = json.getString("id");
            String name = json.getString("name");
            String salary = json.getString("salary");

            TableRow tableRow = new TableRow(this);

            TextView textViewName = new TextView(this);
            textViewName.setText(name);
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewSalary = new TextView(this);
            textViewSalary.setHeight(100);
            textViewSalary.setTextSize(16);
            textViewSalary.setText(salary);
            textViewSalary.setTextColor(Color.WHITE);
            textViewSalary.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(id);

            tableRow.addView(textViewName);
            tableRow.addView(textViewSalary);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#0A2647"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int j = 0; j < tableLayoutWorkers.getChildCount(); j++) {
                    View view = tableLayoutWorkers.getChildAt(j);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#0A2647"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#144272"));
            });

            tableLayoutWorkers.addView(tableRow);
        }
    }

}