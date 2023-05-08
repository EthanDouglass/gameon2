package com.example.frontendfinal;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * This Fragment shows the word "Correct!" when a player answers the last question correctly
 * Placed into the Answer Fragment
 *
 * TODO - (Idea) Add the player's icon happy or fireworks for correct, and something else for incorrect?
 */

@SuppressLint("ValidFragment")
public class CorrectFragment extends Fragment {

    private TextView pointsTextView;
    private TextView playerDisplayName;
    private int currentPoints;
    private String displayName;
    private CountDownTimer countDownTimer;
    private int answerPts;
    private TextView currentPtsText;

    public CorrectFragment(int answerPts) {
        displayName = Player.getDisplayName();
        currentPoints = Player.getCurrentGamePts();
        this.answerPts = answerPts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_correct, container, false);
        currentPtsText = view.findViewById(R.id.pointsTextView);
        pointsTextView = view.findViewById(R.id.CurrentPts);
        playerDisplayName = view.findViewById(R.id.DisplayName);

        pointsTextView.setText(String.valueOf(currentPoints));
        playerDisplayName.setText(displayName);

        currentPtsText.setText(getString(R.string.correct_points_text, answerPts));
        return view;
    }

}
