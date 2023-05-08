package com.example.frontendfinal;

import static com.example.frontendfinal.VolleyMethods.loginReturningUser;

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

import org.json.JSONException;
import org.json.JSONObject;

public class ReturningUserActivity extends AppCompatActivity {

    private EditText playerUsername;

    private EditText playerPassword;

    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_returning_user);

        Button buttonBack = (Button) findViewById(R.id.buttonBack);
        Button login = (Button) findViewById(R.id.buttonLogin);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReturningUserActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Retrieve input values
                playerUsername = (EditText)findViewById(R.id.PlayerUsername);
                playerPassword = (EditText) findViewById(R.id.PlayerPassword);
                String username = playerUsername.getText().toString();
                String password = playerPassword.getText().toString();

                // Retrieve login confirmation and display name
                loginReturningUser(ctx, username, password, new VolleyMethods.LoginResponseCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                        if (response == null) {
                            loadFragment(new invalidloginFragment());
                        }
                        else {
                            // The response in this case would be the displayName
                            CurrentPlayerActivity.currentPlayer = new Player(username, password, response.getString("displayName"));
                            CurrentPlayerActivity.currentPlayer.setPlayerID(response.getInt("playerid"));
                            CurrentPlayerActivity.currentPlayer.setLifetimePts(response.getInt("runningScore"));
                            Intent i = new Intent(ReturningUserActivity.this, HostOrLocal.class);
                            i.putExtra("CurrentPlayer", CurrentPlayerActivity.currentPlayer);
                            startActivity(i);
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
        fragmentTransaction.replace(R.id.returninguseractivityframe, fragment);
        fragmentTransaction.commit();
    }
}