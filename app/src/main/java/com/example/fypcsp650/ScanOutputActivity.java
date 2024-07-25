package com.example.fypcsp650;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScanOutputActivity extends AppCompatActivity {
    String userId;

    // Inside ScanOutputActivity.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_output);

        // Retrieve the image and classification result from intent extras
        Bitmap image = getIntent().getParcelableExtra("image");
        String classification = getIntent().getStringExtra("classification");
        userId = String.valueOf(getIntent().getIntExtra("user_id", 0));

        // Display the image and classification result as needed
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

        TextView resultTextView = findViewById(R.id.result);
        resultTextView.setText(classification);

        Toolbar toolbar = findViewById(R.id.toolbar_scanoutput);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Button for uploading
        Button btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Config.BASE_URL + "upload_info.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("success")) {
                                    Toast.makeText(ScanOutputActivity.this, "Info Uploaded", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ScanOutputActivity.this, "Failed to Upload Info: " + response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("image", base64Image);
                        paramV.put("classification", classification);
                        paramV.put("user_id", userId);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
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
        super.onBackPressed();
        Intent intent = new Intent(ScanOutputActivity.this, MainActivity.class);
        intent.putExtra("user_id", Integer.parseInt(userId));
        startActivity(intent);
        finish();
    }
}
