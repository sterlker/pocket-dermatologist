package com.example.adminapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

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

public class SDDetailActivity extends AppCompatActivity {

    TextView skinDiseaseTitle;
    TextView skinDiseaseDescription;
    TextView skinDiseaseSymptoms;
    TextView actionNeeded;
    int userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sddetail);

        String skinDiseaseName = getIntent().getStringExtra("skinDiseaseName");

        Toolbar toolbar = findViewById(R.id.toolbar_sddetail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(skinDiseaseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = getIntent().getIntExtra("user_id", 0);

        skinDiseaseTitle = findViewById(R.id.SD_Title);
        skinDiseaseDescription = findViewById(R.id.SD_Description);
        skinDiseaseSymptoms = findViewById(R.id.SD_Symptoms);
        actionNeeded = findViewById(R.id.SD_ActionNeeded);

        skinDiseaseTitle.setText(skinDiseaseName);

        new FetchDetail().execute(skinDiseaseName);
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
        intent.putExtra("user_id", userId);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private class FetchDetail extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params){
            String skinDiseaseName = params[0];
            String result = "";

            try{
                URL url = new URL(Config.BASE_URL + "read_sddetail.php?skinDiseaseName=" + skinDiseaseName);
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

                String description = jsonObject.getString("skindisease_description");
                String symptoms = jsonObject.getString("skindisease_symptoms");
                String action_needed = jsonObject.getString("action_needed");

                skinDiseaseDescription.setText(description);
                skinDiseaseSymptoms.setText(symptoms);
                actionNeeded.setText(action_needed);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}