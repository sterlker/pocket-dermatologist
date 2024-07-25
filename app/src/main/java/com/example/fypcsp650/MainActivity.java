package com.example.fypcsp650;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView btnScan;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    int userId;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnScan = findViewById(R.id.btnScan);

        userId = getIntent().getIntExtra("user_id", 0);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        userId = getIntent().getIntExtra("user_id", 0);
        int id = item.getItemId();

        if(id == R.id.Scan) {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
            finish();
        } else if (id == R.id.main){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
            finish();
        } else if (id == R.id.SD_List){
            Intent intent = new Intent(MainActivity.this, SDListActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
            finish();
        } else if (id == R.id.SH_List){
            Intent intent = new Intent (MainActivity.this, SHListActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
            finish();
        } else if (id == R.id.Logout){
            logoutMenu(MainActivity.this);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logoutMenu(MainActivity mainActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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