package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursework.R;
import com.example.coursework.database.logics.BossLogic;
import com.example.coursework.database.models.BossModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button button_to_register_activity;
    Button button_enter;
    EditText editTextLogin;
    EditText editTextPassword;

    String URL = "http://192.168.31.7:8000/gotowork/boss/login.php";

    String login, password;
    static CheckBox checkBoxOfflineMode;

    BossLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = password = "";

        button_to_register_activity = findViewById(R.id.button_to_register_activity);
        button_enter = findViewById(R.id.button_enter);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);
        checkBoxOfflineMode = findViewById(R.id.checkBoxOfflineMode);

        logic = new BossLogic(this);

        checkBoxOfflineMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxOfflineMode.isChecked())
                    Toast.makeText(LoginActivity.this, "Теперь Вы находитесь в оффлайн режиме.", Toast.LENGTH_SHORT).show();
            }
        });

        button_enter.setOnClickListener(
                v -> {
                    // OFFLINE
                    if (checkBoxOfflineMode.isChecked()) {
                        BossModel model = new BossModel(editTextLogin.getText().toString(), editTextPassword.getText().toString());

                        logic.open();

                        List<BossModel> bosses = logic.getFullList();
                        for (BossModel boss : bosses) {
                            if (boss.getLogin().equals(model.getLogin()) && boss.getPassword().equals(model.getPassword())) {
                                logic.close();

                                this.finish();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("bossId", boss.getId());
                                startActivity(intent);

                                return;
                            }
                        }

                        logic.close();

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Пароль введен неверно или такой логин не зарегистрирован!");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "ОК",
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else { // ONLINE
                        login = editTextLogin.getText().toString().trim();
                        password = editTextPassword.getText().toString().trim();
                        if (!login.equals("") && !password.equals("")) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                                if (response.equals("success")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (response.equals("failure")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Пароль введен неверно или такой логин не зарегистрирован!");
                                    builder.setCancelable(true);

                                    builder.setPositiveButton(
                                            "ОК",
                                            (dialog, id) -> dialog.cancel());

                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }, error -> Toast.makeText(LoginActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("login", login);
                                    data.put("password", password);
                                    return data;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("Поля не могут быть пустыми!");
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "ОК",
                                    (dialog, id) -> dialog.cancel());

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }
        );

        button_to_register_activity.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
        );
    }
}