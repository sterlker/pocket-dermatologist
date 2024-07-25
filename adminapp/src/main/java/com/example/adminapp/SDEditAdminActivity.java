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

public class SDEditAdminActivity extends AppCompatActivity {

    EditText sdName, sdDescription, sdSymptoms, actionNeeded;
    Button btnEdit, btnClear;
    Toolbar toolbar;
    String skinDiseaseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdedit_admin);

        toolbar = findViewById(R.id.toolbar_sdedit_admin);
        setSupportActionBar(toolbar);

        sdName = findViewById(R.id.SDEdit_SDName);
        sdDescription = findViewById(R.id.SDEdit_SDDescription);
        sdSymptoms = findViewById(R.id.SDEdit_SDSymptoms);
        actionNeeded = findViewById(R.id.SDEdit_ActionNeeded);
        btnEdit = findViewById(R.id.btnEditSD);
        btnClear = findViewById(R.id.btnClearSDEdit);

        skinDiseaseID = getIntent().getStringExtra("skindisease_id");
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdIDStr = skinDiseaseID;
                String sdNameStr = sdName.getText().toString().trim();
                String sdDescriptionStr = sdDescription.getText().toString().trim();
                String sdSymptomsStr = sdSymptoms.getText().toString().trim();
                String actionNeededStr = actionNeeded.getText().toString().trim();

                if (sdIDStr.isEmpty() || sdNameStr.isEmpty() || sdDescriptionStr.isEmpty() || sdSymptomsStr.isEmpty() || actionNeededStr.isEmpty()) {
                    Toast.makeText(SDEditAdminActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    new EditSDTask().execute(sdIDStr, sdNameStr, sdDescriptionStr, sdSymptomsStr, actionNeededStr);
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sdName.setText("");
                sdDescription.setText("");
                sdSymptoms.setText("");
                actionNeeded.setText("");
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
        Intent intent = new Intent(SDEditAdminActivity.this, SkinDiseaseTableActivity.class);
        startActivity(intent);
        finish();
    }


    private class EditSDTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String skindisease_id = params[0];
                String skindisease_name = params[1];
                String skindisease_description = params[2];
                String skindisease_symptoms = params[3];
                String action_needed = params[4];

                URL url = new URL(Config.BASE_URL + "edit_adminsddetail.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("skindisease_id", "UTF-8") + "=" + URLEncoder.encode(skindisease_id, "UTF-8") + "&"
                        + URLEncoder.encode("skindisease_name", "UTF-8") + "=" + URLEncoder.encode(skindisease_name, "UTF-8") + "&"
                        + URLEncoder.encode("skindisease_description", "UTF-8") + "=" + URLEncoder.encode(skindisease_description, "UTF-8") + "&"
                        + URLEncoder.encode("skindisease_symptoms", "UTF-8") + "=" + URLEncoder.encode(skindisease_symptoms, "UTF-8") + "&"
                        + URLEncoder.encode("action_needed", "UTF-8") + "=" + URLEncoder.encode(action_needed, "UTF-8");
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
            Toast.makeText(SDEditAdminActivity.this, result, Toast.LENGTH_LONG).show();
            if (result.equals("success")) {
                Intent intent = new Intent(SDEditAdminActivity.this, SkinDiseaseTableActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

}