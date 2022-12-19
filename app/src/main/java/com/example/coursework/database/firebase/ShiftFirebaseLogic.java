package com.example.coursework.database.firebase;

import android.content.Context;

import com.example.coursework.database.logics.ShiftLogic;
import com.example.coursework.database.models.ShiftModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShiftFirebaseLogic {
    private final String TABLE = "shift";

    private DatabaseReference database;

    public ShiftFirebaseLogic(){
        database = FirebaseDatabase.getInstance().getReference(TABLE);
    }

    public void syncShifts(Context context) {
        ShiftLogic logic = new ShiftLogic(context);

        logic.open();
        List<ShiftModel> models = logic.getFullList();
        logic.close();

        database.removeValue();

        for (ShiftModel model: models) {
            database.push().setValue(model);
        }
    }
}
