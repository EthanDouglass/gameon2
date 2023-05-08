package com.example.frontendfinal;

import static com.example.frontendfinal.VolleyMethods.createHostGame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

//READ - THOUGHTS
//So I made 2 activities for when each button (host and join) is pressed. My thoughts about what
//Each one should do is written here. I have nothing for the "Host" branch, so focus on "Join"
//The first test thing we should have done is to get the display name in the top corner of the "Join"
//screen. I've already put the box there and everything, you just have to connect with your mock server and
//have it change to whatever you initially set it to. The StaticsVariables class is for the URLS and such
//And the VolleyMethods are for the methods for Volley, which I think we have to copy and paste into each
//Activity depending on the need. That's everything new I believe

public class HostOrLocal extends AppCompatActivity {

    private Context ctx = this;

    private ImageButton menu;

    private DrawerLayout drawerLayout;

    TextView displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //THIS REMOVES TOP BAR AND ACTION BAR
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //REMOVES ALL TOP BARS ^^
        setContentView(R.layout.activity_host_or_local);

        Button hostUser = (Button) findViewById(R.id.buttonHost);
        Button joinUser = (Button) findViewById(R.id.buttonJoin);
        menu =  findViewById(R.id.menuButton);
        drawerLayout = findViewById(R.id.my_drawer_layout);

        displayName = (TextView) findViewById(R.id.displayName);
        displayName.setText(Player.getDisplayName());

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this::onNavigationItemSelected);


        //When a player clicks "host", we'll push them to the next page and send a request to the
        //Database to start a game server. Once we get the go that the server has been successfully set up,
        //We'll request the join code and send them to the host page, which will display the join code,
        //The num of players currently in lobby, and the game select screen with the option to play or end
        //the current game session. There will also be the menu fragment we need to setup for the menu
        hostUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.setPlayerHost();
                createHostGame(ctx, new VolleyMethods.CreateGameResponseCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Intent i = new Intent(HostOrLocal.this, HostActivity.class);
                        i.putExtra("gameCode", response);
                        startActivity(i);
                    }
                    @Override
                    public void onError() {
                        loadFragment(new errorFragment());
                    }
                });
            }
        });


        //When the player hits join, we'll prompt them for a code to join and send that up to the server.
        //If that matches a server, they'll be joined into that game and will be sent to a loading screen.
        //The loading screen won't have much, just the option to change their display name and character logo.
        //Maybe it'll have current stats for the game?
        joinUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HostOrLocal.this, JoinActivity.class);
                startActivity(i);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    public boolean onNavigationItemSelected (@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.nav_account) {
            Intent i = new Intent(HostOrLocal.this, ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            CurrentPlayerActivity.currentPlayer = null;
            Player.setCharacterIcon(0);
            Player.setDisplayName("");
            Player.setUsername("");
            Player.setPassword("");
            Player.setLifetimePts(0);
            Player.setGameCode(0);
            Player.setPlayerID(0);
            Player.resetCurrentGamePts();
            Intent i = new Intent(HostOrLocal.this, MainActivity.class);
            startActivity(i);
        }
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.hostorlocalframe, fragment);
        fragmentTransaction.commit();
    }


}