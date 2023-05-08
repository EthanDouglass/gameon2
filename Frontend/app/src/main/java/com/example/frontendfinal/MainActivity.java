package com.example.frontendfinal;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frontendfinal.sketchIt.SketchItActivity;
import com.example.frontendfinal.sketchIt.SketchItGameActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        Button newUser = (Button) findViewById(R.id.buttonNewUser);
        Button returningUser = (Button) findViewById(R.id.buttonReturningUser);
        Button guestUser = (Button) findViewById(R.id.buttonGuestUser);
        Button testPage = (Button) findViewById(R.id.buttonTest);



        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewUserActivity.class);
                startActivity(i);
            }
        });

        returningUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ReturningUserActivity.class);
                startActivity(i);
            }
        });

        guestUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player currentPlayer = new Player("guest21", "guest21", "guest21");
                Intent i = new Intent(MainActivity.this, HostOrLocal.class);
                i.putExtra("CurrentPlayer", currentPlayer);
                startActivity(i);
            }
        });

        testPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SketchItGameActivity.class);
                startActivity(i);
            }
        });

    }



}