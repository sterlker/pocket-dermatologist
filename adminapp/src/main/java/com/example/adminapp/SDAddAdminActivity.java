package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SDAddAdminActivity extends AppCompatActivity {

    EditText sdName, sdDescription, sdSymptoms, actionNeeded;
    Button btnAdd, btnClear;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdadd_admin);

        toolbar = findViewById(R.id.toolbar_sdadd_admin);
        setSupportActionBar(toolbar);

        sdName = findViewById(R.id.SDAdd_SDName);
        sdDescription = findViewById(R.id.SDAdd_SDDescription);
        sdSymptoms = findViewById(R.id.SDAdd_SDSymptoms);
        actionNeeded = findViewById(R.id.SDAdd_ActionNeeded);
        btnAdd = findViewById(R.id.btnAddSD);
        btnClear = findViewById(R.id.btnClearSDAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdNameGet = sdName.getText().toString();
                String sdDescriptionGet = sdDescription.getText().toString();
                String sdSymptomsGet = sdSymptoms.getText().toString();
                String actionNeededGet = actionNeeded.getText().toString();

                if(sdNameGet.isEmpty() || sdDescriptionGet.isEmpty() || sdSymptomsGet.isEmpty() || actionNeededGet.isEmpty()){
                    Toast.makeText(SDAddAdminActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Config.BASE_URL + "add_adminsdlist.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    Toast.makeText(SDAddAdminActivity.this, "Info Uploaded", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SDAddAdminActivity.this, SkinDiseaseTableActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SDAddAdminActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("skindisease_name", sdNameGet);
                        paramV.put("skindisease_description", sdDescriptionGet);
                        paramV.put("skindisease_symptoms", sdSymptomsGet);
                        paramV.put("action_needed", actionNeededGet);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
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
}
