package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.example.coursework.R;
import com.example.coursework.database.firebase.UserFirebaseLogic;
import com.example.coursework.database.logics.UserLogic;
import com.example.coursework.database.models.UserModel;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    Button buttonRegister;
    EditText editTextLogin;
    EditText editTextPassword;
    UserLogic logic;
    UserFirebaseLogic firebaseLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = findViewById(R.id.button_register);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);

        logic = new UserLogic(this);
        firebaseLogic = new UserFirebaseLogic();

        buttonRegister.setOnClickListener(
                v -> {
                    UserModel model = new UserModel(editTextLogin.getText().toString(), editTextPassword.getText().toString());

                    logic.open();

                    List<UserModel> users = logic.getFullList();

                    for (UserModel user: users) {
                        if(user.getLogin().equals(model.getLogin())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Такой логин уже зарегистрирован");
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "ОК",
                                    (dialog, id) -> dialog.cancel());

                            AlertDialog alert = builder.create();
                            alert.show();
                            return;
                        }
                    }

                    logic.insert(model);
                    logic.close();

                    this.finish();
                    Intent intent = new Intent(RegisterActivity.this, EnterActivity.class);
                    startActivity(intent);
                }
        );
    }
}