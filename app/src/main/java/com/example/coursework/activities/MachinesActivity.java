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
import com.example.coursework.database.logics.ShiftLogic;
import com.example.coursework.database.logics.MachineLogic;
import com.example.coursework.database.models.ShiftModel;
import com.example.coursework.database.models.MachineModel;

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
import java.util.Arrays;
import java.util.List;

public class MachinesActivity extends AppCompatActivity {
    URL url;
    InputStream inputStream;
    String line = "";
    String result = "";
    TableRow selectedRow;

    Button button_create;
    Button button_update;
    Button button_delete;
    Button button_back;

    MachineLogic logic;

    @Override
    public void onResume() {
        super.onResume();
        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            logic.open();
            fillTable(Arrays.asList("Тип станка", "Смена", "Количество работников"), logic.getFullList());
            logic.close();
        } else {
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            try {
                url = new URL("http://192.168.31.7:8000/gotowork/machine/get.php");
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
                fillTableFromJSON(Arrays.asList("1", "2", "3", "4"), new JSONArray(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            logic.open();
            fillTable(Arrays.asList("Тип станка", "Смена", "Количество работников"), logic.getFullList());
            logic.close();
        } else {
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            try {
                url = new URL("http://192.168.31.7:8000/gotowork/machine/get.php");
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
                fillTableFromJSON(Arrays.asList("1", "2", "3", "4"), new JSONArray(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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
                        TextView textViewId = (TextView) selectedRow.getChildAt(3);
                        TextView textViewMachineType = (TextView) selectedRow.getChildAt(0);
                        intent.putExtra("id", Integer.valueOf(textViewId.getText().toString()));
                        intent.putExtra("machine_type", textViewMachineType.getText().toString());
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
                        fillTable(Arrays.asList("Тип станка", "Смена", "Количество работников"), logic.getFullList());
                        logic.close();
                        selectedRow = null;
                    }
                }
        );

        button_back.setOnClickListener(v -> {
            finish();
        });


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

        tableRowTitles.setBackgroundColor(Color.parseColor("#0A2647"));
        tableLayoutMachines.addView(tableRowTitles);


        for (MachineModel machine : machines) {
            TableRow tableRow = new TableRow(this);

            ShiftLogic shiftLogic = new ShiftLogic(this);
            shiftLogic.open();
            ShiftModel shift = shiftLogic.getElement(machine.getShiftId());
            shiftLogic.close();

            TextView textViewMachineType = new TextView(this);
            textViewMachineType.setHeight(100);
            textViewMachineType.setTextSize(16);
            textViewMachineType.setText(machine.getMachine_type());
            textViewMachineType.setTextColor(Color.WHITE);
            textViewMachineType.setGravity(Gravity.CENTER);

            TextView textViewShiftType = new TextView(this);
            textViewShiftType.setHeight(100);
            textViewShiftType.setTextSize(16);
            textViewShiftType.setText(shift.getType());
            textViewShiftType.setTextColor(Color.WHITE);
            textViewShiftType.setGravity(Gravity.CENTER);

            TextView textViewMachineWorkers = new TextView(this);
            textViewMachineWorkers.setHeight(100);
            textViewMachineWorkers.setTextSize(16);
            textViewMachineWorkers.setText(String.valueOf(machine.getMachineWorkers().size()));
            textViewMachineWorkers.setTextColor(Color.WHITE);
            textViewMachineWorkers.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(String.valueOf(machine.getId()));

            tableRow.addView(textViewMachineType);
            tableRow.addView(textViewShiftType);
            tableRow.addView(textViewMachineWorkers);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#0A2647"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int j = 0; j < tableLayoutMachines.getChildCount(); j++) {
                    View view = tableLayoutMachines.getChildAt(j);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#0A2647"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#144272"));
            });

            tableLayoutMachines.addView(tableRow);
        }
    }

    void fillTableFromJSON(List<String> titles, JSONArray machines) throws JSONException {
        TableLayout tableLayoutMachines = findViewById(R.id.tableLayoutMachines);

        tableLayoutMachines.removeAllViews();

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
        tableLayoutMachines.addView(tableRowTitles);


        for (int i = 0; i < machines.length(); i++) {
            JSONObject json = machines.getJSONObject(i);
            String id = json.getString("id");
            String machine_type = json.getString("machine_type");
            String shift_begin_time = json.getString("shift_begin_time");
            String shift_end_time = json.getString("shift_end_time");
            String shift_id = json.getString("shift_id");
            String shift_name = json.getString("shift_name");

            TableRow tableRow = new TableRow(this);

            TextView textViewMachineType = new TextView(this);
            textViewMachineType.setHeight(100);
            textViewMachineType.setTextSize(16);
            textViewMachineType.setText(machine_type);
            textViewMachineType.setTextColor(Color.WHITE);
            textViewMachineType.setGravity(Gravity.CENTER);

            TextView textViewShiftBeginTime = new TextView(this);
            textViewShiftBeginTime.setHeight(100);
            textViewShiftBeginTime.setTextSize(16);
            textViewShiftBeginTime.setText(shift_begin_time);
            textViewShiftBeginTime.setTextColor(Color.WHITE);
            textViewShiftBeginTime.setGravity(Gravity.CENTER);

            TextView textViewShiftEndTime = new TextView(this);
            textViewShiftEndTime.setHeight(100);
            textViewShiftEndTime.setTextSize(16);
            textViewShiftEndTime.setText(shift_end_time);
            textViewShiftEndTime.setTextColor(Color.WHITE);
            textViewShiftEndTime.setGravity(Gravity.CENTER);

            TextView textViewShiftId = new TextView(this);
            textViewShiftId.setText(shift_id);
            textViewShiftId.setVisibility(View.INVISIBLE);

            TextView textViewShiftName = new TextView(this);
            textViewShiftName.setHeight(100);
            textViewShiftName.setTextSize(16);
            textViewShiftName.setText(shift_name);
            textViewShiftName.setTextColor(Color.WHITE);
            textViewShiftName.setGravity(Gravity.CENTER);


            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(id);

            tableRow.addView(textViewMachineType);
            tableRow.addView(textViewShiftBeginTime);
            tableRow.addView(textViewShiftEndTime);
            tableRow.addView(textViewShiftId);
            tableRow.addView(textViewShiftName);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#0A2647"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int j = 0; j < tableLayoutMachines.getChildCount(); j++) {
                    View view = tableLayoutMachines.getChildAt(j);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#0A2647"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#144272"));
            });

            tableLayoutMachines.addView(tableRow);
        }

    }
}