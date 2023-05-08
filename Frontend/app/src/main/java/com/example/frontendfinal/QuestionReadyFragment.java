package com.example.frontendfinal;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("ValidFragment")
public class QuestionReadyFragment extends Fragment {

    View view;
    private TextView readyTextView;
    private CountDownTimer countDownTimer;
    private final int currentPoints;
    private final String displayName;

    @SuppressLint("ValidFragment")
    QuestionReadyFragment () {
        displayName = Player.getDisplayName();
        currentPoints = Player.getCurrentGamePts();
    }

    @SuppressLint("StringFormatInvalid")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question_ready, container, false);
        readyTextView = view.findViewById(R.id.ready_text_view);
        TextView pointsTextView = view.findViewById(R.id.CurrentPts);
        TextView playerDisplayName = view.findViewById(R.id.DisplayName);

        // Set the initial text for the readyTextView
        readyTextView.setText(getString(R.string.question_ready_text, 5));

        // Set current total points
        pointsTextView.setText(String.valueOf(currentPoints));

        //Set the current player's DisplayName
        playerDisplayName.setText(displayName);

        // Create and start the countdown timer
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick (long millisUntilFinished) {
                // Update text for the readyTextView every second
                int secondsLeft = (int) millisUntilFinished / 1000;
                readyTextView.setText(getString(R.string.question_ready_text, secondsLeft));
            }

            @Override
            public void onFinish() {
            }
        }.start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel the countdown timer to prevent leaks
        countDownTimer.cancel();
    }
}