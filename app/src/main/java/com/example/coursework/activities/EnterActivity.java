package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.coursework.R;
import com.example.coursework.database.logics.UserLogic;
import com.example.coursework.database.models.UserModel;

import java.util.List;

public class EnterActivity extends AppCompatActivity {

    Button button_to_register_activity;
    Button button_enter;
    EditText editTextLogin;
    EditText editTextPassword;

    UserLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        button_to_register_activity = findViewById(R.id.button_to_register_activity);
        button_enter = findViewById(R.id.button_enter);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);

        logic = new UserLogic(this);

        button_to_register_activity.setOnClickListener(
                v -> {
                    Intent intent = new Intent(EnterActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
        );

        button_enter.setOnClickListener(
                v -> {
                    UserModel model = new UserModel(editTextLogin.getText().toString(), editTextPassword.getText().toString());

                    logic.open();

                    List<UserModel> users = logic.getFullList();
                    for(UserModel user : users){
                        if(user.getLogin().equals(model.getLogin()) && user.getPassword().equals(model.getPassword())){
                            logic.close();

                            this.finish();
                            Intent intent = new Intent(EnterActivity.this, MainActivity.class);
                            intent.putExtra("userId", user.getId());
                            startActivity(intent);

                            return;
                        }
                    }

                    logic.close();

                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterActivity.this);
                    builder.setMessage("Пароль введен неверно или такой логин не зарегистрирован");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "ОК",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                }
        );
    }
}