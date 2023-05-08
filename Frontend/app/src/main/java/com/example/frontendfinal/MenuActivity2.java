package com.example.frontendfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

/**
 * This is the real menu activity, which is basically a test for a navigation bar I will put into other activites
 * Users won't actually ever see this activity, but is rather a test of the popout bar
 */

public class MenuActivity2 extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    public Button testButton1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu2);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        testButton1 = findViewById(R.id.testButton);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        testButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

    }

    public boolean onNavigationItemSelected (@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.nav_account) {
            Intent i = new Intent(MenuActivity2.this, ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(MenuActivity2.this, MainActivity.class);
            startActivity(i);
        }
        return true;
    }

}