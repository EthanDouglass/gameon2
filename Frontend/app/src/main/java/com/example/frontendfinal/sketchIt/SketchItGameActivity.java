package com.example.frontendfinal.sketchIt;

import static com.example.frontendfinal.sketchIt.SketchItGuessActivity.getGuess;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import java.util.Base64;
import android.view.Window;
import android.view.WindowManager;

import com.example.frontendfinal.FactOrCapActivity;
import com.example.frontendfinal.GameSelectActivity;
import com.example.frontendfinal.Player;
import com.example.frontendfinal.R;
import com.example.frontendfinal.WaitingScreenActivity;
import com.example.frontendfinal.WebSocketListener;
import com.example.frontendfinal.WebSocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HexFormat;
import java.util.Objects;

import okio.ByteString;

/**
 * This is the main activity for the SketchIt game. The majority of the game logic will be done here
 * Here, we will send and receive all of the info needed throughout the game
 * This class will call other activities to do things, NOT fragments for simplicity
 *   Therefore, all activities will have to pass the needed items through intents back to this class
 *
 *
 */
public class SketchItGameActivity extends AppCompatActivity implements WebSocketListener {

    private String currentGamePrompt = ""; //This is the prompt that the specific player drew for (Doesn't change after Step 3)
    private String currentDrawingPrompt = "This Prompt Changes"; //This is the current prompt for the drawing you're guessing
    private static byte[] clientDrawing; //This is the drawing the specific player drew (Doesn't change after Step 3)
    private byte[] currentDrawing; //This is the drawing the backend sent back that players guess on
    private String currentDrawingPlayerId = ""; // This is the player id of the current drawing being guessed. Will change
    private ArrayList<String> prompts = new ArrayList<String>();
    private Handler mHandler = new Handler();
    byte[] bytesDrawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sketch_it_game);

        WebSocketManager.getInstance().setWebSocketListener(this);

        Bundle b = getIntent().getExtras();
        String start = b.getString("start");

        if (start.equals("true")) {
            JSONObject getPrompt = new JSONObject();
            try {
                getPrompt.put("request", "Get Prompt");
            } catch (JSONException e) {
                System.out.println("JsonException sending getting new prompt");
            }
            WebSocketManager.getInstance().sendMessage(getPrompt.toString());
        } else if (start.equals("false")) {
            if (b.getByteArray("drawing") != null) {
                //String test = Base64.getEncoder().encodeToString(SketchItActivity.getDrawing());
                //System.out.println(test);
                clientDrawing =  SketchItActivity.getDrawing();
                //System.out.println(clientDrawing);
                currentGamePrompt = b.getString("prompt");
                JSONObject playerSketch = new JSONObject();
                try {
                    playerSketch.put("request", "Player Sketch");
                    playerSketch.put("prompt", currentGamePrompt);
                    playerSketch.put("drawing",clientDrawing);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                WebSocketManager.getInstance().sendMessage(playerSketch.toString());
            } else if (b.getString("guess") != null) {
                String userGuess = b.getString("guess");
                currentDrawingPlayerId = b.getString("playerid");
                JSONObject playerGuess = new JSONObject();
                try {
                    playerGuess.put("request", "Player Guess");
                    playerGuess.put("guess", userGuess);
                    playerGuess.put("playerid", currentDrawingPlayerId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                WebSocketManager.getInstance().sendMessage(playerGuess.toString());
                loadFragment(new SketchItGuessWaitFragment());
            } else if (b.getString("guesslist") != null) {
                String guess = b.getString("guesslist");
                currentDrawingPrompt = b.getString("prompt");
                if (guess.equals(currentDrawingPrompt)) {
                    Player.addCurrentGamePts(100);
                    JSONObject getPrompt = new JSONObject();
                    try {
                        getPrompt.put("request", "Next Drawing");
                    } catch (JSONException e) {
                        System.out.println("JsonException sending getting new prompt");
                    }
                    WebSocketManager.getInstance().sendMessage(getPrompt.toString());
                } else {
                    JSONObject getPrompt = new JSONObject();
                    try {
                        getPrompt.put("request", "Next Drawing");
                    } catch (JSONException e) {
                        System.out.println("JsonException sending getting new prompt");
                    }
                    WebSocketManager.getInstance().sendMessage(getPrompt.toString());
                }
        }
    }}

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.sketchItFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonMessage;
                String response = null;
                try {
                    jsonMessage = new JSONObject(message);
                    response = jsonMessage.getString("response");
                } catch (JSONException e) {
                    System.out.println("Invalid message was received in the SketchIt Game Activity, threw JSON exception");
                }

                if (response != null) {
                    if (response.equals("New Prompt")) {
                        JSONObject newPrompt;
                        try {
                            newPrompt = new JSONObject(message);
                            currentGamePrompt = newPrompt.getString("prompt");
                        } catch (JSONException e) {
                            System.out.println("Invalid message was received in the SketchIt Game Activity when receiving a new prompt, threw JSON exception");
                        }
                        loadFragment(new SketchIt1Fragment(currentGamePrompt)); // They will sit in waiting screen for 5 seconds waiting to draw
                        CountDownTimer c = new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long l) {
                                // Do nothing
                            }
                            @Override
                            public void onFinish() {
                                Intent i = new Intent(SketchItGameActivity.this, SketchItActivity.class);
                                i.putExtra("prompt", currentGamePrompt);
                                startActivity(i);
                            }
                        }.start();
                    } else if (response.equals("Guess Drawing")) {
                        JSONObject guessDrawing;
                        try {
                            guessDrawing = new JSONObject(message);
                            //TODO - \/\/ This is 100% the issue - sending to and from backend \/\/
                            //String drawingHexString = guessDrawing.getString("drawing");
                            //byte[] bytesDrawing = ;
                            //System.out.println(bytesDrawing);
                            //currentDrawing = ;
                            currentDrawingPrompt = guessDrawing.getString("prompt");
                            currentDrawingPlayerId = guessDrawing.getString("playerid");
                        } catch (JSONException e) {
                            System.out.println("Invalid message was received in the SketchIt Game Activity when receiving a drawing to guess, threw JSON exception");
                        }
                        if ((currentDrawingPlayerId.equals("" + Player.getPlayerid()))) {
                            Intent j = new Intent(SketchItGameActivity.this, SketchItGuessActivity.class);
                            j.putExtra("drawing", currentDrawing);
                            j.putExtra("playerid", currentDrawingPlayerId);
                            startActivity(j);
                        } else {
                            loadFragment(new SketchItHostFragment());
                        }
                    } else if (response.equals("Guess List")) {
                        JSONObject guessList;
                        try {
                            guessList = new JSONObject(message);
                            int numGuesses = guessList.getInt("Num guesses");
                            String test = guessList.getString("drawing"); //TODO same issue as above
                            currentDrawingPrompt = guessList.getString("prompt");
                            currentDrawingPlayerId = guessList.getString("playerid");
                            if (numGuesses > 0) {
                                for (int i = 0; i < numGuesses; i++) {
                                    prompts.set(i - 1, guessList.getString("guess " + i));
                                }
                            }
                            prompts.add(currentDrawingPrompt);
                        } catch (JSONException e) {
                            System.out.println("Invalid message was received in the SketchIt Game Activity when receiving the list of guesses, threw JSON exception");
                        }

                        if ((currentDrawingPlayerId.equals("" + Player.getPlayerid()))) {
                            Intent p = new Intent(SketchItGameActivity.this, SketchItGuessListActivity.class);
                            p.putExtra("guesses", prompts);
                            p.putExtra("drawing", currentDrawing);
                            p.putExtra("prompt", currentDrawingPrompt);
                            startActivity(p);
                        } else {
                            loadFragment(new SketchItHostFragment());
                        }
                    } else if (response.equals("SketchIt Close")) {
                        if (Player.getPlayerHost()) {
                            Intent i = new Intent(SketchItGameActivity.this, GameSelectActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SketchItGameActivity.this, WaitingScreenActivity.class);
                            startActivity(i);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().removeWebSocketListener();
    }

    public static byte[] getDrawing(){
        return clientDrawing;
    }
}
