package com.example.adminapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SHDetailActivity extends AppCompatActivity {
    ImageView imageView_sh;
    TextView classified_sh;
    TextView result_sh;
    Button btnDelete_sh;
    int path;
    int userId;
    String scan_id, classification;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shdetail);

        imageView_sh = findViewById(R.id.imageView_sh);
        classified_sh = findViewById(R.id.classified_sh);
        result_sh = findViewById(R.id.result_sh);
        btnDelete_sh = findViewById(R.id.btnDelete_sh);

        Intent intent = getIntent();
        // Retrieve the path as a String
        String path_to_image = intent.getStringExtra("path_to_image");

        // Use Glide to load the image from the path
        Glide.with(this)
                .load(path_to_image)
                .into(imageView_sh);

        userId = intent.getIntExtra("user_id", 0);
        scan_id = intent.getStringExtra("scan_id");
        classification = intent.getStringExtra("classification");

        Toolbar toolbar = findViewById(R.id.toolbar_shdetail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        classified_sh.setText(classification);

        btnDelete_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteScanHistoryDetail().execute(scan_id);
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
        intent.putExtra("user_id", userId);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private class DeleteScanHistoryDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String scan_id = params[0];
            String result = "";

            try {
                URL url = new URL(Config.BASE_URL + "delete_shdetail.php?scan_id=" + scan_id);
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
                Toast.makeText(SHDetailActivity.this, "Failed to Delete", Toast.LENGTH_LONG).show();
            }
        }
    }
}