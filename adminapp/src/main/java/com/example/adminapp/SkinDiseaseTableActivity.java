package com.example.adminapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SkinDiseaseTableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView listView;
    List<SkinDisease> list;
    SkinDiseaseAdapter skinDiseaseAdapter;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button SDAdminAdd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_disease_table);

        toolbar = findViewById(R.id.toolbar_sdadminlist);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_sdadminlist);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view_sdadminlist);
        navigationView.setNavigationItemSelectedListener(this);

        listView = findViewById(R.id.lv_sdadminlist);

        list = new ArrayList<>();
        skinDiseaseAdapter = new SkinDiseaseAdapter(this, list);
        listView.setAdapter(skinDiseaseAdapter);

        SDAdminAdd = findViewById(R.id.SDAdmin_Add);

        SDAdminAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SkinDiseaseTableActivity.this, SDAddAdminActivity.class);
                startActivity(intent);
            }
        });

        // Call AsyncTask to fetch data from the server
        new FetchData().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get selected skin disease name
                String selectedSkinDisease = list.get(position).getTitle_sd();
                String selectedSkinDiseaseID = list.get(position).getExpand_sd();

                Intent intent = new Intent(SkinDiseaseTableActivity.this, SDDetailAdminActivity.class);
                intent.putExtra("skinDiseaseID", selectedSkinDiseaseID);
                intent.putExtra("skinDiseaseName", selectedSkinDisease);
                startActivity(intent);
            }
        });
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {

        String url = Config.BASE_URL + "read_adminsdlist.php";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String data = stringBuilder.toString();
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String skinDiseaseId = jsonObject.getString("skindisease_id");
                    String skinDiseaseName = jsonObject.getString("skindisease_name");
                    list.add(new SkinDisease(skinDiseaseName, skinDiseaseId));
                }
                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Notify the adapter that data has changed
            skinDiseaseAdapter.notifyDataSetChanged();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.main) {
            Intent intent = new Intent(SkinDiseaseTableActivity.this, AdminMainActivity.class);
            startActivity(intent);
            finish();
        }  else if (id == R.id.SD_List){
            Intent intent = new Intent(SkinDiseaseTableActivity.this, SkinDiseaseTableActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.User_List){
            Intent intent = new Intent(SkinDiseaseTableActivity.this, UserTableActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.Logout){
            logoutMenu(SkinDiseaseTableActivity.this);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logoutMenu(SkinDiseaseTableActivity skinDiseaseTableActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SkinDiseaseTableActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
