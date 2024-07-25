package com.example.adminapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserDetailAdminActivity extends AppCompatActivity {

    TextView username, userIdentity, fullname, email, password;
    Button btnEdit, btnDelete;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        String user_name = getIntent().getStringExtra("username");
        String userIdString = getIntent().getStringExtra("user_id");

        Toolbar toolbar = findViewById(R.id.toolbar_userdetail_admin);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(user_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int userId = Integer.parseInt(userIdString);

        userIdentity = findViewById(R.id.UserAdmin_UserID);
        username = findViewById(R.id.UserAdmin_Username);
        fullname = findViewById(R.id.UserAdmin_Fullname);
        email = findViewById(R.id.UserAdmin_Email);
        password = findViewById(R.id.UserAdmin_Password);
        btnEdit = findViewById(R.id.UserAdmin_Edit);
        btnDelete = findViewById(R.id.UserAdmin_Delete);

        username.setText(user_name);
        userIdentity.setText(userIdString);

        new FetchDetail().execute(userIdString, user_name);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailAdminActivity.this, UserEditAdminActivity.class);
                intent.putExtra("user_id", userIdString);
                intent.putExtra("username", user_name);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteUserDetail().execute(userIdString);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private class FetchDetail extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params){
            String userId = params[0];
            String userName = params[1];
            String result = "";

            try{
                URL url = new URL(Config.BASE_URL + "read_adminuserdetail.php?user_id=" + userId + "&username=" + userName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String fullname_db = jsonObject.getString("fullname");
                String email_db = jsonObject.getString("email");
                String password_db = jsonObject.getString("pw");

                fullname.setText(fullname_db);
                email.setText(email_db);
                password.setText(password_db);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class DeleteUserDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userID = params[0];
            String result = "";

            try {
                URL url = new URL(Config.BASE_URL + "delete_adminuserdetail.php?user_id=" + userID);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(UserDetailAdminActivity.this, "Failed to Delete", Toast.LENGTH_LONG).show();
            }
        }
    }
}
