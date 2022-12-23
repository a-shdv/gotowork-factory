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
import com.example.coursework.database.models.ShiftModel;

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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class ShiftsActivity extends AppCompatActivity {
    URL url;
    InputStream inputStream;
    String line = "";
    String result = "";
    String bossStrId;

    TableRow selectedRow;
    Button button_create;
    Button button_update;
    Button button_delete;
    Button button_back;
    ShiftLogic logic;
    int bossIntId;

    @Override
    public void onResume() {
        super.onResume();
        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            logic.open();
            fillTable(Arrays.asList("Тип смены", "Дата смены"), logic.getFilteredList(bossIntId));
            logic.close();
        } else {
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            try {
                url = new URL("http://192.168.31.7:8000/gotowork/shift/get.php");
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
                fillTableFromJSON(Arrays.asList("Тип смены", "Дата смены"), new JSONArray(result));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            bossIntId = getIntent().getExtras().getInt("bossIntId");
            logic.open();
            fillTable(Arrays.asList("Тип смены", "Дата смены"), logic.getFilteredList(bossIntId));
            logic.close();
        } else {
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            try {
                bossStrId = getIntent().getExtras().getString("bossStrId");
                url = new URL("http://192.168.31.7:8000/gotowork/shift/get.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                inputStream = new BufferedInputStream(connection.getInputStream());
                fillTableFromJSON(Arrays.asList("Тип смены", "Дата смены"), new JSONArray(result));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
                fillTableFromJSON(Arrays.asList("Тип смены", "Дата смены"), new JSONArray(result));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        button_create.setOnClickListener(v -> {
            Intent intent = new Intent(ShiftsActivity.this, ShiftActivity.class);

            if (bossIntId != 0)
                intent.putExtra("bossIntId", bossIntId);
            if (bossStrId != "")
                intent.putExtra("bossStrId", bossStrId);
            intent.putExtra("id", 0);
            startActivity(intent);
        });

        button_update.setOnClickListener(v -> {
            if (selectedRow != null) {
                Intent intent = new Intent(ShiftsActivity.this, ShiftActivity.class);
                intent.putExtra("bossId", bossIntId);
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
                fillTable(Arrays.asList("Тип смены", "Дата смены"), logic.getFilteredList(bossIntId));
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

        tableRowTitles.setBackgroundColor(Color.parseColor("#0A2647"));
        tableLayoutShifts.addView(tableRowTitles);


        for (ShiftModel shift : shifts) {
            TableRow tableRow = new TableRow(this);

            TextView textViewShiftType = new TextView(this);
            textViewShiftType.setText(shift.getType());
            textViewShiftType.setHeight(100);
            textViewShiftType.setTextSize(16);
            textViewShiftType.setTextColor(Color.WHITE);
            textViewShiftType.setGravity(Gravity.CENTER);

            // Formatting date
            Date shiftTimeDate = new Date(shift.getDate());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String formattedShiftTimeDate = dateFormat.format(shiftTimeDate);

            TextView textViewShiftDate = new TextView(this);
            textViewShiftDate.setText(formattedShiftTimeDate);
            textViewShiftDate.setHeight(100);
            textViewShiftDate.setTextSize(16);
            textViewShiftDate.setTextColor(Color.WHITE);
            textViewShiftDate.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(String.valueOf(shift.getId()));

            tableRow.addView(textViewShiftType);
            tableRow.addView(textViewShiftDate);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#0A2647"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int j = 0; j < tableLayoutShifts.getChildCount(); j++) {
                    View view = tableLayoutShifts.getChildAt(j);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#0A2647"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#144272"));
            });

            tableLayoutShifts.addView(tableRow);
        }
    }

    void fillTableFromJSON(List<String> titles, JSONArray shifts) throws JSONException {
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

        tableRowTitles.setBackgroundColor(Color.parseColor("#0A2647"));
        tableLayoutShifts.addView(tableRowTitles);


        for(int i = 0; i < shifts.length(); i++){
            JSONObject json = shifts.getJSONObject(i);
            String id = json.getString("id");
            String name = json.getString("type");
            String shift = json.getString("shift_date");

            TableRow tableRow = new TableRow(this);

            TextView textViewName = new TextView(this);
            textViewName.setText(name);
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewShiftDate = new TextView(this);
            textViewShiftDate.setHeight(100);
            textViewShiftDate.setTextSize(16);
            textViewShiftDate.setText(shift);
            textViewShiftDate.setTextColor(Color.WHITE);
            textViewShiftDate.setGravity(Gravity.CENTER);

            TextView textViewId = new TextView(this);
            textViewId.setVisibility(View.INVISIBLE);
            textViewId.setText(id);

            tableRow.addView(textViewName);
            tableRow.addView(textViewShiftDate);
            tableRow.addView(textViewId);

            tableRow.setBackgroundColor(Color.parseColor("#0A2647"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int j = 0; j < tableLayoutShifts.getChildCount(); j++) {
                    View view = tableLayoutShifts.getChildAt(j);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#0A2647"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#144272"));
            });

            tableLayoutShifts.addView(tableRow);
        }
    }

}
