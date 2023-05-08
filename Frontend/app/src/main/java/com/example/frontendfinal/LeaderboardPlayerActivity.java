package com.example.frontendfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is the activity that is opened when a player clicks another player's info in the leaderboard activity
 * This should display that player's stats, including displayName, avatar, lifetimePts, and previous game history
 *
 * We need to get the info from backend first, and then when they click a player, we'll send every player's info here
 * and display that player's info to the screen
 */

public class LeaderboardPlayerActivity extends AppCompatActivity {

    private Button back;
    private TextView lifetimePtsChange;
    private TextView gamesPlayedChange;
    private TextView displayName;
    private ImageView charIcon;
    private String displayedPlayerName;
    private String playerPts;
    private int gamesWon;
    private int charIcon2;
    private int icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_leaderboard_player);

        back = findViewById(R.id.buttonBack);
        lifetimePtsChange = findViewById(R.id.textViewLifetimePtsChange);
        gamesPlayedChange = findViewById(R.id.textViewGamesChange);
        displayName = findViewById(R.id.textViewDisplayName);
        charIcon = findViewById(R.id.imageIcon);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            displayedPlayerName = (String) getIntent().getStringExtra("displayPlayer");
            System.out.println(displayedPlayerName);
            playerPts = (String) getIntent().getStringExtra("lifetimePts");
            System.out.println(playerPts);
            gamesWon = (int) getIntent().getIntExtra("gamesWon", 0);
            System.out.println(gamesWon);
            charIcon2 = (int) getIntent().getIntExtra("charIcon", 0);
            System.out.println(charIcon2);
        }

        icon = getCharIcon(charIcon2);
        System.out.println("1");
        displayName.setText(displayedPlayerName);
        System.out.println("2");
        lifetimePtsChange.setText(playerPts.substring(0, 6));
        System.out.println("3");
        gamesPlayedChange.setText(String.valueOf(gamesWon));
        System.out.println("4");
        charIcon.setImageResource(icon);
        System.out.println("5");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public int getCharIcon(int iconNum){
        int icon = getResources().getIdentifier("character_logo3.jpg", "drawable", getPackageName());
        if(iconNum == 1){
            icon = getResources().getIdentifier("character_logo1.jpg", "drawable", getPackageName());
        }
        if(iconNum == 2){
            icon = getResources().getIdentifier("character_logo2.jpg", "drawable", getPackageName());
        }
        if(iconNum == 3){
            icon = getResources().getIdentifier("character_logo3.jpg", "drawable", getPackageName());
        }
        if(iconNum == 4){
            icon = getResources().getIdentifier("character_logo4.jpg", "drawable", getPackageName());
        }
        if(iconNum == 5){
            icon = getResources().getIdentifier("character_logo5.jpg", "drawable", getPackageName());
        }
        if(iconNum == 6){
            icon = getResources().getIdentifier("character_logo6.jpg", "drawable", getPackageName());
        }
        if(iconNum == 7){
            icon = getResources().getIdentifier("character_logo7.jpg", "drawable", getPackageName());
        }
        if(iconNum == 8){
            icon = getResources().getIdentifier("character_logo8.jpg", "drawable", getPackageName());
        }
        return icon;
    }
}