package com.example.coursework.database.firebase;

import android.content.Context;

import com.example.coursework.database.logics.WorkerLogic;
import com.example.coursework.database.models.WorkerModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WorkerFirebaseLogic {
    private final String TABLE = "worker";

    private DatabaseReference database;

    public WorkerFirebaseLogic(){
        database = FirebaseDatabase.getInstance().getReference(TABLE);
    }

    public void syncWorkers(Context context) {
        WorkerLogic logic = new WorkerLogic(context);

        logic.open();
        List<WorkerModel> models = logic.getFullList();
        logic.close();

        database.removeValue();

        for (WorkerModel model: models) {
            database.push().setValue(model);
        }
    }

}
