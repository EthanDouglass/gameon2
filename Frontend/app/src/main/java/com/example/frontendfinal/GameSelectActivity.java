package com.example.frontendfinal;

import static com.example.frontendfinal.VolleyMethods.joinGame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.frontendfinal.sketchIt.SketchItGameActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class GameSelectActivity extends AppCompatActivity implements WebSocketListener {

    /**
     * This activity is the game select for the host
     *
     * The host will either try to start the game or end the game here
     */

    TextView displayName;
    Button FactOrCap;
    Button SketchIt;
    Button game3;
    Button game4;
    Button startGame;
    Button endGame;
    int gameSelected = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);

        displayName = (TextView) findViewById(R.id.displayName);
        FactOrCap = (Button) findViewById(R.id.buttonGame1);
        SketchIt = (Button) findViewById(R.id.buttonGame2);
        game3 = (Button) findViewById(R.id.buttonGame3);
        game4 = (Button) findViewById(R.id.buttonGame4);
        startGame = (Button) findViewById(R.id.buttonStartGame);
        endGame = (Button) findViewById(R.id.buttonEndGame);

        displayName.setText(Player.getDisplayName());

        WebSocketManager.getInstance().setWebSocketListener(this);

        FactOrCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameSelected = 1;
            }
        });

        SketchIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameSelected = 2;
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject leaderboardObj = new JSONObject();
                try {
                    leaderboardObj.put("request","End Lobby");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                WebSocketManager.getInstance().sendMessage(leaderboardObj.toString());
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameSelected == 0) {
                    //Need to make fragment to tell user to pick a game
                    loadFragment(new errorFragment());
                } else if (gameSelected == 1) {
                    JSONObject startfactorcap = new JSONObject();
                    try {
                        startfactorcap.put("request","Start Fact or Cap");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    WebSocketManager.getInstance().sendMessage(startfactorcap.toString());
                } else if (gameSelected == 2) {
                    JSONObject startSketchIt = new JSONObject();
                    try {
                        startSketchIt.put("request","Start SketchIt");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    WebSocketManager.getInstance().sendMessage(startSketchIt.toString());
                } else {
                    loadFragment(new errorFragment());
                }
            }
        });

    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.joinactivityframe, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onWebSocketMessage(String message) {
        JSONObject jsonMessage;
        String response = null;
        try {
            jsonMessage = new JSONObject(message);
            response = jsonMessage.getString("response");
        } catch (JSONException e) {
            System.out.println("Invalid message was received in the Game Select Activity, threw JSON exception");
        }

        if (response != null) {
            if (response.equals("Start Fact or Cap")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(GameSelectActivity.this, FactOrCapActivity.class);
                        startActivity(i);
                    }
                });
            } else if (response.equals("Start SketchIt")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(GameSelectActivity.this, SketchItGameActivity.class);
                        i.putExtra("start", "true");
                        startActivity(i);
                    }
                });
            } else if (response.equals("End Lobby")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject gamePointObj = new JSONObject();
                        try {
                            gamePointObj.put("request", "Game Score");
                            gamePointObj.put("Game Points", Player.getCurrentGamePts() + "");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        WebSocketManager.getInstance().sendMessage(gamePointObj.toString());
                    }
                });
            } else if (response.equals("Leaderboard Results")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(GameSelectActivity.this, LeaderboardActivity.class);
                        i.putExtra("message", message);
                        startActivity(i);
                    }
                });
            } else if (response.equals("Added Scores")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject gamePointObj = new JSONObject();
                        try {
                            gamePointObj.put("request", "Leaderboard Info");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        WebSocketManager.getInstance().sendMessage(gamePointObj.toString());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().removeWebSocketListener();
    }
}