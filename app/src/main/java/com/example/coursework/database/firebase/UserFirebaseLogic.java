package com.example.coursework.database.firebase;

import android.content.Context;

import com.example.coursework.database.logics.UserLogic;
import com.example.coursework.database.models.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserFirebaseLogic {

    private final String TABLE = "user";

    private DatabaseReference database;

    public UserFirebaseLogic(){
        database = FirebaseDatabase.getInstance().getReference(TABLE);
    }

    public void syncUsers(Context context) {
        UserLogic logic = new UserLogic(context);

        logic.open();
        List<UserModel> models = logic.getFullList();
        logic.close();

        database.removeValue();

        for (UserModel model: models) {
            database.push().setValue(model);
        }
    }
}
