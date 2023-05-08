package com.example.frontendfinal;

import static java.lang.String.valueOf;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LeaderboardActivity extends AppCompatActivity {

    /**
     * This activity shows the leaderboard for the game session at the end of the game.
     *
     * TODO -
     * Make a method to get the leaderboard info from the server
     * Connect the leaderboard stats to the actual screen
     *
     * Make a fragment that shows everyone's score at the bottom? The top 3 stay on the top half of the screen,
     * and the bottom half is a scrollable screen for all the placements
     */

    private ArrayAdapter<String> adapter;

    private String[] names = new String[8];
    private int[] points = new int[8];
    private ArrayList<String> listItems = new ArrayList<String>();
    private ListView listView;
    private Button back;
    private int numPlayers;
    private int[] gamesWon = new int[8]; //TODO - Same as lifetimePts
    private int[] charIcon = new int[8]; //TODO - Same as lifetimePts, I need the character icon for each player
    private String[] lifeScore = new String[8];
    TextView First;
    TextView Second;
    TextView Third;
    TextView FirstPts;
    TextView SecondPts;
    TextView ThirdPts;
    private String firstPlaceName;
    private String secondPlaceName;
    private String thirdPlaceName;
    private String firstPlacePts = "";
    private String secondPlacePts = "";
    private String thirdPlacePts = "";
    ImageView firstPlaceImage;
    ImageView secondPlaceImage;
    ImageView thirdPlaceImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_leaderboard);
        String leaderboardMessage = (String) getIntent().getSerializableExtra("message");
        back = findViewById(R.id.button3);
        First = (TextView) findViewById(R.id.textView1stPlace);
        Second = findViewById(R.id.textView2ndPlace);
        Third = findViewById(R.id.textView3rdPlace);
        FirstPts = findViewById(R.id.textView1stPts);
        SecondPts = findViewById(R.id.textView2ndPts);
        ThirdPts = findViewById(R.id.textView3rdPts);
        listView = findViewById(R.id.leaderboardList);
        firstPlaceImage = findViewById(R.id.imageView3);
        secondPlaceImage = findViewById(R.id.imageView2);
        thirdPlaceImage = findViewById(R.id.imageView4);

        JSONObject leaderboardResults;
        try {
            leaderboardResults = new JSONObject(leaderboardMessage);
            numPlayers = Integer.parseInt(leaderboardResults.getString("Num Players"));
            for (int i = 1; i <= numPlayers; i++) {
                String playerName = leaderboardResults.getString("player " + i);
                int gameScore = Integer.parseInt(leaderboardResults.getString("Lobby Score " + i));
                String lifetimeScore = leaderboardResults.getString("Lifetime Score " + i);
                int previousWins = Integer.parseInt(leaderboardResults.getString("Previous Lobby Wins " + i));
                int iconId = Integer.parseInt(leaderboardResults.getString("Icon " + i));
                names[i - 1] = playerName; // Saves name of player
                points[i - 1] = gameScore; // Saves current game points of player
                lifeScore[i - 1] = lifetimeScore; // Saves life time score of player
                gamesWon[i - 1] = previousWins; // Saves Previous Number of wins for player
                charIcon[i - 1] = iconId;
            }
        } catch (JSONException e) {
            System.out.println("JsonException for Leaderboard Results");
        }
        //Orders the points and names together
        bubbleSort(points, names, lifeScore, gamesWon, charIcon);

        //First 3 names are on the podium, so put them on the podium
        System.out.println("Players: " + numPlayers);
        System.out.println("Names: " + names.length);
        System.out.println("char icon - " + charIcon[0]);


        if (numPlayers == 1){
            System.out.println(names[0]);
            First.setText(names[0]);
            FirstPts.setText(String.valueOf(points[0]));
            int icon = charIcon[0];
            firstPlaceImage.setImageResource(getCharIcon(icon));
        }
        else if(numPlayers == 2){
            First.setText(names[0]);
            Second.setText(names[1]);
            FirstPts.setText(String.valueOf(points[0]));
            SecondPts.setText(String.valueOf(points[1]));
            int icon = getCharIcon(charIcon[0]);
            firstPlaceImage.setImageResource(icon);
            int icon2 = getCharIcon(charIcon[1]);
            firstPlaceImage.setImageResource(icon2);
        }
        else{
            First.setText(names[0]);
            Second.setText(names[1]);
            Third.setText(names[2]);
            FirstPts.setText(String.valueOf(points[0]));
            SecondPts.setText(String.valueOf(points[1]));
            ThirdPts.setText(String.valueOf(points[2]));
            int icon = getCharIcon(charIcon[0]);
            firstPlaceImage.setImageResource(icon);
            int icon2 = getCharIcon(charIcon[1]);
            firstPlaceImage.setImageResource(icon2);
            int icon3 = getCharIcon(charIcon[2]);
            firstPlaceImage.setImageResource(icon3);
        }

        //Puts the names and points into the listview
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, listItems);
        listView.setAdapter(adapter);
        for(int j = 3; j < numPlayers; j++){
            String temp = names[j] + "  :   " + points[j];
            adapter.add(temp);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String item = (String) adapter.getItemAtPosition(position);
                int playerNum = 0;
                // Figure out which name was clicked
                for (int i = 0; i < numPlayers; i++) {
                    if (names[i].equals(item)) {
                        playerNum = i;
                    }
                }
                Intent intent = new Intent(LeaderboardActivity.this, LeaderboardPlayerActivity.class);
                intent.putExtra("displayPlayer", names[playerNum]);
                intent.putExtra("lifetimePts", lifeScore[playerNum]);
                intent.putExtra("gamesWon", gamesWon[playerNum]);
                intent.putExtra("charIcon", charIcon[playerNum]);
                startActivity(intent);
            }
        });

        //Textview clickers for moving to the profile page
        First.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaderboardActivity.this, LeaderboardPlayerActivity.class);
                intent.putExtra("displayPlayer", names[0]);
                intent.putExtra("lifetimePts", lifeScore[0]);
                intent.putExtra("gamesWon", gamesWon[0]);
                intent.putExtra("charIcon", charIcon[0]);
                //based on item add info to intent
                startActivity(intent);
            }
        });

        Second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaderboardActivity.this, LeaderboardPlayerActivity.class);
                intent.putExtra("displayPlayer", names[1]);
                intent.putExtra("lifetimePts", lifeScore[1]);
                intent.putExtra("gamesWon", gamesWon[1]);
                intent.putExtra("charIcon", charIcon[1]);
                //based on item add info to intent
                startActivity(intent);
            }
        });

        Third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaderboardActivity.this, LeaderboardPlayerActivity.class);
                intent.putExtra("displayPlayer", names[2]);
                intent.putExtra("lifetimePts", lifeScore[2]);
                intent.putExtra("gamesWon", gamesWon[2]);
                intent.putExtra("charIcon", charIcon[2]);
                //based on item add info to intent
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(LeaderboardActivity.this, HostOrLocal.class);
                    startActivity(intent);
            }
        });
    }

    public int getCharIcon(int iconNum){
        System.out.println(iconNum);
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


    public void bubbleSort(int[] array, String[] names, String[] lifeScore, int[] gamesWon, int[] charIcon) {
        boolean swapped = true;
        int j = 0;
        int tmp; //array
        String tmp2; //names
        String tmp3; //lifeScore
        int tmp4; //gamesWon
        int tmp5; //charIcon
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < array.length - j; i++) {
                if (array[i] > array[i + 1]) {
                    tmp = array[i];
                    tmp2 = names[i];
                    tmp3 = lifeScore[i];
                    tmp4 = gamesWon[i];
                    tmp5 = charIcon[i];
                    array[i] = array[i + 1];
                    names[i] = names[i+1];
                    lifeScore[i] = lifeScore[i+1];
                    gamesWon[i] = gamesWon[i+1];
                    charIcon[i] = charIcon[i+1];
                    array[i + 1] = tmp;
                    names[i+1] = tmp2;
                    lifeScore[i+1] = tmp3;
                    gamesWon[i+1] = tmp4;
                    charIcon[i+1] = tmp5;
                    swapped = true;
                }
            }
        }
    }
}