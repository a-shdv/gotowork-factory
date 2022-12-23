package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.Report;
import com.example.coursework.database.firebase.ShiftFirebaseLogic;
import com.example.coursework.database.firebase.WorkerFirebaseLogic;
import com.example.coursework.database.firebase.MachineFirebaseLogic;
import com.example.coursework.database.firebase.MachineWorkersFirebaseLogic;
import com.example.coursework.database.firebase.UserFirebaseLogic;
import com.example.coursework.database.logics.MachineLogic;
import com.example.coursework.database.logics.UserLogic;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    Button button_workers;
    Button button_shifts;
    Button button_machines;
    Button button_report;
    Button button_exit;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserLogic userLogic = new UserLogic(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLogic.open();

        context = this;

        button_machines = findViewById(R.id.button_machines);
        button_workers = findViewById(R.id.button_workers);
        button_shifts = findViewById(R.id.button_shifts);
        button_report = findViewById(R.id.button_report);
        button_exit = findViewById(R.id.button_exit);

        button_machines.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MainActivity.this, MachinesActivity.class);
                    startActivity(intent);
                }
        );

        button_workers.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MainActivity.this, WorkersActivity.class);
                    startActivity(intent);
                }
        );

        button_shifts.setOnClickListener(
                v -> {
                    int userId = getIntent().getExtras().getInt("userId");
                    Intent intent = new Intent(MainActivity.this, ShiftsActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
        );

        button_report.setOnClickListener(
                v -> {
                    Report report = new Report();
                    MachineLogic shiftLogic = new MachineLogic(this);

                    try {
                        report.generatePdf(shiftLogic.getFullList());
                        Toast.makeText(MainActivity.this, "Отчет успешно сформирован!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        button_exit.setOnClickListener(
                v -> {
                    finish();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UserFirebaseLogic userFirebaseLogic = new UserFirebaseLogic();
        userFirebaseLogic.syncUsers(this);

        ShiftFirebaseLogic shiftFirebaseLogic = new ShiftFirebaseLogic();
        shiftFirebaseLogic.syncShifts(this);

        WorkerFirebaseLogic workerFirebaseLogic = new WorkerFirebaseLogic();
        workerFirebaseLogic.syncWorkers(this);

        MachineFirebaseLogic machineFirebaseLogic = new MachineFirebaseLogic();
        machineFirebaseLogic.syncMachines(this);

        MachineWorkersFirebaseLogic machineWorkersFirebaseLogic = new MachineWorkersFirebaseLogic();
        machineWorkersFirebaseLogic.syncMachineWorkers(this);

    }

}