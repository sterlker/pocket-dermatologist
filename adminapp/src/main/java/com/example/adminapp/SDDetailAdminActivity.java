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

public class SDDetailAdminActivity extends AppCompatActivity {

    TextView skinDiseaseTitle;
    TextView skinDiseaseDescription;
    TextView skinDiseaseSymptoms;
    TextView actionNeeded, skinDiseaseIDSet;
    Button btnEdit, btnDelete;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sddetail_admin);

        String skinDiseaseName = getIntent().getStringExtra("skinDiseaseName");
        String skinDiseaseID = getIntent().getStringExtra("skinDiseaseID");

        Toolbar toolbar = findViewById(R.id.toolbar_sddetail_admin);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(skinDiseaseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        skinDiseaseIDSet = findViewById(R.id.SDAdmin_ID);
        skinDiseaseTitle = findViewById(R.id.SDAdmin_Title);
        skinDiseaseDescription = findViewById(R.id.SDAdmin_Description);
        skinDiseaseSymptoms = findViewById(R.id.SDAdmin_Symptoms);
        actionNeeded = findViewById(R.id.SDAdmin_ActionNeeded);
        btnEdit = findViewById(R.id.SDAdmin_Edit);
        btnDelete = findViewById(R.id.SDAdmin_Delete);

        skinDiseaseTitle.setText(skinDiseaseName);
        skinDiseaseIDSet.setText(skinDiseaseID);

        new FetchDetail().execute(skinDiseaseID);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SDDetailAdminActivity.this, SDEditAdminActivity.class);
                intent.putExtra("skindisease_id", skinDiseaseID);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteSkinDiseaseDetail().execute(skinDiseaseID);
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
        protected String doInBackground(String... params) {
            String skinDiseaseID = params[0];
            String result = "";

            try {
                URL url = new URL(Config.BASE_URL + "read_adminsddetail.php?skindisease_id=" + skinDiseaseID);
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
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("error")) {
                    Toast.makeText(SDDetailAdminActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject data = jsonArray.getJSONObject(0);

                    String description = data.getString("skindisease_description");
                    String symptoms = data.getString("skindisease_symptoms");
                    String action_needed = data.getString("action_needed");

                    skinDiseaseDescription.setText(description);
                    skinDiseaseSymptoms.setText(symptoms);
                    actionNeeded.setText(action_needed);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DeleteSkinDiseaseDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String skinDiseaseID = params[0];
            String result = "";

            try {
                URL url = new URL(Config.BASE_URL + "delete_adminsddetail.php?skindisease_id=" + skinDiseaseID);
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
                Toast.makeText(SDDetailAdminActivity.this, "Failed to Delete", Toast.LENGTH_LONG).show();
            }
        }
    }
}
