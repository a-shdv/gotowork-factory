package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coursework.R;
import com.example.coursework.database.firebase.UserFirebaseLogic;
import com.example.coursework.database.logics.UserLogic;
import com.example.coursework.database.models.UserModel;

import java.util.List;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursework.R;
import com.example.coursework.database.logics.UserLogic;
import com.example.coursework.database.models.UserModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    Button buttonRegister;
    Button buttonBack;
    EditText editTextLogin;
    EditText editTextPassword;
    UserLogic logic;
    UserFirebaseLogic firebaseLogic;

    String URL = "http://192.168.31.7:8000/gotowork/authorization/register.php";
    String login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = findViewById(R.id.button_register);
        buttonBack = findViewById(R.id.button_back);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);

        logic = new UserLogic(this);
        firebaseLogic = new UserFirebaseLogic();

        buttonRegister.setOnClickListener(
                v -> {
                    // OFFLINE
                    if (LoginActivity.checkBoxOfflineMode.isChecked()) {
                        UserModel model = new UserModel(editTextLogin.getText().toString(), editTextPassword.getText().toString());

                        logic.open();

                        List<UserModel> users = logic.getFullList();

                        for (UserModel user : users) {
                            if (user.getLogin().equals(model.getLogin())) {
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
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else { // ONLINE
                        login = editTextLogin.getText().toString().trim();
                        password = editTextPassword.getText().toString().trim();
                        if (!login.equals("") || !password.equals("")) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("success")) {
                                        Toast.makeText(RegisterActivity.this, "Успех!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if (response.equals("failure")) {
                                        Toast.makeText(RegisterActivity.this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
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
                        }
                    }
                }
        );

        buttonBack.setOnClickListener(v -> {
            finish();
        });
    }
}