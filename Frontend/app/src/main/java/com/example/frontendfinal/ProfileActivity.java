package com.example.frontendfinal;

import static com.example.frontendfinal.CurrentPlayerActivity.currentPlayer;
import static com.example.frontendfinal.VolleyMethods.joinGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This activity is the profile activity, where the player can see their lifeTime pts and username,
 * and change their displayName and characterIcon
 *
 * Lifetime Wins?
 *
 */


public class ProfileActivity extends AppCompatActivity {

    private Button red;
    private Button orange;
    private Button yellow;
    private Button green;
    private Button teal;
    private Button lightBlue;
    private Button blue;
    private Button purple;
    private Button cancel;
    private Button apply;

    private ImageView avatarImg;
    private EditText displayNameChange;
    private int iconNum = 0;
    private String newDisplayName = "error";
    private TextView username;
    private TextView lifetimePts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //THIS REMOVES TOP BAR AND ACTION BAR
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //REMOVES ALL TOP BARS ^^
        setContentView(R.layout.activity_profile);


        red = findViewById(R.id.buttonRed);
        orange = findViewById(R.id.buttonOrange);
        yellow = findViewById(R.id.buttonYellow);
        green = findViewById(R.id.buttonGreen);
        teal = findViewById(R.id.buttonTeal);
        lightBlue = findViewById(R.id.buttonLightBlue);
        blue = findViewById(R.id.buttonBlue);
        purple = findViewById(R.id.buttonPurple);
        avatarImg = findViewById(R.id.imageIcon);
        displayNameChange = findViewById(R.id.displayNameChangeOption);
        cancel = findViewById(R.id.buttonCancel);
        apply = findViewById(R.id.buttonApply);

        username = (TextView) findViewById(R.id.usernameChange);
        username.setText(Player.getUsername());

        lifetimePts = (TextView) findViewById(R.id.lifetimePtsChange);
        String tempString =  Integer.toString(Player.getLifetimePts());
        lifetimePts.setText(tempString);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newDisplayName != "error") {
                    newDisplayName = displayNameChange.getText().toString();
                    Player.setDisplayName(newDisplayName);
                }
                if(iconNum != 0){
                    Player.setCharacterIcon(iconNum);
                }
                finish();
                //TODO - Setup Volley methods for changing the displayName and characterIcon
            }
        });



        //AVATAR COLOR CHANGING BUTTONS
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImg.setImageResource(R.drawable.character_logo1);
                iconNum = 1;
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImg.setImageResource(R.drawable.character_logo2);
                iconNum = 2;
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImg.setImageResource(R.drawable.character_logo3);
                iconNum = 3;
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImg.setImageResource(R.drawable.character_logo4);
                iconNum = 4;
            }
        });

        teal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImg.setImageResource(R.drawable.character_logo5);
                iconNum = 5;
            }
        });

        lightBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImg.setImageResource(R.drawable.character_logo6);
                iconNum = 6;
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImg.setImageResource(R.drawable.character_logo7);
                iconNum = 7;
            }
        });

        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImg.setImageResource(R.drawable.character_logo8);
                iconNum = 8;
            }
        });


    }


}