package com.example.frontendfinal;

import static com.example.frontendfinal.VolleyMethods.joinGame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class JoinActivity extends AppCompatActivity {

    TextView displayName;
    EditText JoinCode;
    Button buttonGo;
    String attemptedCode = "error";
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_join);


        buttonGo = (Button) findViewById(R.id.buttonGo);
        displayName = (TextView) findViewById(R.id.displayName);
        JoinCode = (EditText) findViewById(R.id.JoinCode);

        //Now let's get the displayName part setup so we can have it everywhere
        displayName.setText(Player.getDisplayName());

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptedCode = JoinCode.getText().toString();
                joinGame(ctx, attemptedCode, new VolleyMethods.JoinGameResponseCallback() {
                    @Override
                    public void onSuccess(String response) {
                        if (response.equals("success")) {
                            int poo = Integer.parseInt(attemptedCode);
                            Player.setGameCode(poo);
                            Intent i = new Intent(JoinActivity.this, WaitingScreenActivity.class);
                            startActivity(i);
                        }
                        else if (response.equals("Game does not exist")) {
                            loadFragment(new invalidgamecodeFragment());
                        }
                    }
                    @Override
                    public void onError() {
                        loadFragment(new errorFragment());
                    }
                });
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.joinactivityframe, fragment);
        fragmentTransaction.commit();
    }
}