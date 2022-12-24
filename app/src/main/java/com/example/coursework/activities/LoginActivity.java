package com.example.coursework.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button button_to_register_activity;
    Button button_enter;
    EditText editTextLogin;
    EditText editTextPassword;

    URL url;
    String address = "http://192.168.31.7:8000/gotowork/boss/login.php";

    InputStream inputStream;
    String line = "";
    String result = "";

    String bossStrId, login, password;
    public static CheckBox checkBoxOfflineMode;

    BossLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bossStrId = login = password = "";

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
                                intent.putExtra("bossIntId", boss.getId());
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
                        try {
                            bossStrId = getBossStrId();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        login = editTextLogin.getText().toString().trim();
                        password = editTextPassword.getText().toString().trim();
                        if (!login.equals("") && !password.equals("")) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, address, response -> {
                                if (response.equals("success")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("bossStrId", bossStrId);
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
                                    data.put("bossStrId", bossStrId);
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

    public String getBossStrId() throws JSONException {
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        try {
            url = new URL("http://192.168.31.7:8000/gotowork/boss/get.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            inputStream = new BufferedInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read inputStream content into a String
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;

        String result = "";

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.getString("login").equals(editTextLogin.getText().toString())) {
                result = jsonObject.getString("id");
            }
        }
        return result;

    }
}