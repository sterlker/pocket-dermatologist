package com.example.fypcsp650;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
    int path;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shdetail);

        imageView_sh = findViewById(R.id.imageView_sh);
        classified_sh = findViewById(R.id.classified_sh);
        result_sh = findViewById(R.id.result_sh);

        Intent intent = getIntent();
        path = intent.getIntExtra("path", 0);
        userId = intent.getIntExtra("user_id", 0);

        Toolbar toolbar = findViewById(R.id.toolbar_shdetail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        @Override
        protected String doInBackground(String... params){
            String path = params[0];
            String result = "";

            try{
                // Construct URL with path and user_id
                URL url = new URL(Config.BASE_URL + "read_sddetail.php?path=" + path + "&user_id=" + userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
                // Parse JSON response
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                
                String classification = jsonObject.getString("classification");

                // Display retrieved data in UI
                result_sh.setText(classification);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}