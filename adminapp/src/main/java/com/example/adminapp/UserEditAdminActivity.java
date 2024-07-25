package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UserEditAdminActivity extends AppCompatActivity {

    EditText fullname, username, password, email;
    Button btnEdit, btnClear;
    Toolbar toolbar;
    String userId, user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_admin);

        toolbar = findViewById(R.id.toolbar_useredit_admin);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fullname = findViewById(R.id.UserEdit_fullname);
        username = findViewById(R.id.UserEdit_username);
        password = findViewById(R.id.UserEdit_password);
        email = findViewById(R.id.UserEdit_email);
        btnEdit = findViewById(R.id.btnEditUser);
        btnClear = findViewById(R.id.btnClearUserEdit);

        userId = getIntent().getStringExtra("user_id");
        user_name = getIntent().getStringExtra("username");

        if (userId == null) {
            Toast.makeText(this, "Error: userId is null", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnameStr = fullname.getText().toString().trim();
                String usernameStr = username.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();
                String emailStr = email.getText().toString().trim();

                if (fullnameStr.isEmpty() || usernameStr.isEmpty() || passwordStr.isEmpty() || emailStr.isEmpty()) {
                    Toast.makeText(UserEditAdminActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    new EditUserTask().execute(userId, fullnameStr, usernameStr, passwordStr, emailStr);
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname.setText("");
                username.setText("");
                password.setText("");
                email.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserEditAdminActivity.this, UserTableActivity.class);
        startActivity(intent);
        finish();
    }

    private class EditUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String user_id = params[0];
                String fullname = params[1];
                String username = params[2];
                String password = params[3];
                String email = params[4];

                if (user_id == null || fullname == null || username == null || password == null || email == null) {
                    return "Error: One or more fields are null.";
                }

                URL url = new URL(Config.BASE_URL + "edit_adminuserdetail.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("fullname", "UTF-8") + "=" + URLEncoder.encode(fullname, "UTF-8") + "&"
                        + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int responseCode = httpURLConnection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                    bufferedReader.close();
                } else {
                    result.append("Failed to update data");
                }
                httpURLConnection.disconnect();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(UserEditAdminActivity.this, result, Toast.LENGTH_LONG).show();
            if (result.equals("success")) {
                Intent intent = new Intent(UserEditAdminActivity.this, UserTableActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
