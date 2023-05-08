package com.example.frontendfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.JsonReader;
import android.view.Window;
import android.view.WindowManager;

import org.java_websocket.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class FactOrCapActivity extends AppCompatActivity implements WebSocketListener {


    /**
     * This activity is the main activity for the FactOrCap game
     * This will basically be a blank page, in which the different fragments will be places over
     * to show the game info.
     * The backend of this activity (This page here) will just run the actual game, with the loops for the questions and timers
     *
     */

    int questionCount = 1;
    String question;
    String answer;
    List<Integer> questionIDList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fact_or_cap);

        WebSocketManager.getInstance().setWebSocketListener(this);

        getNewQuestion();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.factorcapframe, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onWebSocketMessage(String message) {
        System.out.println("Question count = " + questionCount);

        JSONObject jsonMessage;
        String response = null;
        try {
            jsonMessage = new JSONObject(message);
            response = jsonMessage.getString("response");
        } catch (JSONException e) {
            System.out.println("Invalid message was received in the Fact or Cap Activity, threw JSON exception");
        }

        if (response != null) {
        if (response.equals("question") && (questionCount <= 10)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // extract the question and answer
                    JSONObject QandAJson;
                    try {
                        QandAJson = new JSONObject(message);
                        question = QandAJson.getString("questiontext");
                        answer = QandAJson.getString("answertext");
                    } catch (JSONException e) {
                        System.out.println("Invalid question and answer in the Fact or Cap Activity, threw JSON exception");
                    }

                    // Show questionReady screen
                    loadFragment(new QuestionReadyFragment());
                    new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            // Show question to user
                            QuestionScreenFragment q = new QuestionScreenFragment(question, answer);
                            loadFragment(q);

                            new CountDownTimer(8000, 1000) {
                                @Override
                                public void onTick(long l) {
                                }

                                @Override
                                public void onFinish() {
                                    //Check if player was correct or incorrect -> award points
                                    if (q.correctCheck()) {
                                        Player.addCurrentGamePts(q.pointsEarned());
                                    }
                                    getNewQuestion();
                                }
                            }.start();
                        }
                    }.start();
                    questionCount += 1;
                }
            });
        } else if (response.equals("Leaderboard")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(FactOrCapActivity.this, LeaderboardActivity.class);
                    startActivity(i);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Player.getPlayerHost()) {
                        Intent i = new Intent(FactOrCapActivity.this, GameSelectActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(FactOrCapActivity.this, WaitingScreenActivity.class);
                        startActivity(i);
                    }
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

    public void getNewQuestion() {
            int randomNumber = getRand();
            JSONObject factcapStart = new JSONObject();
            try {
                factcapStart.put("request", "question");
                factcapStart.put("question_id", "" + randomNumber);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            WebSocketManager.getInstance().sendMessage(factcapStart.toString());
        }

    public int getRand() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(75) + 1;
        if (questionIDList.contains(randomNumber)) {
            getRand();
        }
        questionIDList.add(randomNumber);
        return randomNumber;
    }
}