package com.example.frontendfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.frontendfinal.sketchIt.SketchItGameActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class WaitingScreenActivity extends AppCompatActivity implements WebSocketListener{

    /**
     * TODO -
     * Add displayName in the corner
     * Add the joined code into the center
     * Add the menu fragment
     *
     */

    private TextView t;
    private TextView p;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_waiting_screen);

        t = (TextView) (findViewById(R.id.textView2));
        t.setText(Player.getGameCode() + "");
        p = (TextView) (findViewById(R.id.displayName));
        p.setText(Player.getDisplayName());

        WebSocketManager.getInstance().connect("ws://coms-309-008.class.las.iastate.edu:8080/connectPlayers/" + Player.getPlayerid() + "/" + Player.getGameCode());
        WebSocketManager.getInstance().setWebSocketListener(this);
    }

    @Override
    public void onWebSocketMessage(String message) {
        JSONObject jsonMessage;
        String response = null;
        try {
            jsonMessage = new JSONObject(message);
            response = jsonMessage.getString("response");
        } catch (JSONException e) {
            System.out.println("Invalid message was received in the Waiting Screen Activity, threw JSON exception");
        }

        if (response != null) {
            if (response.equals("Start Fact or Cap")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(WaitingScreenActivity.this, FactOrCapActivity.class);
                        startActivity(i);
                    }
                });
            } else if (response.equals("End Lobby")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject gamePointObj = new JSONObject();
                        try {
                            gamePointObj.put("request","Game Score");
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
                        Intent i = new Intent(WaitingScreenActivity.this, LeaderboardActivity.class);
                        i.putExtra("message", message);
                        startActivity(i);
                    }
                });
            } else if (response.equals("close")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebSocketManager.getInstance().disconnect();
                        WebSocketManager.getInstance().removeWebSocketListener();
                        Intent i = new Intent(WaitingScreenActivity.this, HostOrLocal.class);
                        startActivity(i);
                    }
                });
            } else if (response.equals("Added Scores")) {
                WebSocketManager.getInstance().sendMessage("Leaderboard Info");
            } else if (response.equals("Start SketchIt")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(WaitingScreenActivity.this, SketchItGameActivity.class);
                        i.putExtra("start", "true");
                        startActivity(i);
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