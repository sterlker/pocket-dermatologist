package com.example.adminapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextPassword, textInputEditTextEmail;
    Button buttonLogin;
    TextView textViewSignUp;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextEmail = findViewById(R.id.emailLogin);
        textInputEditTextPassword = findViewById(R.id.passwordLogin);
        buttonLogin = findViewById(R.id.btnLogin);
        textViewSignUp = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progressLogin);

        textViewSignUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password, email;
                password = String.valueOf(textInputEditTextPassword.getText());
                email = String.valueOf(textInputEditTextEmail.getText());

                if(!password.equals("") && !email.equals("")) {
                    if (email.equals("admin") && password.equals("admin123")) {
                        // Redirect to Admin Dashboard
                        Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "email";
                                field[1] = "password";
                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = email;
                                data[1] = password;
                                PutData putData = new PutData(Config.BASE_URL + "login.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        if(result != null) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(result);
                                                String message = jsonObject.getString("message");
                                                if (message.equals("Login Success")) {
                                                    int userId = jsonObject.getInt("user_id");
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    intent.putExtra("user_id", userId);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
                                        }

                                        Log.i("PutData", result);
                                    }
                                }
                                //End Write and Read data with URL
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
