package com.example.frontendfinal;


import static com.example.frontendfinal.VolleyMethods.createNewUser;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewUserActivity extends AppCompatActivity {

    private EditText player_username;
    private EditText player_password;
    private EditText player_display_name;

    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_user);

        Button buttonBack = (Button) findViewById(R.id.buttonBack);
        Button createAccount = (Button) findViewById(R.id.buttonCreateAccount);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewUserActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Retrieve input values
                player_username = (EditText)findViewById(R.id.PlayerUsername);
                player_password = (EditText) findViewById(R.id.PlayerPassword);
                player_display_name = (EditText) findViewById(R.id.PlayerDisplayName);
                String username = player_username.getText().toString();
                String password = player_password.getText().toString();
                String displayName = player_display_name.getText().toString();
                CurrentPlayerActivity.currentPlayer = new Player(username, password, displayName);

                 createNewUser(ctx, CurrentPlayerActivity.currentPlayer, new VolleyMethods.NewUserResponseCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                        String signupCheck = response.getString("SR");
                        int ID = response.getInt("playerid");
                        if (signupCheck.equals("success")) {
                            Player.setPlayerID(ID);
                            Intent i = new Intent(NewUserActivity.this, HostOrLocal.class);
                            i.putExtra("CurrentPlayer", CurrentPlayerActivity.currentPlayer);
                            startActivity(i);
                        }
                        else if (response.equals("Username already exists")) {
                            loadFragment(new usernametakenFragment());
                        }
                        else if (response.equals("display name already exists")) {
                            loadFragment(new displaynametakenFragment());
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
        fragmentTransaction.replace(R.id.newuseractivityframe, fragment);
        fragmentTransaction.commit();
    }
}