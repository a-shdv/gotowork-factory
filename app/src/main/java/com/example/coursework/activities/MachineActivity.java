package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursework.R;
import com.example.coursework.database.logics.ShiftLogic;
import com.example.coursework.database.logics.WorkerLogic;
import com.example.coursework.database.logics.MachineLogic;
import com.example.coursework.database.logics.MachineWorkersLogic;
import com.example.coursework.database.models.ShiftModel;
import com.example.coursework.database.models.WorkerModel;
import com.example.coursework.database.models.MachineWorkersModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MachineActivity extends AppCompatActivity {

    URL url;

    String urlCreate = "http://192.168.31.7:8000/gotowork/machine/create.php";
    String urlUpdate = "http://192.168.31.7:8000/gotowork/machine/update.php";
    String type, shiftBeginTime, shiftEndTime, shiftId, shiftName;
    InputStream inputStream;
    String line = "";
    String result = "";

    TableRow selectedRow;

    MachineLogic logic;
    Button button_shift_begin_time;
    Button button_shift_end_time;
    Button button_create;
    Button button_cancel;
    Button button_add_worker;
    Button button_delete_worker;

    Calendar shift_begin_time;
    Calendar shift_end_time;

    EditText edit_text_machine_type;
    EditText edit_text_hours;
    Spinner spinnerShifts;
    List<MachineWorkersModel> machineWorkers = new ArrayList<>();

    int differenceBetweenShiftHours = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        type = shiftBeginTime = shiftEndTime = shiftId = shiftName = "";

        logic = new MachineLogic(this);

        int id = getIntent().getExtras().getInt("id");

        if (id != 0) {
            MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(this);
            machineWorkers = machineWorkersLogic.getFilteredList(id);
        }

        button_add_worker = findViewById(R.id.button_add_worker);
        button_shift_begin_time = findViewById(R.id.button_shift_begin_time);
        button_shift_end_time = findViewById(R.id.button_shift_end_time);
        button_create = findViewById(R.id.button_create);
        button_cancel = findViewById(R.id.button_cancel);
        button_delete_worker = findViewById(R.id.button_delete_worker);

        shift_begin_time = new GregorianCalendar();
        shift_begin_time.set(Calendar.HOUR_OF_DAY, 0);
        shift_begin_time.set(Calendar.MINUTE, 0);

        shift_end_time = new GregorianCalendar();
        shift_end_time.set(Calendar.HOUR_OF_DAY, 0);
        shift_end_time.set(Calendar.MINUTE, 0);

        edit_text_machine_type = findViewById(R.id.edit_text_machine_type);
        String machineType = "";
        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            machineType = getIntent().getExtras().getString("machine_type");
        }
        if (machineType != "") {
            edit_text_machine_type.setText(machineType);
        }

        edit_text_hours = findViewById(R.id.edit_text_hours);
        edit_text_hours.setText(Integer.toString(0));
        edit_text_hours.setEnabled(false);


        ShiftLogic shiftLogic = new ShiftLogic(this);
        shiftLogic.open();
        List<ShiftModel> shifts = shiftLogic.getFullList();
        List<String> shiftsNames = new LinkedList<>();
        shiftLogic.close();

        for (ShiftModel shift : shifts) {
            shiftsNames.add(shift.getType());
        }

        spinnerShifts = findViewById(R.id.spinner_shifts);
        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            ArrayAdapter<String> adapterShifts = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shiftsNames);
            adapterShifts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerShifts.setAdapter(adapterShifts);
        } else {
            ArrayList<String> shiftsNamesOnline = new ArrayList<>();
            try {
                shiftsNamesOnline = getShiftNames();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapterShifts = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shiftsNamesOnline);
            adapterShifts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerShifts.setAdapter(adapterShifts);
        }

        WorkerLogic workerLogic = new WorkerLogic(this);
        workerLogic.open();
        List<WorkerModel> workers = workerLogic.getFullList();
        List<String> workersNames = new LinkedList<>();
        workerLogic.close();

        for (WorkerModel worker : workers) {
            workersNames.add(worker.getName());
        }

        Spinner spinnerWorkers = findViewById(R.id.spinner_workers);
        if (LoginActivity.checkBoxOfflineMode.isChecked()) {
            ArrayAdapter<String> adapterWorkers = new ArrayAdapter(this, android.R.layout.simple_spinner_item, workersNames);
            adapterWorkers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerWorkers.setAdapter(adapterWorkers);
        } else {
            ArrayList<String> workerNamesOnline = new ArrayList<>();
            try {
                workerNamesOnline = getWorkerNames();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapterWorkers = new ArrayAdapter(this, android.R.layout.simple_spinner_item, workerNamesOnline);
            adapterWorkers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerWorkers.setAdapter(adapterWorkers);
        }

        button_shift_begin_time.setOnClickListener(
                v ->

                {
                    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            shift_begin_time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            shift_begin_time.set(Calendar.MINUTE, minute);

                            // Formatting
                            StringBuilder formattedHourOfDay = new StringBuilder();
                            StringBuilder formattedMinute = new StringBuilder();

                            // Hours formatting
                            if (hourOfDay / 10 == 0) {
                                formattedHourOfDay.append("0");
                                formattedHourOfDay.append(hourOfDay);
                            } else {
                                formattedHourOfDay.append(hourOfDay);
                            }

                            // Minutes formatting
                            if (minute / 10 == 0) {
                                formattedMinute.append("0");
                                formattedMinute.append(minute);
                            } else {
                                formattedMinute.append(minute);
                            }

                            button_shift_begin_time.setText(formattedHourOfDay + ":" + formattedMinute);

                            differenceBetweenShiftHours = calculateDifferenceBetweenShiftHours(shift_begin_time, shift_end_time);

                            edit_text_hours.setText(Integer.toString(differenceBetweenShiftHours));
                        }
                    };

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            timeSetListener, 0, 0, true);

                    timePickerDialog.show();
                }
        );

        button_shift_end_time.setOnClickListener(
                v ->

                {
                    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            shift_end_time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            shift_end_time.set(Calendar.MINUTE, minute);

                            // Formatting
                            StringBuilder formattedHourOfDay = new StringBuilder();
                            StringBuilder formattedMinute = new StringBuilder();

                            // Hours formatting
                            if (hourOfDay / 10 == 0) {
                                formattedHourOfDay.append("0");
                                formattedHourOfDay.append(hourOfDay);
                            } else {
                                formattedHourOfDay.append(hourOfDay);
                            }

                            // Minutes formatting
                            if (minute / 10 == 0) {
                                formattedMinute.append("0");
                                formattedMinute.append(minute);
                            } else {
                                formattedMinute.append(minute);
                            }

                            button_shift_end_time.setText(formattedHourOfDay + ":" + formattedMinute);

                            differenceBetweenShiftHours = calculateDifferenceBetweenShiftHours(shift_begin_time, shift_end_time);

                            edit_text_hours.setText(Integer.toString(differenceBetweenShiftHours));

                        }
                    };

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            timeSetListener, 0, 0, true);

                    timePickerDialog.show();
                }
        );

        button_create.setOnClickListener(v ->
        {
            if (LoginActivity.checkBoxOfflineMode.isChecked()) { // OFFLINE

                String machine = edit_text_machine_type.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String dateTimeBegin = dateFormat.format(shift_begin_time.getTime());
                String dateTimeEnd = dateFormat.format(shift_end_time.getTime());

                int shiftId = shifts.get(spinnerShifts.getSelectedItemPosition()).getId();
                String shiftName = shifts.get(spinnerShifts.getSelectedItemPosition()).getType();
                long shiftDate = shifts.get(spinnerShifts.getSelectedItemPosition()).getDate();


                MachineModel model = new MachineModel(machine, dateTimeBegin, dateTimeEnd, shiftId, shiftName, machineWorkers);
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

                String shiftId = "";
                String shift_name = spinnerShifts.getSelectedItem().toString();
                try {
/*
                    HashMap<String, String> workersOnline = getWorkers();
*/
                    HashMap<String, String> shiftsOnline = getShifts();

                    for (Map.Entry<String, String> entry : shiftsOnline.entrySet()) {
                        if (entry.getValue().equals(shift_name)) {
                            shiftId = entry.getKey();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!edit_text_machine_type.getText().toString().equals("") || !button_shift_begin_time.getText().toString().equals("")
                        || !button_shift_end_time.getText().toString().equals("") || !shiftId.equals("")
                        || !shift_name.equals("")) {
                    String finalShiftId = shiftId;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCreate, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                Toast.makeText(MachineActivity.this, "Успех!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (response.equals("failure")) {
                                Toast.makeText(MachineActivity.this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("machine_type", edit_text_machine_type.getText().toString());
                            data.put("shift_begin_time", button_shift_begin_time.getText().toString());
                            data.put("shift_end_time", button_shift_end_time.getText().toString());
                            data.put("shift_id", finalShiftId);
                            data.put("shift_name", shift_name);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                    this.finish();
                }
            }
        });

        button_cancel.setOnClickListener(
                v ->

                {
                    this.finish();
                }
        );

        button_add_worker.setOnClickListener(
                v ->
                {
                    if (LoginActivity.checkBoxOfflineMode.isChecked()) {
                        int workerId = workers.get(spinnerWorkers.getSelectedItemPosition()).getId();
                        String workerName = workers.get(spinnerWorkers.getSelectedItemPosition()).getName();
                        int hours = Integer.valueOf(edit_text_hours.getText().toString());
                        for (MachineWorkersModel machineWorker : machineWorkers) {
                            if (machineWorker.getWorkerId() == workerId) {
                                return;
                            }
                        }
                        machineWorkers.add(new MachineWorkersModel(id, workerId, workerName,
                                hours));
                        edit_text_hours.setText(Integer.toString(differenceBetweenShiftHours));
                        spinnerWorkers.setSelection(0);
                        fillTable(Arrays.asList("Имя работника", "Количество часов"), machineWorkers);
                    } else {
                        String workerId = "";
                        String worker_name = spinnerWorkers.getSelectedItem().toString();
                        String shiftId = "";
                        String shift_name = spinnerShifts.getSelectedItem().toString();
                        String hours = edit_text_hours.getText().toString();
                        try {
                            HashMap<String, String> workersOnline = getWorkers();
                            HashMap<String, String> shiftsOnline = getShifts();

                            for (Map.Entry<String, String> entry : workersOnline.entrySet()) {
                                if (entry.getValue().equals(worker_name)) {
                                    workerId = entry.getKey();
                                }
                            }
                            for (Map.Entry<String, String> entry : shiftsOnline.entrySet()) {
                                if (entry.getValue().equals(shift_name)) {
                                    shiftId = entry.getKey();
                                }
                            }
                            System.out.println(workerId);
                            System.out.println(shiftId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int k = 100;
                        if (!workerId.equals("") && !worker_name.equals("") || !shiftId.equals("")
                                || !shift_name.equals("") || !hours.equals("")) {
                            String finalWorkerId = workerId;
                            int finalK = k;
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCreate, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("success")) {
                                        Toast.makeText(MachineActivity.this, "Успех!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if (response.equals("failure")) {
                                        Toast.makeText(MachineActivity.this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("machine_id", String.valueOf(finalK));
                                    data.put("worker_id", finalWorkerId);
                                    data.put("worker_name", worker_name);
                                    data.put("boss_id", hours);
                                    return data;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
                            spinnerWorkers.setSelection(0);
                            HashMap<String, String> worker = new HashMap<>();
                            worker.put(worker_name, hours);
                            try {
                                fillTableFromJSON(Arrays.asList("Имя работника", "Количество часов"), worker);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        k++;
                    }
                }
        );

        button_delete_worker.setOnClickListener(
                v ->

                {
                    TextView textView = (TextView) selectedRow.getChildAt(2);
                    int index = Integer.valueOf(textView.getText().toString());
                    machineWorkers.remove(index);
                    fillTable(Arrays.asList("Имя работника", "Количество часов"), machineWorkers);
                }
        );

        fillTable(Arrays.asList("Имя работника", "Количество часов"), machineWorkers);
    }

    void fillTableFromJSON(List<String> titles, HashMap<String, String> worker) throws JSONException {
        TableLayout tableLayoutMachineWorkers = findViewById(R.id.tableLayoutMachineWorkers);

        tableLayoutMachineWorkers.removeAllViews();

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
        tableLayoutMachineWorkers.addView(tableRowTitles);

        for (Map.Entry<String, String> entry : worker.entrySet()) {
            TableRow tableRow = new TableRow(this);

            TextView textViewWorkerName = new TextView(this);
            textViewWorkerName.setHeight(100);
            textViewWorkerName.setTextSize(16);
            textViewWorkerName.setText(entry.getKey());
            textViewWorkerName.setTextColor(Color.WHITE);
            textViewWorkerName.setGravity(Gravity.CENTER);

            TextView textViewHours = new TextView(this);
            textViewHours.setHeight(100);
            textViewHours.setTextSize(16);
            textViewHours.setText(entry.getValue());
            textViewHours.setTextColor(Color.WHITE);
            textViewHours.setGravity(Gravity.CENTER);

            tableRow.addView(textViewWorkerName);
            tableRow.addView(textViewHours);

            tableRow.setBackgroundColor(Color.parseColor("#0A2647"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int j = 0; j < tableLayoutMachineWorkers.getChildCount(); j++) {
                    View view = tableLayoutMachineWorkers.getChildAt(j);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#0A2647"));
                    }
                }

                tableRow.setBackgroundColor(Color.parseColor("#144272"));
            });

            tableLayoutMachineWorkers.addView(tableRow);
        }

    }

    public HashMap<String, String> getShifts() throws JSONException {
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

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;

        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            result.put(jsonObject.getString("id"), jsonObject.getString("type"));
        }
        return result;
    }

    public HashMap<String, String> getWorkers() throws JSONException {
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

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;

        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            result.put(jsonObject.getString("id"), jsonObject.getString("name"));
        }
        return result;
    }

    public ArrayList<String> getShiftNames() throws JSONException {
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

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;

        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            result.add(jsonObject.getString("type"));
        }
        return result;
    }

    public ArrayList<String> getWorkerNames() throws JSONException {
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

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;

        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            result.add(jsonObject.getString("name"));
        }
        return result;
    }

    void fillTable(List<String> titles, List<MachineWorkersModel> machineWorkers) {

        TableLayout tableLayoutMachineWorkers = findViewById(R.id.tableLayoutMachineWorkers);

        tableLayoutMachineWorkers.removeAllViews();

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
        tableLayoutMachineWorkers.addView(tableRowTitles);

        int index = 0;
        for (MachineWorkersModel machineWorker : machineWorkers) {
            TableRow tableRow = new TableRow(this);

            WorkerLogic workerLogic = new WorkerLogic(this);

            TextView textViewName = new TextView(this);
            textViewName.setHeight(100);
            textViewName.setTextSize(16);
            textViewName.setText(workerLogic.getElement(machineWorker.getWorkerId()).getName());
            textViewName.setTextColor(Color.WHITE);
            textViewName.setGravity(Gravity.CENTER);

            TextView textViewHours = new TextView(this);
            textViewHours.setHeight(100);
            textViewHours.setTextSize(16);
            textViewHours.setText(String.valueOf(machineWorker.getHours()));
            textViewHours.setTextColor(Color.WHITE);
            textViewHours.setGravity(Gravity.CENTER);

            TextView textViewIndex = new TextView(this);
            textViewIndex.setVisibility(View.INVISIBLE);
            textViewIndex.setText(String.valueOf(index));

            tableRow.addView(textViewName);
            tableRow.addView(textViewHours);
            tableRow.addView(textViewIndex);

            tableRow.setBackgroundColor(Color.parseColor("#0A2647"));

            tableRow.setOnClickListener(v -> {

                selectedRow = tableRow;

                for (int i = 0; i < tableLayoutMachineWorkers.getChildCount(); i++) {
                    View view = tableLayoutMachineWorkers.getChildAt(i);
                    if (view instanceof TableRow) {
                        view.setBackgroundColor(Color.parseColor("#0A2647"));
                    }
                }

                tableRow.setBackgroundColor(Color.WHITE);
            });

            tableLayoutMachineWorkers.addView(tableRow);
            index++;
        }
    }

    int calculateDifferenceBetweenShiftHours(Calendar shift_begin_time, Calendar shift_end_time) {
        int beginHours = shift_begin_time.getTime().getHours() * 60 + shift_begin_time.getTime().getMinutes();
        int endHours = shift_end_time.getTime().getHours() * 60 + shift_end_time.getTime().getMinutes();
        int difference;
        if (endHours < beginHours) {
            difference = 24 - Math.abs(endHours - beginHours) / 60;
        } else {
            difference = Math.abs(endHours - beginHours) / 60;
        }
        return difference;
    }
}