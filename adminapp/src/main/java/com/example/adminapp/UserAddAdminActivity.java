package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UserAddAdminActivity extends AppCompatActivity {

    EditText fullname, username, email, password;
    Button btnAdd, btnClear;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_admin);

        toolbar = findViewById(R.id.toolbar_useradd_admin);
        setSupportActionBar(toolbar);

        fullname = findViewById(R.id.UserAdd_fullname);
        username = findViewById(R.id.UserAdd_username);
        email = findViewById(R.id.UserAdd_email);
        password = findViewById(R.id.UserAdd_password);
        btnAdd = findViewById(R.id.btnAddUser);
        btnClear = findViewById(R.id.btnClearUserAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnameget = fullname.getText().toString();
                String usernameget = username.getText().toString();
                String passwordget = password.getText().toString();
                String emailget = email.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Config.BASE_URL + "add_adminuserlist.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    Toast.makeText(UserAddAdminActivity.this, "Info Uploaded", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UserAddAdminActivity.this, UserTableActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(UserAddAdminActivity.this, "Failed to Upload Info", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("fullname", fullnameget);
                        paramV.put("username", usernameget);
                        paramV.put("password", passwordget);
                        paramV.put("email", emailget);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}