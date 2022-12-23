package com.example.coursework.database.firebase;

import android.content.Context;

import com.example.coursework.database.logics.BossLogic;
import com.example.coursework.database.models.BossModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BossFirebaseLogic {

    private final String TABLE = "boss";

    private DatabaseReference database;

    public BossFirebaseLogic(){
        database = FirebaseDatabase.getInstance().getReference(TABLE);
    }

    public void syncBosses(Context context) {
        BossLogic logic = new BossLogic(context);

        logic.open();
        List<BossModel> models = logic.getFullList();
        logic.close();

        database.removeValue();

        for (BossModel model: models) {
            database.push().setValue(model);
        }
    }
}
