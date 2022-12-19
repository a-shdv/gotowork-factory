package com.example.coursework.database.firebase;

import android.content.Context;

import com.example.coursework.database.logics.MachineWorkersLogic;
import com.example.coursework.database.models.MachineWorkersModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MachineWorkersFirebaseLogic {
    private final String TABLE = "receiptMedicines";

    private DatabaseReference database;

    public MachineWorkersFirebaseLogic(){
        database = FirebaseDatabase.getInstance().getReference(TABLE);
    }

    public void syncReceiptMedicines(Context context) {
        MachineWorkersLogic logic = new MachineWorkersLogic(context);

        logic.open();
        List<MachineWorkersModel> models = logic.getFullList();
        logic.close();

        database.removeValue();

        for (MachineWorkersModel model: models) {
            database.push().setValue(model);
        }
    }
}
