package com.example.frontendfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PingFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Random;

import okhttp3.Response;


public class HostActivity extends AppCompatActivity implements WebSocketListener {

    TextView displayName;

    TextView hostCode;

    TextView playerCount;

    Button buttonContinue;
    Button buttonEndGame;

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_host);
        String gameCode = (String) getIntent().getSerializableExtra("gameCode");

        buttonContinue = (Button) findViewById(R.id.buttonContinue);
        buttonEndGame = (Button) findViewById(R.id.buttonEndLobby);
        displayName = (TextView) findViewById(R.id.displayName);
        playerCount = (TextView) findViewById(R.id.gamePlayerCount);
        hostCode = (TextView) findViewById(R.id.gameCode);

        displayName.setText(Player.getDisplayName());
        hostCode.setText(gameCode);

        WebSocketManager.getInstance().connect("ws://coms-309-008.class.las.iastate.edu:8080/connectPlayers/" + Player.getPlayerid() + "/" + gameCode);
        WebSocketManager.getInstance().setWebSocketListener(this);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HostActivity.this, GameSelectActivity.class);
                startActivity(i);
            }
        });

        buttonEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject closeLobby = new JSONObject();
                try {
                    closeLobby.put("request","Kick Out");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                WebSocketManager.getInstance().sendMessage(closeLobby.toString());
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void updatePlayerCount(int c) {
        count = c + count;
        playerCount.setText(count + "/8 Players");
    }

    @Override
    public void onWebSocketMessage(String message) {
        JSONObject jsonMessage;
        String response = null;
        try {
            jsonMessage = new JSONObject(message);
            response = jsonMessage.getString("response");
        } catch (JSONException e) {
            System.out.println("Invalid message was received in the Host Activity, threw JSON exception");
        }

        if (response != null) {
            if (response.contains("has joined the game session")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updatePlayerCount(1);
                    }
                });
            } else if (response.contains("disconnected")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updatePlayerCount(-1);
                    }
                });
            } else if (response.equals("close")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebSocketManager.getInstance().disconnect();
                        WebSocketManager.getInstance().removeWebSocketListener();
                        Intent i = new Intent(HostActivity.this, HostOrLocal.class);
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