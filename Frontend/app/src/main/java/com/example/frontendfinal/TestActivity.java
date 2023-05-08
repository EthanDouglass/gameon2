package com.example.frontendfinal;

import static com.example.frontendfinal.StaticsVariables.URL_CURRENT_PTS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class TestActivity extends AppCompatActivity {
    /**
     * This class is purely for testing things - we can link a button to it for texting everything out
     *
     */
    Button getCurrentPts;
    Context ctx = this;
    TextView currentPts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        getCurrentPts = (Button) findViewById(R.id.buttonCurrentPts);
        currentPts = (TextView) findViewById(R.id.textCurrentPts);


        getCurrentPts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPtsTest = VolleyMethods.getCurrentPts(ctx);
                currentPts.setText(currentPtsTest);
            }
        });


    }


}