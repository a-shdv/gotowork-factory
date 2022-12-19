package com.example.coursework.database.firebase;

import android.content.Context;

import com.example.coursework.database.logics.MachineLogic;
import com.example.coursework.database.models.MachineModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MachineFirebaseLogic {
    private final String TABLE = "receipt";

    private DatabaseReference database;

    public MachineFirebaseLogic(){
        database = FirebaseDatabase.getInstance().getReference(TABLE);
    }

    public void syncReceipt(Context context) {
        MachineLogic logic = new MachineLogic(context);

        logic.open();
        List<MachineModel> models = logic.getFullList();
        logic.close();

        database.removeValue();

        for (MachineModel model: models) {
            database.push().setValue(model);
        }
    }
}
